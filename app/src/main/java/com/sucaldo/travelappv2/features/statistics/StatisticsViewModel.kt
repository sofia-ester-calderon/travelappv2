package com.sucaldo.travelappv2.features.statistics

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.anychart.AnyChartView
import com.sucaldo.travelappv2.db.DatabaseHelper
import com.sucaldo.travelappv2.util.ChartHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.*

class StatisticsViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(StatisticsUiState())
    val uiState: StateFlow<StatisticsUiState> = _uiState.asStateFlow()
    private val myDb: DatabaseHelper
    private val chartHelper: ChartHelper

    init {
        myDb = DatabaseHelper(application.applicationContext)
        chartHelper = ChartHelper(myDb)
        _uiState.update { it.copy(topTenData = myDb.getTop10VisitedPlaces()) }
    }

    fun goToNextStatistic() {
        _uiState.update {
            it.copy(
                statisticsType = when (it.statisticsType) {
                    StatisticsType.TOP_PLACES -> StatisticsType.PLACES_CLOUD
                    StatisticsType.PLACES_CLOUD -> StatisticsType.DISTANCE_CONTINENT
                    StatisticsType.DISTANCE_CONTINENT -> StatisticsType.DISTANCE_BUBBLE
                    StatisticsType.DISTANCE_BUBBLE -> StatisticsType.TOP_PLACES
                }
            )
        }
    }

    fun goToPreviousStatistic() {
        _uiState.update {
            it.copy(
                statisticsType = when (it.statisticsType) {
                    StatisticsType.TOP_PLACES -> StatisticsType.DISTANCE_BUBBLE
                    StatisticsType.PLACES_CLOUD -> StatisticsType.TOP_PLACES
                    StatisticsType.DISTANCE_CONTINENT -> StatisticsType.PLACES_CLOUD
                    StatisticsType.DISTANCE_BUBBLE -> StatisticsType.DISTANCE_CONTINENT
                }
            )
        }
    }

    fun setTopTenChart(topTenChartView: AnyChartView) {
        val barChart = chartHelper.initTop10PlacesChart(topTenChartView, _uiState.value.topTenData)
        _uiState.update { it.copy(topTenBarChart = barChart) }
    }

    fun updateTopTenChart(topPlacesType: TopPlacesType) {
        _uiState.value.topTenBarChart?.let {
            val years = when (topPlacesType) {
                TopPlacesType.OVERALL -> listOf()
                TopPlacesType.LAST_TWO -> getLastNYears(2)
                TopPlacesType.LAST_FIVE -> getLastNYears(5)
                TopPlacesType.LAST_TEN -> getLastNYears(10)
            }
            chartHelper.updateChart(it, myDb.getTop10VisitedPlaces(years))
        }
        _uiState.update { it.copy(topTenType = topPlacesType) }
    }

    private fun getLastNYears(n: Int): List<String> {
        val currentYear = Calendar.getInstance()[Calendar.YEAR]
        val years: MutableList<String> = ArrayList()
        years.add(currentYear.toString())
        for (i in 1..n) {
            val year = currentYear - i
            years.add(year.toString())
        }
        return years
    }
}