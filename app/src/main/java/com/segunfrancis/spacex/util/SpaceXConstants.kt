package com.segunfrancis.spacex.util

import com.segunfrancis.spacex.BuildConfig

object SpaceXConstants {
    const val BASE_URL: String = "https://api.spacexdata.com/v3/"
    const val CONNECTION_TIMEOUT: Long = 30L
    const val DATE_TIME_PATTERN: String = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    const val LOCAL_DB_NAME: String = BuildConfig.APPLICATION_ID.plus("spacex_database")
    const val YEARS_BUNDLE_KEY: String = "years_key"
    const val LAUNCH_STATE_BUNDLE_KEY: String = "launch_state_key"
    const val ORDER_BUNDLE_KEY: String = "order_key"
    const val QUERY_STRING_FRAGMENT_RESULT_KEY: String = "query_string"
}
