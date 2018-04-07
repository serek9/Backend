package com.proyecto.serflix.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A FriendRequest.
 */
@Entity
@Table(name = "friend_request")
public class FriendRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "sent_on")
    private ZonedDateTime sentOn;

    @Column(name = "resolved_on")
    private ZonedDateTime resolvedOn;

    @Column(name = "accepted")
    private Boolean accepted;

    @ManyToOne
    private User sender;

    @ManyToOne
    private User receiver;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getSentOn() {
        return sentOn;
    }

    public FriendRequest sentOn(ZonedDateTime sentOn) {
        this.sentOn = sentOn;
        return this;
    }

    public void setSentOn(ZonedDateTime sentOn) {
        this.sentOn = sentOn;
    }

    public ZonedDateTime getResolvedOn() {
        return resolvedOn;
    }

    public FriendRequest resolvedOn(ZonedDateTime resolvedOn) {
        this.resolvedOn = resolvedOn;
        return this;
    }

    public void setResolvedOn(ZonedDateTime resolvedOn) {
        this.resolvedOn = resolvedOn;
    }

    public Boolean isAccepted() {
        return accepted;
    }

    public FriendRequest accepted(Boolean accepted) {
        this.accepted = accepted;
        return this;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }

    public User getSender() {
        return sender;
    }

    public FriendRequest sender(User user) {
        this.sender = user;
        return this;
    }

    public void setSender(User user) {
        this.sender = user;
    }

    public User getReceiver() {
        return receiver;
    }

    public FriendRequest receiver(User user) {
        this.receiver = user;
        return this;
    }

    public void setReceiver(User user) {
        this.receiver = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FriendRequest friendRequest = (FriendRequest) o;
        if (friendRequest.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, friendRequest.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "FriendRequest{" +
            "id=" + id +
            ", sentOn='" + sentOn + "'" +
            ", resolvedOn='" + resolvedOn + "'" +
            ", accepted='" + accepted + "'" +
            '}';
    }
}
