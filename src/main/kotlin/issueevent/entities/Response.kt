package issueevent.entities

import com.fasterxml.jackson.annotation.JsonProperty

data class Event(
        @JsonProperty("action") val action: String,
        @JsonProperty("created_at") val createdAt: String,
        @JsonProperty("updated_at") val updatedAt: String
)

data class Statistics(
        @JsonProperty("open") var open: Int,
        @JsonProperty("closed") var closed: Int
)
