package us.abstracta.jmeter.javadsl.azure.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class TestRun {

  private final String testRunId;
  private final String displayName;
  private final String testId;
  private final String status;
  private final Integer virtualUsers;
  private final Instant startDateTime;
  private final Instant endDateTime;
  private final Map<String, TransactionStats> testRunStatistics;
  private final String portalUrl;

  public TestRun(String testId) {
    this(UUID.randomUUID().toString(), buildName(), testId, "ACCEPTED", null, null,
        null, null, Collections.emptyMap());
  }

  @JsonCreator
  public TestRun(@JsonProperty("testRunId") String id,
      @JsonProperty("displayName") String displayName,
      @JsonProperty("testId") String testId, @JsonProperty("status") String status,
      @JsonProperty("virtualUsers") Integer virtualUsers,
      @JsonProperty("startDateTime") Instant startDateTime,
      @JsonProperty("endDateTime") Instant endDateTime, @JsonProperty("portalUrl") String portalUrl,
      @JsonProperty("testRunStatistics") Map<String, TransactionStats> testRunStatistics) {
    this.testRunId = id;
    this.displayName = displayName;
    this.testId = testId;
    this.status = status;
    this.virtualUsers = virtualUsers;
    this.startDateTime = startDateTime;
    this.endDateTime = endDateTime;
    this.portalUrl = portalUrl;
    this.testRunStatistics = testRunStatistics;
  }

  private static String buildName() {
    return "TestRun_" + new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(
        Date.from(Instant.now()));
  }

  public String getId() {
    return testRunId;
  }

  @JsonIgnore
  public boolean isAccepted() {
    return "ACCEPTED".equals(status);
  }

  @JsonIgnore
  public boolean isEnded() {
    return "DONE".equals(status) || "FAILED".equals(status) || "CANCELLED".equals(status);
  }

  @JsonIgnore
  public boolean isSuccess() {
    return "DONE".equals(status);
  }

  public String getStatus() {
    return status;
  }

  public Integer getVirtualUsers() {
    return virtualUsers;
  }

  public Map<String, TransactionStats> getTestRunStatistics() {
    return testRunStatistics;
  }

  public Instant getStartTime() {
    return startDateTime;
  }

  public Instant getEndTime() {
    return endDateTime;
  }

  public String getUrl() {
    return portalUrl;
  }

}
