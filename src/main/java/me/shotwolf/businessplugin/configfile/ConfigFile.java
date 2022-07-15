package me.shotwolf.businessplugin.configfile;

import me.shotwolf.businessplugin.Main;

public class ConfigFile {
    private Main main;

    /*
showbusinessmoney: "L'azienda $b ha $m"

businessexist: "L'azienda esiste già"

businessnotexist: "L'azienda non esiste"

nomoneyleft: "Non hai soldi sufficienti"

nomoneyleftbusiness: "Non ci sono soldi sufficienti nella tua azienda"

nopermission: "Non hai i permessi"

invalidcommand: "Comando errato"

businessdeleted: "L'azienda è stata rimossa"

businesscreated: "L'azienda è stata creata"

aziendalist: "   &aP_IVA   - &cNOME AZIENDA"

aziendalistlayout: "$v $n" #v -> vat azienda ||||||||  $n -> business name
     */
    private String useSSL;
    private String database;
    private String host;
    private int port;
    private String username;
    private String password;
    private String ShowBusinessMoney;
    private String BusinessExist;
    private String PlayerNotOnline;
    private String NotInABusiness;
    private String ArleadyEmployer;
    private String EmployerDeleted;
    private String EmployerNotInThatBusiness;
    private String PlayerNotEmployer;
    private String NotEmployer;
    private String BusinessNotExist;
    private String NoMoneyLeft;
    private String NoMoneyLeftBusiness;
    private String NoPermission;
    private String InvalidCommand;
    private String BusinessDeleted;
    private String BusinessCreated;
    private String EmployerAdd;
    private String SQLError;
    private String AziendaList;
    private String AziendaListLayout;



    private String ReceiptName;
    private String ImportLineReceipt;
    private String DescriptionLineReceipt;

    public ConfigFile(Main main){
        useSSL = main.getConfig().getString("usessl");
        host = main.getConfig().getString("host");
        database = main.getConfig().getString("database");
        password = main.getConfig().getString("password");
        port = main.getConfig().getInt("port");
        username = main.getConfig().getString("username");

        PlayerNotOnline = main.getConfig().getString("playernotonline");
        ArleadyEmployer = main.getConfig().getString("arleadyemployer");
        EmployerDeleted = main.getConfig().getString("employerdeleted");
        EmployerNotInThatBusiness = main.getConfig().getString("employernotinthatbusiness");
        NotEmployer = main.getConfig().getString("notemployer");
        EmployerAdd = main.getConfig().getString("employeradd");
        PlayerNotEmployer = main.getConfig().getString("playernotemployer");
        ReceiptName = main.getConfig().getString("receiptname");
        ImportLineReceipt = main.getConfig().getString("importlinereceipt");
        DescriptionLineReceipt = main.getConfig().getString("descriptionlinereceipt");

        ShowBusinessMoney = main.getConfig().getString("showbusinessmoney");
        BusinessExist = main.getConfig().getString("businessexist");
        BusinessNotExist = main.getConfig().getString("businessnotexist");
        NoMoneyLeft = main.getConfig().getString("nomoneyleft");
        NoMoneyLeftBusiness = main.getConfig().getString("nomoneyleftbusiness");
        NoPermission = main.getConfig().getString("nopermission");
        InvalidCommand = main.getConfig().getString("invalidcommand");
        BusinessDeleted = main.getConfig().getString("businessdeleted");
        BusinessCreated = main.getConfig().getString("businesscreated");
        SQLError = main.getConfig().getString("sqlerror");
        AziendaList = main.getConfig().getString("aziendalist");
        AziendaListLayout = main .getConfig().getString("aziendalistlayout");

    }

    public int getPort() {
        return port;
    }

    public String getDatabase() {
        return database;
    }

    public String getUseSSL() {
        return useSSL;
    }

    public String getHost() {
        return host;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public String getBusinessNotExist() {
        return BusinessNotExist;
    }

    public String getNoMoneyLeft() {
        return NoMoneyLeft;
    }

    public String getNoMoneyLeftBusiness() {
        return NoMoneyLeftBusiness;
    }

    public String getShowBusinessMoney() {
        return ShowBusinessMoney;
    }

    public String getBusinessCreated() {
        return BusinessCreated;
    }

    public String getBusinessDeleted() {
        return BusinessDeleted;
    }

    public String getBusinessExist() {
        return BusinessExist;
    }

    public String getInvalidCommand() {
        return InvalidCommand;
    }

    public String getNoPermission() {
        return NoPermission;
    }

    public String getSQLError() {
        return SQLError;
    }

    public String getAziendaList() {
        return AziendaList;
    }

    public String getAziendaListLayout() {
        return AziendaListLayout;
    }

    public String getArleadyEmployer() {
        return ArleadyEmployer;
    }

    public String getDescriptionLineReceipt() {
        return DescriptionLineReceipt;
    }

    public String getEmployerAdd() {
        return EmployerAdd;
    }

    public String getEmployerDeleted() {
        return EmployerDeleted;
    }

    public String getEmployerNotInThatBusiness() {
        return EmployerNotInThatBusiness;
    }

    public String getImportLineReceipt() {
        return ImportLineReceipt;
    }

    public String getNotEmployer() {
        return NotEmployer;
    }

    public String getNotInABusiness() {
        return NotInABusiness;
    }

    public String getPlayerNotEmployer() {
        return PlayerNotEmployer;
    }

    public String getPlayerNotOnline() {
        return PlayerNotOnline;
    }

    public String getReceiptName() {
        return ReceiptName;
    }
}
