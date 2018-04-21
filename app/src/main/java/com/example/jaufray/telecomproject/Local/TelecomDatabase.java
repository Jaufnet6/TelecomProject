package com.example.jaufray.telecomproject.Local;

        import android.arch.persistence.room.Database;
        import android.arch.persistence.room.Room;
        import android.arch.persistence.room.RoomDatabase;
        import android.content.Context;

        import com.example.jaufray.telecomproject.Local.DAO.ClientDAO;
        import com.example.jaufray.telecomproject.Local.DAO.PackageDAO;
        import com.example.jaufray.telecomproject.Local.DAO.ServiceDAO;
        import com.example.jaufray.telecomproject.Model.Client;
        import com.example.jaufray.telecomproject.Model.Package;
        import com.example.jaufray.telecomproject.Model.Service;

        import static com.example.jaufray.telecomproject.Local.TelecomDatabase.DATABASE_VERSION;

@Database(entities = {Client.class, Service.class, Package.class}, version = DATABASE_VERSION)
public abstract class TelecomDatabase extends RoomDatabase{

    public static final int DATABASE_VERSION=1;
    public static final String DATABASE_NAME="TELECOM-Database";

    public abstract ClientDAO clientDAO();
    public abstract ServiceDAO serviceDAO();
    public abstract PackageDAO packageDAO();

    private static TelecomDatabase mInstance;

    public synchronized static TelecomDatabase getInstance(Context context){

        if(mInstance == null){
            mInstance = Room.databaseBuilder(context, TelecomDatabase.class, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();

        }

        return mInstance;
    }

}
