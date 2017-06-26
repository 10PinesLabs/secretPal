package com.tenPines.model.process;

import com.tenPines.model.Worker;

import java.util.HashMap;
import java.util.Map;

public class AssignmentException extends RuntimeException {
    private String reason;
    private Map<String, Object> details = new HashMap<>();

    public AssignmentException(Reason reason, Worker worker) {
        super(reason.toString());
        this.reason = reason.toString();
        this.details.put("worker", worker);
    }

    public AssignmentException(Reason reason) {
        super(reason.toString());
        this.reason = reason.toString();
    }

    public String getReason() {
        return reason;
    }

    public Map<String, Object> getDetails() {
        return details;
    }

    public enum Reason{
        DOES_NOT_WANT_TO_PARTICIPATE("Ese usuario no quiere participar"),
        NOT_ENOUGH_QUORUM("No hay suficientes participantes"),
        CANT_SELF_ASSIGN("Ambos usuarios no pueden ser el mismo"),
        RECEIVER_NULL("Alguien debe recibir el regalo"),
        GIVER_NULL("Alguien debe recibir el regalo"),
        CANT_AUTO_ASSIGN("Las reglas del juego no permiten realizar la asignacion automatica");

        private final String text;

        Reason(final String text) {
            this.text = text;
        }

        public String toString() {
            return text;
        }
    }
}
