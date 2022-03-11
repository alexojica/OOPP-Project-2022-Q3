package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.util.List;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

@Entity
@Table(name = "Question")
public class Question {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    @Column(name = "type")
    public int type;

    @Column(name = "text")
    public String text;

    @Column(name = "pointer")
    public Long pointer;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public List<Activity> foundActivities;

    public Question() {
        // for object mapper
    }

    public Question(int type, Long pointer, List<Activity> foundActivities) {
        this.type = type;
        switch (type){
            case 0: this.text = "What requires more energy?";
                break;
            case 1: this.text = "How much energy does it take?";
                break;
            case 2: this.text = "Instead of, this you could do that";
                break;
            default: this.text = "This question has no type.";
        }
        this.text = text;
        this.pointer = pointer;
        this.foundActivities = foundActivities;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getPointer() {
        return pointer;
    }

    public void setPointer(Long pointer) {
        this.pointer = pointer;
    }

    public List<Activity> getFoundActivities() {
        return foundActivities;
    }

    public void setFoundActivities(List<Activity> foundActivities) {
        this.foundActivities = foundActivities;
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
