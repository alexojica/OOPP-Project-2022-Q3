package commons;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

@Entity
public class Activity {

    @Id
    @Column(name = "activityID")
    private String id;
    @JsonProperty("image_path")
    private String imagePath;
    private String title;
    @JsonProperty("consumption_in_wh")
    private Long energyConsumption;
    private String source;

    public Activity() {
    }

    public Activity(String id, String imagePath, String title, Long energyConsumption, String source) {
        this.id = id;
        this.imagePath = imagePath;
        this.title = title;
        this.energyConsumption = energyConsumption;
        this.source = source;
    }

    public void setEnergyConsumption(Long energyConsumption) {
        this.energyConsumption = energyConsumption;
    }

    public String getId() {
        return id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getTitle() {
        return title;
    }

    public Long getEnergyConsumption() {
        return energyConsumption;
    }

    public String getSource() {
        return source;
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }
}
