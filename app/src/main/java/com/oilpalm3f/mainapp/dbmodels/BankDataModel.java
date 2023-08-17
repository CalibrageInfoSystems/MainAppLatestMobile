package com.oilpalm3f.mainapp.dbmodels;
public class BankDataModel {
    int Id;
    String bankname;
    String BranchName;
    String IFSCCode;

    public int getBankTypeId() {
        return Id;
    }

    public void setBankTypeId(int bankTypeId) {
        Id = bankTypeId;
    }

    public String getbankname() {
        return bankname;
    }

    public void setbankname(String Bankname) {
        bankname = Bankname;
    }
    public String getBranchName() {
        return BranchName;
    }

    public void setBranchName(String branchName) {
        BranchName = branchName;
    }
    public String getIFSCCode() {
        return IFSCCode;
    }

    public void setIFSCCode(String ifSCCode) {
        IFSCCode = ifSCCode;
    }
}
