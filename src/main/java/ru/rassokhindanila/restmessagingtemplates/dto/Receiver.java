package ru.rassokhindanila.restmessagingtemplates.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.rassokhindanila.restmessagingtemplates.enums.ReceiverType;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@NoArgsConstructor
@Embeddable
public class Receiver implements Serializable {

    private ReceiverType receiverType;

    private String destination;

    @JsonCreator
    public Receiver(@JsonProperty("receiver_type") ReceiverType receiverType,
                    @JsonProperty("destination") String destination)
    {
        this.receiverType = receiverType;
        this.destination = destination;
    }
}
