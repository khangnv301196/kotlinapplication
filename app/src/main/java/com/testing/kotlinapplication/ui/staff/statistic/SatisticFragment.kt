package com.testing.kotlinapplication.ui.staff.statistic

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import com.testing.kotlinapplication.R
import com.testing.kotlinapplication.network.DataCallBack
import com.testing.kotlinapplication.network.ServiceBuilder
import com.testing.kotlinapplication.network.model.CategoryStatisticsResponse
import com.testing.kotlinapplication.util.Constant
import com.testing.kotlinapplication.util.Preference
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_satistic.*

class SatisticFragment : Fragment() {

    private lateinit var mContext: Context
    private lateinit var mListQuaterly: ArrayList<Int>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_satistic, container, false)
        mContext = view.context
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpLineChart(linechart)
        mListQuaterly = ArrayList()
//        linechart.data = setLineChartData(mListQuaterly)
        setUpPieChart(pieChart)
//        pieChart.data = setPieChartData()

        doGetQuaterlyStatistics(object : DataCallBack<List<Int>> {
            override fun Complete(respon: List<Int>) {
                Log.d("DEBUG", respon.toString())
                mListQuaterly.clear()
                mListQuaterly.addAll(respon)
                linechart.data = setLineChartData(mListQuaterly)
                linechart.notifyDataSetChanged()
                linechart.invalidate()
            }

            override fun Error(error: Throwable) {
                Log.d("DEBUG", error.toString())
            }
        })

        doGetCategoriStatistics(object : DataCallBack<CategoryStatisticsResponse> {
            override fun Complete(respon: CategoryStatisticsResponse) {
                Log.d("CATE", respon.toString())
                pieChart.data = setPieChartData(respon)
                pieChart.notifyDataSetChanged()
                pieChart.invalidate()
            }

            override fun Error(error: Throwable) {
                Log.d("CATE", error.toString())
            }
        })
    }

    fun setUpLineChart(lineChart: LineChart) {
        var xAxis = lineChart.getXAxis()
        xAxis.setPosition(XAxisPosition.BOTTOM)
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(true)

        val leftAxis: YAxis = lineChart.getAxisLeft()
        leftAxis.setLabelCount(5, false)
        leftAxis.axisMinimum = 0f
        lineChart.axisRight.isEnabled = false
        lineChart.animateX(750)
    }

    fun setUpPieChart(pieChart: PieChart) {
        pieChart.description.isEnabled = false
        pieChart.holeRadius = 52f
        pieChart.setTransparentCircleRadius(57f)
//        pieChart.setCenterText(mCenterText)
//        pieChart.setCenterTextTypeface(mTf)
        pieChart.setCenterTextSize(9f)
        pieChart.setUsePercentValues(true)
        pieChart.setExtraOffsets(5f, 10f, 50f, 10f)

        val l: Legend = pieChart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l.orientation = Legend.LegendOrientation.VERTICAL
        l.setDrawInside(false)
        l.yEntrySpace = 0f
        l.yOffset = 0f

        pieChart.animateY(900)
    }

    fun setLineChartData(listQuaterly: ArrayList<Int>): LineData {
        var mList = ArrayList<Entry>()
//        for (i in 0..listQuaterly.size - 1) {
//            mList.add(Entry(i.toFloat(), ((listQuaterly.get(i).toDouble() * 65) + 40).toFloat()))
//        }
        for (i in 0..listQuaterly.size - 1) {
            mList.add(Entry(i.toFloat(), listQuaterly.get(i).toFloat()))
            Log.d("DEBUG", listQuaterly.get(i).toString())
        }
        var d1 = LineDataSet(mList, "Doanh số")
        d1.lineWidth = 2.5f
        d1.circleRadius = 4.5f
        d1.highLightColor = Color.rgb(244, 117, 117)
        d1.setDrawValues(false)
        val sets = ArrayList<ILineDataSet>()
        sets.add(d1)
        return LineData(sets)
    }

    fun setPieChartData(data: CategoryStatisticsResponse): PieData {
        var mList = ArrayList<PieEntry>()
        for (i in 0..data.arrTen.size - 1) {
            Log.d("CATE", data.arrPrice.get(i).toString())
            mList.add(PieEntry(data.arrPrice.get(i).toFloat(), data.arrTen.get(i)))
        }
        val d = PieDataSet(mList, "")
        // space between slices
        d.sliceSpace = 2f

        // add a lot of colors
        val colors = ArrayList<Int>()

        for (c in ColorTemplate.VORDIPLOM_COLORS) colors.add(c)

        for (c in ColorTemplate.JOYFUL_COLORS) colors.add(c)

        for (c in ColorTemplate.COLORFUL_COLORS) colors.add(c)

        for (c in ColorTemplate.LIBERTY_COLORS) colors.add(c)

        for (c in ColorTemplate.PASTEL_COLORS) colors.add(c)


        d.setColors(colors)

        return PieData(d)
    }

    fun doGetQuaterlyStatistics(callBack: DataCallBack<List<Int>>) {
        CompositeDisposable().add(
            ServiceBuilder.buildService()
                .getQuaterlyStatistics(Preference(mContext).getValueString(Constant.TOKEN))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    callBack.Complete(it)
                }, {
                    callBack.Error(it)
                })
        )
    }

    fun doGetCategoriStatistics(callBack: DataCallBack<CategoryStatisticsResponse>) {
        CompositeDisposable().add(
            ServiceBuilder.buildService()
                .getCategoryStatistics(Preference(mContext).getValueString(Constant.TOKEN))
                .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .subscribe({
                    callBack.Complete(it)
                }, {
                    callBack.Error(it)
                })
        )
    }

}