package commons;

import constants.QuestionTypes;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.util.List;

import static constants.QuestionTypes.MULTIPLE_CHOICE_QUESTION;
import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

@Entity
@Table(name = "Question")
public class Question {

    @Id
    @Column(name = "id")
    public Long id;

    @Column(name = "type")
    public QuestionTypes type;

    @Column(name = "text")
    public String text;

    @Column(name = "pointer")
    public Long pointer;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    public List<Activity> foundActivities;

    @Column(name = "lastLobbyToken")
    public String lastLobbyToken;

    public Question() {
        // for object mapper
    }

    public Question(Long id, QuestionTypes type, Long pointer, List<Activity> foundActivities, String lastLobbyToken) {
        this.id = id;
        this.type = type;
        this.lastLobbyToken = lastLobbyToken;
        switch (type){
            case MULTIPLE_CHOICE_QUESTION: this.text = "What requires more energy?";
                break;
            case ESTIMATION_QUESTION: this.text = "How much energy does it take?";
                break;
            case ENERGY_ALTERNATIVE_QUESTION: this.text = "Instead of, this you could do that";
                break;
            default: this.text = "This question has no type.";
        }
        this.text = text;
        this.pointer = pointer;
        this.foundActivities = foundActivities;
    }

    public QuestionTypes getType() {
        return type;
    }

    public void setType(QuestionTypes type) {
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

    public String getLastLobbyToken() {
        return lastLobbyToken;
    }

    public void setLastLobbyToken(String lastLobbyToken) {
        this.lastLobbyToken = lastLobbyToken;
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
