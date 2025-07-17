package pt.ul.fc.css.soccernow.dtos;

import java.util.List;

public class TableGamesDTO {
    
    private Long id;
    private List<String> homeTeam;
    private int totalHomeGoals;
    private List<String> awayTeam;
    private int totalAwayGoals;
    private String location;
    private String turno;
    private String estado;

    public TableGamesDTO(){}

    public TableGamesDTO(Long id, List<String> homeTeam, int totalHomeGoals, List<String> awayTeam, int totalAwayGoals,
            String location, String turno, String estado) {
        this.id = id;
        this.homeTeam = homeTeam;
        this.totalHomeGoals = totalHomeGoals;
        this.awayTeam = awayTeam;
        this.totalAwayGoals = totalAwayGoals;
        this.location = location;
        this.turno = turno;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<String> getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(List<String> homeTeam) {
        this.homeTeam = homeTeam;
    }

    public int getTotalHomeGoals() {
        return totalHomeGoals;
    }

    public void setTotalHomeGoals(int totalHomeGoals) {
        this.totalHomeGoals = totalHomeGoals;
    }

    public List<String> getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(List<String> awayTeam) {
        this.awayTeam = awayTeam;
    }

    public int getTotalAwayGoals() {
        return totalAwayGoals;
    }

    public void setTotalAwayGoals(int totalAwayGoals) {
        this.totalAwayGoals = totalAwayGoals;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    
    
}
