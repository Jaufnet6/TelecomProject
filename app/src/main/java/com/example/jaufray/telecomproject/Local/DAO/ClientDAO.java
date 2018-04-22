package com.example.jaufray.telecomproject.Local.DAO;

        import android.arch.persistence.room.Dao;
        import android.arch.persistence.room.Delete;
        import android.arch.persistence.room.Insert;
        import android.arch.persistence.room.Query;
        import android.arch.persistence.room.Update;

        import com.example.jaufray.telecomproject.Model.Client;
        import com.example.jaufray.telecomproject.Model.Package;

        import java.util.List;

        import io.reactivex.Flowable;

@Dao
public interface ClientDAO {

    //Get client with specific ID
    @Query("SELECT * FROM clients WHERE idClient=:clientId")
    Flowable<Client> getClientById(int clientId);

    //Get the name of the package linked to the client
    @Query("SELECT name FROM packages WHERE idPackage=IdPackage")
    Flowable<Package> getPackageName(int IdPackage);

    //Get all clients
    @Query("SELECT * FROM clients")
    Flowable<List<Client>> getAllClients();

    @Insert
    void insertClient(Client... clients);

    @Update
    void updateClient(Client... clients);

    @Delete
    void deleteClient(Client client);



}
