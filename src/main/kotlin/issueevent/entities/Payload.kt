package issueevent.entities

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class IssueEvent(
        @JsonProperty("action") val action: String,
        @JsonProperty("issue") val issue: Issue
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Issue(
        @JsonProperty("id") val id: Int,
        @JsonProperty("created_at") val createdAt: String,
        @JsonProperty("updated_at") val updatedAt: String
)
