package services;

import hibernate_classes.Passenger;
import hibernate_classes.Trip;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class PassengerService {
    private static final String url = "jdbc:postgresql://localhost:5432/DB_JDBC";
    private static final String user = "postgres";
    private static final String pass = "2023";
    Connection con;
    Statement st;
    PreparedStatement pst;

    public Passenger getById(int id) throws SQLException {
        Passenger passenger = new Passenger();
        try {
            con = DriverManager.getConnection(url, user, pass);
            st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM passengers WHERE psg_id=" + id);
            while (rs.next()) {
                passenger.setId((long) rs.getInt("id"));
                passenger.setName(rs.getString("name"));
                passenger.setPhone(rs.getString("phone"));
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        } finally {
            st.close();
            con.close();
        }
        return passenger;
    }

    public Set<Passenger> getAll() throws SQLException {
        Set<Passenger> passengerSet = new HashSet<>();
        try {
            con = DriverManager.getConnection(url, user, pass);
            st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM passengers");
            while (rs.next()) {
                passengerSet.add(new Passenger(
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            st.close();
            con.close();
        }
        return passengerSet;
    }

    public void savePassenger(Passenger passenger) throws SQLException {
        try {
            con = DriverManager.getConnection(url, user, pass);
            pst = con.prepareStatement("insert into passengers(name,phone)values (?,?)");
            pst.setString(1, passenger.getName());
            pst.setString(2, passenger.getPhone());
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            pst.close();
            con.close();
        }
    }

    public void update(Passenger passenger, int psgId) throws SQLException, RuntimeException {
        try {
            con = DriverManager.getConnection(url, user, pass);
            pst = con.prepareStatement("UPDATE passengers SET  name=?,phone=? WHERE psg_id=?");
            pst.setString(1, passenger.getName());
            pst.setString(2, passenger.getPhone());
            pst.setString(3, String.valueOf(psgId));
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            pst.close();
            con.close();
        }
    }

    public void delete(int psgId) throws SQLException {
        try {
            con = DriverManager.getConnection(url, user, pass);
            pst = con.prepareStatement("SELECT COUNT(psg_id) FROM pass_in_trip WHERE psg_id=?");
            pst.setInt(1, psgId);
            ResultSet rs = pst.executeQuery();
            rs.next();
            int count = rs.getInt("count");
            if (count > 0) {
                System.out.println("First remove the passenger from the pass_in_trip table");
            } else {
                pst = con.prepareStatement("DELETE FROM passengers WHERE psg_id=?");
                pst.setInt(1, psgId);
                int row = pst.executeUpdate();
                if (row > 0) {
                    System.out.println("Passenger deleted");
                } else {
                    System.out.println("Passenger does not exist");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            pst.close();
            con.close();
        }
    }

    public List<Passenger> getPassengersOfTrip(int tripNumber) throws SQLException {
        List<Passenger> passengerList = new ArrayList<>();
        try {
            con = DriverManager.getConnection(url, user, pass);
            st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT DISTINCT psg_id FROM pass_in_trip WHERE trip_id=" + tripNumber);
            while (rs.next()) {
                passengerList.add(getById(rs.getInt("psg_id")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            con.close();
            st.close();
        }
        return passengerList;
    }

    public void registerTrip(Trip trip, Passenger passenger, String place) throws SQLException {
        Date date = new Date(200);
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        try {
            con = DriverManager.getConnection(url, user, pass);
            pst = con.prepareStatement("insert into pass_in_trip(trip_id,psg_id,date,place)values (?,?,?,?)");
            pst.setInt(1, trip.getTripId());
            pst.setInt(2, Math.toIntExact(passenger.getId()));
            pst.setDate(3, sqlDate);
            pst.setString(4, place);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            pst.close();
            con.close();
        }

    }

    public void cancelRegistration(int tripId, int psgId) throws SQLException {
        try {
            con = DriverManager.getConnection(url, user, pass);
            pst = con.prepareStatement("DELETE FROM pass_in_trip WHERE trip_id=? AND psg_id=?");
            pst.setInt(1, tripId);
            pst.setInt(2, psgId);
            int row = pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            pst.close();
            con.close();
        }
    }

    public List<Passenger> get(int offset, int perPage, String sort) throws SQLException {
        List<Passenger> passengers = new ArrayList<>();
        try {
            con = DriverManager.getConnection(url, user, pass);
            pst = con.prepareStatement("SELECT * FROM passengers ORDER BY " + sort + " LIMIT ? OFFSET ?");
            pst.setInt(1, perPage);
            pst.setInt(2, offset);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                passengers.add(new Passenger(
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            pst.close();
            con.close();
        }
        return passengers;
    }
}