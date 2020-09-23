package com.testing.kotlinapplication.ui.staff.statistic

import android.graphics.Color
import android.os.Bundle
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
import kotlinx.android.synthetic.main.fragment_satistic.*

class SatisticFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_satistic, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpLineChart(linechart)
        linechart.data = setLineChartData()
        setUpPieChart(pieChart)
        pieChart.data = setPieChartData()
    }

    fun setUpLineChart(lineChart: LineChart) {
        var xAxis = lineChart.getXAxis()
        xAxis.setPosition(XAxisPosition.BOTTOM)
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(true)

        val leftAxis: YAxis = lineChart.getAxisLeft()
        leftAxis.setLabelCount(5, false)
        leftAxis.axisMinimum = 0f

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

    fun setLineChartData(): LineData {
        var mList = ArrayList<Entry>()
        for (i in 0..12) {
            mList.add(Entry(i.toFloat(), ((Math.random() * 65) + 40).toFloat()))
        }
        var d1 = LineDataSet(mList, "New DataSet " + "zero" + ", (1)")
        d1.lineWidth = 2.5f
        d1.circleRadius = 4.5f
        d1.highLightColor = Color.rgb(244, 117, 117)
        d1.setDrawValues(false)
        val sets = ArrayList<ILineDataSet>()
        sets.add(d1)
        return LineData(sets)
    }

    fun setPieChartData(): PieData {
        var mList = ArrayList<PieEntry>()
        for (i in 0..2) {
            mList.add(PieEntry((Math.random() * 70 + 30).toFloat(), "Quater"))
        }
        val d = PieDataSet(mList, "")
        // space between slices
        d.sliceSpace = 2f
        d.setColors(*ColorTemplate.VORDIPLOM_COLORS)

        return PieData(d)
    }

}