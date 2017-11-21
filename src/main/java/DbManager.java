import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;


public class DbManager {

    private static Connection connection;



    public static void initialize() {

        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:nvddb.db");
        } catch (ClassNotFoundException notFound) {
            System.err.println("Database driver not found");
        } catch (SQLException sql) {
            System.err.println("Database connection error");
        }

    }

    public static void load() {

        try {
            ResultSet resultSet = connection.createStatement().executeQuery("select * from items");

            while(resultSet.next()) {
                Item item = new Item();
                item.setItemId( resultSet.getInt("id") );
                item.setTitle( resultSet.getString("title") );
                item.setUrl( resultSet.getString("url") );
                item.setLocation( resultSet.getString("location") );
                item.setCustomName( resultSet.getString("customName") );
                item.setSpeedLimit( resultSet.getInt("speedLimit") );
                item.setActionAfterFinish( resultSet.getString("actionAfterFinish") );
                item.setAddToQueue( resultSet.getBoolean("addToQueue") );
                item.setIsVideo( resultSet.getBoolean("isVideo") );
                item.setFormat( resultSet.getString("format") );
                item.setVideoQuality( resultSet.getInt("videoQuality") );
                item.setAudioQuality( resultSet.getInt("audioQuality") );
                item.setSubtitleLanguage( resultSet.getString("subtitleLanguage") );
                item.setEmbeddedSubtitle( resultSet.getBoolean("embeddedSubtitle") );
                item.setAutoGeneratedSubtitle( resultSet.getBoolean("autoGeneratedSubtitle") );
                item.setIsPlaylist( resultSet.getBoolean("isPlaylist") );
                item.setStartIndex( resultSet.getInt("startIndex") );
                item.setEndIndex( resultSet.getInt("endIndex") );
                item.setItems( resultSet.getString("items") );
                item.setAllItems( resultSet.getBoolean("allItems") );
                item.setDone( resultSet.getFloat("done") );
                item.setSizeUnit( resultSet.getString("sizeUnit") );
                item.setSize( resultSet.getFloat("size") );
                item.setStatus("Stopped");

                HomeController.getItemList().add(item);
                if(item.isAddToQueue())
                    HomeController.getQueueItemList().add(item);
            }

        } catch (SQLException e) {
            System.out.println("Error loading database");
            e.printStackTrace();
        }

    }

    public static void insert(Item item) {

        try {
            String sqlCommand = String.format("INSERT INTO items (id,url,location,title,customName,speedLimit,actionAfterFinish," +
                            "addToQueue,isVideo,format,videoQuality,audioQuality,subtitleLanguage,embeddedSubtitle," +
                            "autoGeneratedSubtitle,isPlaylist,startIndex,endIndex,items,allItems,done,size,sizeUnit) " +
                            "VALUES (%d, '%s', '%s', '%s', '%s', %d, '%s', '%b', '%b', '%s', %d, %d, '%s', '%b', '%b', " +
                            " '%b', %d, %d, '%s', '%b', %f, %f, '%s') ",
                    item.getItemId(), item.getUrl(), item.getLocation(), item.getTitle(), item.getCustomName(), item.getSpeedLimit(),
                    item.getActionAfterFinish(), item.isAddToQueue(), item.getIsVideo(), item.getFormat(), item.getVideoQuality(),
                    item.getAudioQuality(), item.getSubtitleLanguage(), item.isEmbeddedSubtitle(), item.isAutoGeneratedSubtitle(),
                    item.getIsPlaylist(), item.getStartIndex(), item.getEndIndex(), item.getItems(), item.isAllItems(),
                    item.getDone(), item.getSize(), item.getSizeUnit());

            connection.createStatement().executeUpdate(sqlCommand);
        } catch (SQLException e) {
            System.out.println("Error inserting item");
            e.printStackTrace();
        }

    }

    public static void delete(Item item) {

        try {
            String sqlCommand = String.format("DELETE FROM items WHERE id = %d", item.getItemId());
            connection.createStatement().executeUpdate(sqlCommand);
        } catch (SQLException e) {
            System.out.println("Error deleting item");
        }

    }

    public static void updateFloat(Item item, String columnLabel, float value) {

        try {
            String sqlCommand = String.format("UPDATE items SET %s = %f WHERE id = %d", columnLabel, value, item.getItemId());
            connection.createStatement().executeUpdate(sqlCommand);
        } catch (SQLException e) {
            System.out.println("Error updating done");
            e.printStackTrace();
        }

    }

    public static void updateString(Item item, String columnLabel, String value) {

        try {
            String sqlCommand = String.format("UPDATE items SET %s = '%s' WHERE id = %d", columnLabel, value, item.getItemId());
            connection.createStatement().executeUpdate(sqlCommand);
        } catch (SQLException e) {
            System.out.println("Error updating title");
            e.printStackTrace();
        }

    }

    public static int getNextId() {

        try {
            ResultSet resultSet = connection.createStatement().executeQuery("SELECT COUNT(*) FROM items");
            return resultSet.getInt(1);
        } catch (SQLException e) {
            System.out.println("Error getting last row id");
            return 0;
        }

    }

    public static void close() {

        try {
            if(! connection.isClosed())
                connection.close();
        } catch (SQLException e) {
            System.out.println("Error closing connection");
        }
    }


}
