package org.example.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String message;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Message(String message, User user) {
        this.message = message;
        this.user = user;
    }

    @Override
    public String toString() {
        return "id " + id + "\nmessage " + message + "\nuser name " + user.getUsername() + user.getId();
    }
}
