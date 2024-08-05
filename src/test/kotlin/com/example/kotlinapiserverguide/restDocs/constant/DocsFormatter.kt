package com.example.kotlinapiserverguide.restDocs.constant

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DocsFormatter {
    companion object {
        var DATETIME = "yyyy-mm-dd HH:mm:ss"
        var DATE = "yyyy-mm-dd"

        var datetimeToString: (datetime: LocalDateTime?) -> (String) = { datetime ->
            if (datetime != null)
                DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(datetime)
            else
                DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now())
        }

        var datetimeNowToString: () -> (String) = {
            DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now())
        }
    }
}