package com.lmm.android.recyclerviewselectiondemo

import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StableIdKeyProvider
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private val items = arrayListOf<String>().apply {
        for (i in 0 until 100) {
            add("Item $i")
        }
    }

    val TAG = MainActivity::class.java.simpleName
    private var mSelectionTracker: SelectionTracker<String>? = null
    private var mItemDetailListAdapter: ItemDetailListAdapter? = null
    private var mIsEdit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        findViewById<Button>(R.id.button).setOnClickListener {
            changeEditState()
        }
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val adapter = ItemDetailListAdapter(items)
        mItemDetailListAdapter = adapter
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView).apply {
            layoutManager = LinearLayoutManager(this@MainActivity).apply {
                orientation = LinearLayoutManager.VERTICAL
            }
            this.adapter = adapter
        }

        val selectionTracker = SelectionTracker.Builder(
            "my-selection-id",
            recyclerView,
            StringItemKeyProvider(1, items), // 这里也可以传入一个StableIdKeyProvider，用来生成唯一的id
            StringItemDetailLookup(recyclerView),
            StorageStrategy.createStringStorage()
        ).withSelectionPredicate(SelectionPredicates.createSelectAnything()).build()
        mSelectionTracker = selectionTracker

        recyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            ).apply {
                AppCompatResources.getDrawable(this@MainActivity, R.drawable.divider_line)?.apply {
                    setDrawable(this)
                }
            })
//        initRecyclerListenerWithTouchListener(recyclerView)
        initRecyclerListenerWithGestureDetector(recyclerView)
    }

    /**
     * 使用简单TouchListener监听RecyclerView的点击事件
     *
     * @param recyclerView RecyclerView实例
     */
    private fun initRecyclerListenerWithTouchListener(recyclerView: RecyclerView) {
        val touchListener = object : RecyclerView.OnItemTouchListener {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                if (e.action == MotionEvent.ACTION_UP) {
                    val child = recyclerView.findChildViewUnder(e.x, e.y)
                    if (child != null) {
                        val position = recyclerView.getChildAdapterPosition(child)
                        if (position != -1) {
                            Log.e(TAG, "onClick position: $position")
                        }
                    }
                }
                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {

            }

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {

            }
        }
        recyclerView.addOnItemTouchListener(touchListener)
    }

    /**
     * 使用GestureDetector监听RecyclerView的点击事件，单击和长按
     *
     * @param recyclerView RecyclerView实例
     *
     */
    private fun initRecyclerListenerWithGestureDetector(recyclerView: RecyclerView) {

        recyclerView.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
            var gestureDetector: GestureDetectorCompat = GestureDetectorCompat(this@MainActivity, object : GestureDetector.SimpleOnGestureListener() {
                override fun onSingleTapUp(e: MotionEvent): Boolean {
                    val child = recyclerView.findChildViewUnder(e.x, e.y)
                    if (child != null) {
                        val position = recyclerView.getChildAdapterPosition(child)
                        if (position != -1) {
                            Log.e(TAG, "onClick position: $position")
                        }
                    }
                    return super.onSingleTapUp(e)
                }

                override fun onLongPress(e: MotionEvent) {
                    val child = recyclerView.findChildViewUnder(e.x, e.y)
                    if (child != null) {
                        val position = recyclerView.getChildAdapterPosition(child)
                        if (position != -1) {
                            Log.e(TAG, "longPress position: $position")
                        }
                    }
                    super.onLongPress(e)
                }
            })

            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                gestureDetector.onTouchEvent(e)
                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
            }

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mSelectionTracker?.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        mSelectionTracker?.onRestoreInstanceState(savedInstanceState)
    }

    private fun changeEditState() {
        mIsEdit = !mIsEdit
        if (!mIsEdit) {
            mSelectionTracker?.clearSelection()
        }
        mItemDetailListAdapter?.setSelectionTracker(if (mIsEdit) mSelectionTracker else null)
    }
}