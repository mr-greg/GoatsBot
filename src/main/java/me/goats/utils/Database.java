package me.goats.utils;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database
{
    Dotenv dotenv = Dotenv.load();
    Connection connection;
    Statement statement;

    public class Raids {
        public final String id;
        public final String idLeader;
        public final String idRole;
        public final String idChannel;
        public final String idretrivermsg;
        public final String raidName;
        public final String nbPlace;
        public final String nbRaid;
        public final String dateTime;
        public final String aFournir;
        public final String autre;
        public final String idTeamChannel;
        public final String idRegisterChannel;
        public final String idSelectionChannel;

        public Raids(String id, String idLeader, String idRole, String idChannel, String idretrivermsg, String raidName, String nbPlace, String nbRaid, String dateTime, String aFournir, String autre, String idTeamChannel, String idRegisterChannel, String idSelectionChannel) {
            this.id = id;
            this.idLeader = idLeader;
            this.idRole = idRole;
            this.idChannel = idChannel;
            this.idretrivermsg = idretrivermsg;
            this.raidName = raidName;
            this.nbPlace = nbPlace;
            this.nbRaid = nbRaid;
            this.dateTime = dateTime;
            this.aFournir = aFournir;
            this.autre = autre;
            this.idTeamChannel = idTeamChannel;
            this.idRegisterChannel = idRegisterChannel;
            this.idSelectionChannel = idSelectionChannel;
        }
    }

    public class ParticipationRequest {
        public final String raidId;
        public final String raidName;
        public final String leaderId;
        public final String pseudo;
        public final String classeNiveau;
        public final String sp;
        public final String mule;
        public final String autre;
        public final String selectionChannelId;
        public final String msgAcceptId;
        public final String memberId;

        public ParticipationRequest(String raidId, String raidName, String leaderId, String pseudo, String classeNiveau, String sp, String mule, String autre, String selectionChannelId, String msgAcceptId, String memberId) {
            this.raidId = raidId;
            this.raidName = raidName;
            this.leaderId = leaderId;
            this.pseudo = pseudo;
            this.classeNiveau = classeNiveau;
            this.sp = sp;
            this.mule = mule;
            this.autre = autre;
            this.selectionChannelId = selectionChannelId;
            this.msgAcceptId = msgAcceptId;
            this.memberId = memberId;
        }
    }

    public class VerifyParticipationRequest {
        public final String raidId;
        public final String raidName;
        public final String leaderId;
        public final String pseudo;
        public final String classeNiveau;
        public final String sp;
        public final String mule;
        public final String autre;
        public final String selectionChannelId;
        public final String memberId;


        public VerifyParticipationRequest(String raidId, String raidName, String leaderId, String pseudo, String classeNiveau, String sp, String mule, String autre, String selectionChannelId, String memberId) {
            this.raidId = raidId;
            this.raidName = raidName;
            this.leaderId = leaderId;
            this.pseudo = pseudo;
            this.classeNiveau = classeNiveau;
            this.sp = sp;
            this.mule = mule;
            this.autre = autre;
            this.selectionChannelId = selectionChannelId;
            this.memberId = memberId;
        }
    }

    public class Participants {
        public final String idLeader;
        public final String idParticipant;
        public final String idSelectionChannel;
        public final String idRaid;

        public Participants(String idLeader, String idParticipant, String idSelectionChannel, String idRaid) {
            this.idLeader = idLeader;
            this.idParticipant = idParticipant;
            this.idSelectionChannel = idSelectionChannel;
            this.idRaid = idRaid;
        }
    }

    public void dbConnect(String urlDB) {

        try
        {
            // create a database connection
            connection = DriverManager.getConnection(urlDB);
            statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            System.out.println("Connection to SQLite worked. Yayy");
        }
        catch(SQLException e)
        {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }

    }

    private Connection connect() {
        String url = dotenv.get("URLDB");
        Connection conn = null;

        try
        {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public void createNewTable(String urlDB, String reqSQL) {

        String sql = reqSQL;

        try
        {
            connection = DriverManager.getConnection(urlDB);
            statement = connection.createStatement();
            statement.execute(sql);
            System.out.println("Table creation worked !");

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.out.println("Table creation failed :(");

        }

    }

    public void createTableRole(String urlDB, String reqSQL) {

        String sql = reqSQL;

        try
        {
            connection = DriverManager.getConnection(urlDB);
            statement = connection.createStatement();
            statement.execute(sql);
            System.out.println("table role has been created");

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.out.println("Table creation role failed :(");
        }
    }

    public void roleSetup(String reqSQL, String guildID) {
        String sql = reqSQL;

        try (Connection conn = this.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setString(1, guildID);
            pstmt.setInt(2, 1);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateEmbedID(String sql, int id, String reactionRoleMsgID) {
        try (Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        ){
            pstmt.setString(1, reactionRoleMsgID);
            pstmt.setInt(2, id);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public Integer checkSetupRole(String sql) {
        String url = dotenv.get("URLDB");
        Integer result = null;
        try
        {
            Connection connection = DriverManager.getConnection(url);
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                result = rs.getInt("roleSetup");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public String selectEmbed(String sql){
        String url = dotenv.get("URLDB");
        String result = null;
        try
        {
            Connection connection = DriverManager.getConnection(url);
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                result = rs.getString("reactionRoleMsgID");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    // ---------------------------------------------------
    //                  RAIDS

    public void createTableRaids(String urlDB, String reqSQL) {

        String sql = reqSQL;

        try
        {
            connection = DriverManager.getConnection(urlDB);
            statement = connection.createStatement();
            statement.execute(sql);
            System.out.println("table raids has been created");

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.out.println("Table creation raids failed :(");
        }
    }

    public void createTableParticipant(String urlDB, String reqSQL) {

        String sql = reqSQL;

        try
        {
            connection = DriverManager.getConnection(urlDB);
            statement = connection.createStatement();
            statement.execute(sql);
            System.out.println("table participant has been created");

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.out.println("Table creation participant failed :(");
        }
    }

    public void updateRaids(String sql, String idLeader, String idRole, String idChannel, String idretrievemsg, String raidName, String nbPlace, String nbRaid, String dateHeure, String aFournir, String autre, String idTeamChannel, String idRegisterChannel, String idSelectionChannel) {
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)
        ){
            pstmt.setString(1, idLeader);
            pstmt.setString(2, idRole);
            pstmt.setString(3, idChannel);
            pstmt.setString(4, idretrievemsg);
            pstmt.setString(5, raidName);
            pstmt.setString(6, nbPlace);
            pstmt.setString(7, nbRaid);
            pstmt.setString(8, dateHeure);
            pstmt.setString(9, aFournir);
            pstmt.setString(10, autre);
            pstmt.setString(11, idTeamChannel);
            pstmt.setString(12, idRegisterChannel);
            pstmt.setString(13, idSelectionChannel);



            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public void createTableDataSelect(String urlDB, String reqSQL) {

        try
        {
            connection = DriverManager.getConnection(urlDB);
            statement = connection.createStatement();
            statement.execute(reqSQL);
            System.out.println("table dataSelect has been created");

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.out.println("Table creation dataSelect failed :(");
        }
    }

    public void updateSelectData(String sql, String idLeader, String idretrievemsg, String raidName) {
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)
        ){
            pstmt.setString(1, idLeader);
            pstmt.setString(2, idretrievemsg);
            pstmt.setString(3, raidName);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public String idretrievermsgFromDataSelect(String idleader){
        String sql = "SELECT idretrievermsg FROM dataSelect WHERE idLeader = ?";
        String result = null;
        try (Connection conn = this.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setString(1, idleader);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                result = rs.getString("idretrievermsg");
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return result;

    }

    public String raidNameFromDataSelect(String idLeader){
        String sql = "SELECT raidName FROM dataSelect WHERE idLeader = ?";
        String result = null;
        try (Connection conn = this.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setString(1,idLeader);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                result = rs.getString("raidName");
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return result;
    }



    public List<Raids> selectAllRaids(String idretrievermsg)
        throws SQLException {
            try (Connection conn = connect();
                PreparedStatement stmt = conn.prepareStatement("select * from raids where idretrievermsg = ?")
            ) {
                stmt.setString(1, idretrievermsg);
                ResultSet rs = stmt.executeQuery();

                List<Raids> raidsList = new ArrayList<>();


                while (rs.next()) {
                    String raidsQueryid = rs.getString("id");
                    String raidsQueryidLeader = rs.getString("idLeader");
                    String raidsQueryidRole = rs.getString("idRole");
                    String raidsQueryidChannel = rs.getString("idChannel");
                    String raidsQueryidretrivermsg = rs.getString("idretrievermsg");
                    String raidsQueryraidName = rs.getString("raidName");
                    String raidsQuerynbPlace = rs.getString("nbPlace");
                    String raidsQuerynbRaid = rs.getString("nbRaid");
                    String dateTime = rs.getString("dateTime");
                    String aFournir = rs.getString("aFournir");
                    String autre = rs.getString("autre");
                    String raidsQueryidTeamChannel = rs.getString("idTeamChannel");
                    String raidsQueryidRegisterChannel = rs.getString("idRegisterChannel");
                    String raidsQueryidSelectionChannel = rs.getString("idSelectionChannel");

                    Raids raidsQueries = new Raids(raidsQueryid, raidsQueryidLeader, raidsQueryidRole, raidsQueryidChannel, raidsQueryidretrivermsg, raidsQueryraidName, raidsQuerynbPlace, raidsQuerynbRaid, dateTime, aFournir, autre, raidsQueryidTeamChannel, raidsQueryidRegisterChannel, raidsQueryidSelectionChannel);

                    raidsList.add(raidsQueries);
                }

                return raidsList;
            }
        }

    public List<Raids> selectAllRaidsByRaidId(String raidId)
            throws SQLException {
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement("select * from raids where id = ?")
        ) {
            stmt.setString(1, raidId);
            ResultSet rs = stmt.executeQuery();

            List<Raids> raidsList = new ArrayList<>();


            while (rs.next()) {
                String raidsQueryid = rs.getString("id");
                String raidsQueryidLeader = rs.getString("idLeader");
                String raidsQueryidRole = rs.getString("idRole");
                String raidsQueryidChannel = rs.getString("idChannel");
                String raidsQueryidretrivermsg = rs.getString("idretrievermsg");
                String raidsQueryraidName = rs.getString("raidName");
                String raidsQuerynbPlace = rs.getString("nbPlace");
                String raidsQuerynbRaid = rs.getString("nbRaid");
                String dateTime = rs.getString("dateTime");
                String aFournir = rs.getString("aFournir");
                String autre = rs.getString("autre");
                String raidsQueryidTeamChannel = rs.getString("idTeamChannel");
                String raidsQueryidRegisterChannel = rs.getString("idRegisterChannel");
                String raidsQueryidSelectionChannel = rs.getString("idSelectionChannel");

                Raids raidsQueries = new Raids(raidsQueryid, raidsQueryidLeader, raidsQueryidRole, raidsQueryidChannel, raidsQueryidretrivermsg, raidsQueryraidName, raidsQuerynbPlace, raidsQuerynbRaid, dateTime, aFournir, autre, raidsQueryidTeamChannel, raidsQueryidRegisterChannel, raidsQueryidSelectionChannel);

                raidsList.add(raidsQueries);
            }

            return raidsList;
        }
    }

    public List<Raids> selectAllRaidsById(String idSelectChannel)
        throws SQLException {
        try (Connection conn = connect();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM raids WHERE idSelectionChannel = ?")
        ) {
            stmt.setString(1, idSelectChannel);
            ResultSet rs = stmt.executeQuery();

            List<Raids> raidsList = new ArrayList<>();

            while (rs.next()){
                String raidsQueryid = rs.getString("id");
                String raidsQueryidLeader = rs.getString("idLeader");
                String raidsQueryidRole = rs.getString("idRole");
                String raidsQueryidChannel = rs.getString("idChannel");
                String raidsQueryidretrivermsg = rs.getString("idretrievermsg");
                String raidsQueryraidName = rs.getString("raidName");
                String raidsQuerynbPlace = rs.getString("nbPlace");
                String raidsQuerynbRaid = rs.getString("nbRaid");
                String dateTime = rs.getString("dateTime");
                String aFournir = rs.getString("aFournir");
                String autre = rs.getString("autre");
                String raidsQueryidTeamChannel = rs.getString("idTeamChannel");
                String raidsQueryidRegisterChannel = rs.getString("idRegisterChannel");
                String raidsQueryidSelectionChannel = rs.getString("idSelectionChannel");

                Raids raidsQueries = new Raids(raidsQueryid, raidsQueryidLeader, raidsQueryidRole, raidsQueryidChannel, raidsQueryidretrivermsg, raidsQueryraidName, raidsQuerynbPlace, raidsQuerynbRaid, dateTime, aFournir, autre, raidsQueryidTeamChannel, raidsQueryidRegisterChannel, raidsQueryidSelectionChannel);

                raidsList.add(raidsQueries);
            }
            return raidsList;
        }
    }


    public void createTableParticipationRequest(String reqSQL) {
        try
        {
            connection = DriverManager.getConnection(dotenv.get("URLDB"));
            statement = connection.createStatement();
            statement.execute(reqSQL);
            System.out.println("table ParticipationRequest has been created");

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.out.println("Table ParticipationRequest creation failed :(");
        }
    }

    public void updateTableParticipationRequest(String reqSQL, String raidId, String raidName, String leaderId, String pseudo, String classeNiveau, String sp, String mule, String autre,String selectionChannelId, String msgAcceptId, String memberId) {

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(reqSQL)
        ){
            pstmt.setString(1, raidId);
            pstmt.setString(2, raidName);
            pstmt.setString(3, leaderId);
            pstmt.setString(4, pseudo);
            pstmt.setString(5, classeNiveau);
            pstmt.setString(6, sp);
            pstmt.setString(7, mule);
            pstmt.setString(8, autre);
            pstmt.setString(9, selectionChannelId);
            pstmt.setString(10, msgAcceptId);
            pstmt.setString(11, memberId);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<ParticipationRequest> selectAllParticipationsRequest(String selectionChannelId, String msgAcceptId)
            throws SQLException {
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement("select * from participationRequest where selectionChannelId = ? AND msgAcceptId = ?")
        ) {
            stmt.setString(1, selectionChannelId);
            stmt.setString(2,msgAcceptId);
            ResultSet rs = stmt.executeQuery();

            List<ParticipationRequest> participationList = new ArrayList<>();

            while (rs.next()) {
                String raidId = rs.getString("raidId");
                String raidName = rs.getString("raidName");
                String leaderId = rs.getString("leaderId");
                String pseudo = rs.getString("pseudo");
                String classeNiveau = rs.getString("classeNiveau");
                String sp = rs.getString("sp");
                String mule = rs.getString("mule");
                String autre = rs.getString("autre");
                String selectChannelId = rs.getString("selectionChannelId");
                String messageAcceptId = rs.getString("msgAcceptId");
                String memberId = rs.getString("memberId");

                ParticipationRequest participationQueries = new ParticipationRequest(raidId, raidName, leaderId, pseudo, classeNiveau, sp, mule, autre, selectChannelId,messageAcceptId, memberId);

                participationList.add(participationQueries);
            }

            return participationList;
        }
    }

    public List<VerifyParticipationRequest> ParticipationRequestCheck(String selectionChannelId)
            throws SQLException {
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement("select * from participationRequest where selectionChannelId = ?")
        ) {
            stmt.setString(1, selectionChannelId);
            ResultSet rs = stmt.executeQuery();

            List<VerifyParticipationRequest> participationList = new ArrayList<>();

            while (rs.next()) {
                String raidId = rs.getString("raidId");
                String raidName = rs.getString("raidName");
                String leaderId = rs.getString("leaderId");
                String pseudo = rs.getString("pseudo");
                String classeNiveau = rs.getString("classeNiveau");
                String sp = rs.getString("sp");
                String mule = rs.getString("mule");
                String autre = rs.getString("autre");
                String selectChannelId = rs.getString("selectionChannelId");
                String memberId = rs.getString("memberId");

                VerifyParticipationRequest participationQueries = new VerifyParticipationRequest(raidId, raidName, leaderId, pseudo, classeNiveau, sp, mule, autre, selectChannelId, memberId);

                participationList.add(participationQueries);
            }

            return participationList;
        }
    }

    public void updateTableParticipant(String reqSQL, String idLeader, String idParticipant, String idSelectionChannel, String idRaid) {

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(reqSQL)
        ){
            pstmt.setString(1, idLeader);
            pstmt.setString(2, idParticipant);
            pstmt.setString(3, idSelectionChannel);
            pstmt.setString(4, idRaid);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteDataSelect(String sql, String idLeader){
        try (Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setString(1, idLeader);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteParticipationRequest(String sql, String raidId, String memberId){
        try (Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, raidId);
            pstmt.setString(2, memberId);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteRaidFromPartReq(String sql, String raidId){
        try (Connection conn = this.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setString(1,raidId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteParticipant(String sql, String raidId, String memberId){
        try (Connection conn = this.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setString(1,raidId);
            pstmt.setString(2,memberId);

            pstmt.executeUpdate();

        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void deleteRaidFromPart(String sql, String raidId) {
        try (Connection conn = this.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setString(1,raidId);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteRaid(String sql, String raidId) {
        try (Connection conn = this.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setString(1,raidId);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Participants> selectAllParticipants(String idRaid)
            throws SQLException {
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement("select * from participant where idRaid = ?")
        ) {
            stmt.setString(1, idRaid);
            ResultSet rs = stmt.executeQuery();

            List<Participants> participantsList = new ArrayList<>();


            while (rs.next()) {

                String idLeader = rs.getString("idLeader");
                String idParticipant = rs.getString("idParticipant");
                String idSelectionChannel = rs.getString("idSelectionChannel");
                String raidId = rs.getString("idRaid");

                Participants participantsQueries = new Participants(idLeader, idParticipant, idSelectionChannel, raidId);

                participantsList.add(participantsQueries);
            }

            return participantsList;
        }
    }

    public String SelectMsgAcceptIdFromPartReq(String raidId, String memberId){
        String sql = "SELECT msgAcceptId from participationRequest WHERE raidId = ? AND memberId = ?";

        String result = null;
        try (Connection conn = this.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, raidId);
            pstmt.setString(2, memberId);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()){
                result = rs.getString("msgAcceptId");
            }

        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return result;
    }

    public void updateRaidsSelectionChannel(String reqSQL, String idSelectionChannel, String idRaid) {

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(reqSQL)
        ){
            pstmt.setString(1, idSelectionChannel);
            pstmt.setString(2, idRaid);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}