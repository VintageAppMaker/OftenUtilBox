### RecyclerView 이동시 좌측정렬
> 횡스크롤 되는 리사이클러 뷰에서 스크롤 후, 자동으로 좌측정렬하는 앱들이 종종있다.

1. addOnScrollListener의 파라메터로 onScrollStateChanged를 구현한다.
2. newState == RecyclerView.SCROLL_STATE_IDLE일 경우, 스크롤이 완료된 것이다.
3. 리사이클러 뷰에서 최초로 보이는 뷰의 좌측이 시작되는 곳까지 이동거리를 구한다.
4. smoothScrollBy로 해당위치로 이동한다.

- ViewExt.kt
~~~kotlin
// 레퍼런스 :
// https://stackoverflow.com/questions/26370289/snappy-scrolling-in-recyclerview/33774983
fun RecyclerView.getScrollDistanceOfColumnClosestToLeft(): Int {
    val manager = layoutManager as LinearLayoutManager?
    val firstVisibleColumnViewHolder = findViewHolderForAdapterPosition(
        manager!!.findFirstVisibleItemPosition()
    ) ?: return 0
    val columnWidth = firstVisibleColumnViewHolder.itemView.measuredWidth
    val left = firstVisibleColumnViewHolder.itemView.left
    val absoluteLeft = Math.abs(left)
    return if (absoluteLeft <= columnWidth / 2) left else columnWidth - absoluteLeft
}

fun RecyclerView.setMagneticMove(){
    addOnScrollListener(object: RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            val moveTo = getScrollDistanceOfColumnClosestToLeft()
            if(newState == RecyclerView.SCROLL_STATE_IDLE){
                recyclerView.smoothScrollBy(moveTo, 0)
            }
        }
    })
}

~~~

사용법은 recycler를 생성 후, setMagneticMove를 호출해주기만 하면된다.

[MainActivity.kt](/app/src/main/java/oftenutilbox/viam/psw/example/MainActivity.kt)
~~~kotlin
    private fun testMagneticRecyclerView() {
        QuickExampleActivity.launch(this, { setContent ->
            val binding: Example3Binding
            binding = Example3Binding.inflate(layoutInflater)
            setContent(binding.root)

            binding.apply {
                val lst = mutableListOf<SimpleData>()
                val colortable = listOf(Color.RED, Color.GRAY, Color.BLUE, Color.GREEN, Color.WHITE)
                (0..30).forEach {
                    val item = Box(color = colortable.get( it % colortable.size))
                    lst.add( item as SimpleData )
                }

                recycler.setMagneticMove()

                val manager = LinearLayoutManager(applicationContext, RecyclerView.HORIZONTAL, false)
                recycler.layoutManager = manager
                val adt = MagneticAdapter(lst, applicationContext)
                recycler.adapter = adt
            }

        })
    }
~~~
