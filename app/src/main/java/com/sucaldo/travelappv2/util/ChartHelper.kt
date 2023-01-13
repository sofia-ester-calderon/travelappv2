package com.sucaldo.travelappv2.util

import com.anychart.APIlib
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.BubbleDataEntry
import com.anychart.chart.common.dataentry.CategoryValueDataEntry
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Cartesian
import com.anychart.charts.TagCloud
import com.anychart.enums.*
import com.anychart.scales.OrdinalColor
import com.sucaldo.travelappv2.data.CONTINENTS
import com.sucaldo.travelappv2.data.Trip
import com.sucaldo.travelappv2.features.statistics.PlacesCloudType
import java.util.*

class ChartHelper {
    private val FONT_SIZE = 10
    private val COLOR_SCHEMA = arrayOf(
        "#fa70b5", "#8e44ad", "#00c3ff", "#009175", "#34eb31", "#ffd000", "#ff0404", "#a8a8a8"
    )

    /*
     ********* BAR CHART **********************
     */
    fun initTop10PlacesChart(
        anyChartView: AnyChartView,
        data: List<DataEntry?>?
    ): Cartesian {
        APIlib.getInstance().setActiveAnyChartView(anyChartView)
        val barChart = AnyChart.column()
        val column = barChart.column(data)
        column.tooltip()
            .titleFormat("{%X}")
            .position(Position.CENTER_BOTTOM)
            .anchor(Anchor.CENTER_BOTTOM)
            .offsetX(0.0)
            .offsetY(5.0)
            .format("{%Value}{groupsSeparator: }")
        barChart.animation(true)
        barChart.yScale().minimum(0.0)
        barChart.yScale().ticks().interval(2)
        barChart.yAxis(0).labels().format("{%Value}{groupsSeparator: }")
        barChart.tooltip().positionMode(TooltipPositionMode.POINT).fontSize(FONT_SIZE)
        barChart.interactivity().hoverMode(HoverMode.BY_X)
        barChart.yAxis(0).title(TOP_PLACES_Y_AXIS)
        barChart.xAxis(0).title().fontSize(FONT_SIZE)
        barChart.yAxis(0).title().fontSize(FONT_SIZE)
        barChart.xAxis(0).staggerMode(true)
        barChart.xAxis(0).staggerLines(2)
        barChart.xAxis(0).labels().fontSize(FONT_SIZE)
        barChart.yAxis(0).labels().fontSize(FONT_SIZE)
        anyChartView.setChart(barChart)
        return barChart
    }

    fun updateChart(barChart: Cartesian, data: List<DataEntry?>?) {
        barChart.data(data)
    }

    /*
     ********* CLOUD CHART **********************
     */
    fun initCountriesCloudChart(
        anyChartView: AnyChartView,
        data: List<DataEntry?>?
    ): TagCloud {
        APIlib.getInstance().setActiveAnyChartView(anyChartView)
        val tagCloud = AnyChart.tagCloud()
        val ordinalColor = OrdinalColor.instantiate()
        ordinalColor.colors(COLOR_SCHEMA.clone())
        tagCloud.colorScale(ordinalColor)
        tagCloud.angles(arrayOf(-90.0, 0.0, 90.0))
        tagCloud.colorRange().enabled(true)
        tagCloud.colorRange().colorLineSize(5.0)
        tagCloud.tooltip().useHtml(true)
        tagCloud.tooltip().format(TAG_CLOUD_COUNTRIES_TOOLTIP)
        tagCloud.data(data)
        anyChartView.setChart(tagCloud)
        return tagCloud
    }

    private val TAG_CLOUD_COUNTRIES_TOOLTIP = "function() {return '" +
            "<p style=\"color: #d2d2d2; font-size: 15px\"> Trips:' + this.getData('value') + '</p>" +
            " <table style=\"color: #d2d2d2; font-size: 15px\">" +
            "' + this.getData('html') + '</table>';}"

    fun updateTagCloud(tagCloud: TagCloud, type: PlacesCloudType, data: List<DataEntry?>?) {
        val tooltipFormat = when (type) {
            PlacesCloudType.COUNTRIES -> TAG_CLOUD_COUNTRIES_TOOLTIP
            PlacesCloudType.PLACES -> "Trips: {%value}"
        }
        tagCloud.tooltip().format(tooltipFormat)
        tagCloud.data(data)
    }

    class CustomCategoryValueDataEntry(x: String?, category: String?, value: Int?) :
        CategoryValueDataEntry(x, category, value) {
        fun setTripsInfo(trips: List<Trip>) {
            val html = StringBuilder()
            // A max of 15 trips can fit nicely into tooltip based on current Tablet size
            val selectedTrips = if (trips.size < 15) trips else selectRandomTrips(trips)
            for (trip in selectedTrips) {
                html.append("<tr> <td>")
                    .append(formatDate(trip.startDate))
                    .append("</td>")
                    .append("<td> <b>")
                    .append(trip.toCity)
                    .append("</b> </td> </tr>")
            }
            setValue("html", html.toString())
        }
    }


