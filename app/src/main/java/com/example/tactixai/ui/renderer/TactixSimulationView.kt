package com.example.tactixai.ui.renderer

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.example.tactixai.core.engine.AgentStateBuffer
import com.example.tactixai.core.intelligence.InfluenceMap
import com.example.tactixai.core.model.Agent
import com.example.tactixai.core.model.AgentStatus

/**
 * Ultimate Renderer for Tactix AI.
 * Fitur: Heatmap Strategis, Zoom, Pan, dan LOD untuk 10.000+ unit.
 */
class TactixSimulationView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : SurfaceView(context, attrs, defStyleAttr), SurfaceHolder.Callback, Runnable {

    private var drawingThread: Thread? = null
    private var isRunning = false
    private var agents: List<Agent> = emptyList()
    private var stateBuffer: AgentStateBuffer? = null
    private var influenceMap: InfluenceMap? = null
    private var heatmapRenderer: HeatmapRenderer? = null

    // Configuration
    var showHeatmap = true
    var showUnitDetails = true

    // Camera State
    private var offsetX = 0f
    private var offsetY = 0f
    private var scaleFactor = 1.0f

    private val agentPaint = Paint().apply { isAntiAlias = false }
    private val textPaint = Paint().apply { color = Color.WHITE; textSize = 30f; isFakeBoldText = true }

    private val scaleGestureDetector = ScaleGestureDetector(context, object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            scaleFactor *= detector.scaleFactor
            scaleFactor = scaleFactor.coerceIn(0.05f, 10.0f)
            return true
        }
    })

    init {
        holder.addCallback(this)
        setWillNotDraw(false)
    }

    fun setSimulationContext(buffer: AgentStateBuffer, influenceMap: InfluenceMap) {
        this.stateBuffer = buffer
        this.influenceMap = influenceMap
        if (heatmapRenderer == null) {
            heatmapRenderer = HeatmapRenderer(influenceMap)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        scaleGestureDetector.onTouchEvent(event)
        return true
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        isRunning = true
        drawingThread = Thread(this).apply { start() }
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        isRunning = false
        drawingThread?.join()
    }

    override fun run() {
        while (isRunning) {
            val canvas = holder.lockCanvas() ?: continue
            try {
                drawSimulation(canvas)
            } finally {
                holder.unlockCanvasAndPost(canvas)
            }
        }
    }

    private fun drawSimulation(canvas: Canvas) {
        canvas.drawColor(Color.parseColor("#050505")) // Ultra Dark Tactical Background

        canvas.save()
        canvas.translate(offsetX, offsetY)
        canvas.scale(scaleFactor, scaleFactor)

        // 1. Render Layer Strategis: Heatmap (Z-Index: Bottom)
        if (showHeatmap) {
            heatmapRenderer?.render(canvas, 4000, 4000, 50f)
        }

        // 2. Render Layer Taktis: Agents (Z-Index: Top)
        val buffer = stateBuffer ?: return
        val isLowDetail = scaleFactor < 0.3f
        val capacity = buffer.capacity

        for (i in 0 until capacity) {
            if (!buffer.active[i]) continue

            agentPaint.color = when (buffer.domain[i]) {
                0 -> Color.parseColor("#00E5FF") // DRONE
                1 -> Color.parseColor("#76FF03") // ROBOT
                else -> Color.parseColor("#2979FF") // SUBMARINE
            }

            if (isLowDetail) {
                canvas.drawPoint(buffer.x[i], buffer.y[i], agentPaint)
            } else {
                canvas.drawCircle(buffer.x[i], buffer.y[i], 8f, agentPaint)
                
                if (showUnitDetails && scaleFactor > 1.0f) {
                    canvas.drawLine(
                        buffer.x[i], buffer.y[i], 
                        buffer.x[i] + buffer.vx[i] * 0.1f, 
                        buffer.y[i] + buffer.vy[i] * 0.1f, 
                        agentPaint
                    )
                }
            }
        }
        
        canvas.restore()
        
        // 3. Render Layer UI: Diagnostics
        drawOverlay(canvas)
    }

    private fun drawOverlay(canvas: Canvas) {
        val margin = 40f
        canvas.drawText("TACTIX AI OS | ACTIVE AGENTS: ${agents.size}", margin, margin + 40, textPaint)
        canvas.drawText("ZOOM: ${String.format("%.2f", scaleFactor)}x", margin, margin + 80, textPaint)
        
        if (showHeatmap) {
            textPaint.color = Color.parseColor("#00E5FF")
            canvas.drawText("STRATEGIC HEATMAP: ON", margin, margin + 120, textPaint)
            textPaint.color = Color.WHITE
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}
}