    /*
     ********* AREA CHART **********************
     */
    fun initKmsAreaChart(anyChartView: AnyChartView, data: List<DataEntry?>?) {
        APIlib.getInstance().setActiveAnyChartView(anyChartView)
        val areaChart = AnyChart.area()
        areaChart.animation(true)
        val crosshair = areaChart.crosshair()
        crosshair.enabled(true)
        crosshair.yLabel(0).enabled(true)
        areaChart.yScale().stackMode(ScaleStackMode.VALUE)
        val set = com.anychart.data.Set.instantiate()
        set.data(data)
        for (i in 0 until CONTINENTS.size) {
            val valuePostfix = i + 1
            val seriesDataMap = set.mapAs("{ x: 'x', value: 'value$valuePostfix' }")
            val series = areaChart.area(seriesDataMap)
            series.name(CONTINENTS[i])
            series.stroke("3 #fff")
            series.hovered().stroke("3 #fff")
            series.hovered().markers().enabled(true)
            series.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4.0)
                .stroke("1.5 #fff")
            series.markers().zIndex(100.0)
        }
        areaChart.legend().enabled(true)
        areaChart.legend().fontSize(13.0)
        areaChart.legend().padding(0.0, 0.0, 20.0, 0.0)
        areaChart.interactivity().hoverMode(HoverMode.BY_X)
        areaChart.xAxis(0).labels().fontSize(FONT_SIZE)
        areaChart.yAxis(0).title().fontSize(FONT_SIZE)
        areaChart.yAxis(0).labels().fontSize(FONT_SIZE)
        areaChart.tooltip()
            .valuePostfix("kms")
            .fontSize(15)
            .displayMode(TooltipDisplayMode.UNION)
        areaChart.xAxis(0).title(false)
        areaChart.xAxis(0).ticks(false)
        areaChart.yScale().ticks().interval(10000)
        anyChartView.setChart(areaChart)
    }

    class CustomDataEntry(x: String?, values: List<Int?>) :
        ValueDataEntry(x, 0) {
        init {
            var i = 1
            for (value in values) {
                val key = "value" + i++
                setValue(key, value)
            }
        }
    }

    /*
     ********* BUBBLE CHART **********************
     */
    fun initKmsBubbleChart(
        anyChartView: AnyChartView,
        allYears: List<Int>,
        data: List<DataEntry?>?
    ) {
        APIlib.getInstance().setActiveAnyChartView(anyChartView)
        val bubble = AnyChart.bubble()
        bubble.animation(true)
        bubble.xScale().minimum(allYears[0] - 1)
        bubble.xScale().maximum(allYears[allYears.size - 1] + 1)
        bubble.yAxis(0)
            .title("Number of Trips")
        bubble.yGrid(true)
        bubble.bubble(data).name("Details").selected().fill("#31eb97", 0.5)
        bubble.padding(20.0, 20.0, 10.0, 20.0)
        bubble.minBubbleSize(10.0)
            .maxBubbleSize(30.0)
        bubble.yAxis(0).labels().fontSize(FONT_SIZE)
        bubble.yAxis(0).title().fontSize(FONT_SIZE)
        bubble.xAxis(0).labels().fontSize(FONT_SIZE)
        bubble.tooltip()
            .useHtml(true)
            .fontColor("#fff")
            .format(
                """function() {
        return '<div style="width: 175px; font-size: 15px">            Year: <span style="color: #d2d2d2; font-size: 15px">' +
          this.getData('x') + '</span></strong><br/>' +
          'Trips: <span style="color: #d2d2d2; font-size: 15px">' +
          this.getData('value') + '</span></strong><br/>' +
          'Distance: <span style="color: #d2d2d2; font-size: 15px">' +
          this.getData('size') + ' kms.</span></strong><br/>' +
          'Countries: <span style="color: #d2d2d2; font-size: 15px">' +
          this.getData('countries') + '</span></strong><br/> </div>';
      }"""
            )
        anyChartView.setChart(bubble)
    }

    class CustomBubbleDataEntry(x: Int?, value: Int?, size: Int?, countries: List<String?>) :
        BubbleDataEntry(x, value, size) {
        init {
            val joiner = StringJoiner(", ")
            for (country in countries) {
                joiner.add(country)
            }
            setValue("countries", joiner.toString())
        }
    }

    companion object {
        private fun selectRandomTrips(trips: List<Trip>): List<Trip> {
            val randomTrips: MutableList<Trip> = arrayListOf()
            while (randomTrips.size < 15) {
                val r = Random()
                val low = 0
                val high = trips.size - 1
                val result = r.nextInt(high - low) + low
                randomTrips.add(trips[result])
            }
            randomTrips.sortBy { it.startDate }
            return randomTrips
        }

        private const val TOP_PLACES_Y_AXIS = "Number of Visits"
    }
}