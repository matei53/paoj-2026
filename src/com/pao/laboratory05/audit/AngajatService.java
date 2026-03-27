package com.pao.laboratory05.audit;

import java.time.LocalDateTime;
import java.util.Arrays;

public class AngajatService {
    private Angajat[] angajati;
    private AuditEntry[] auditLog;

    private AngajatService() {
        this.angajati = new Angajat[0];
        this.auditLog = new AuditEntry[0];
    }

    private static class Holder {
        private static final AngajatService INSTANCE = new AngajatService();
    }

    private void logAction(String action, String target) {
        AuditEntry[] added = new AuditEntry[auditLog.length + 1];
        System.arraycopy(auditLog, 0, added, 0, auditLog.length);
        added[added.length - 1] = new AuditEntry(action, target, LocalDateTime.now().toString());
        auditLog = added;
    }

    public static AngajatService getInstance() {
        return Holder.INSTANCE;
    }

    void addAngajat(Angajat a) {
        Angajat[] added = new Angajat[angajati.length + 1];
        System.arraycopy(angajati, 0, added, 0, angajati.length);
        added[added.length - 1] = a;
        angajati = added;
        logAction("ADD", a.getNume());
    }

    void printAll() {
        for (Angajat a : angajati)
            System.out.println(a);
    }

    void listBySalary() {
        Angajat[] copy = angajati;
        Arrays.sort(copy);
        for (Angajat a : copy){
            System.out.println(a);
        }
    }

    void findByDepartament(String numeDept) {
        logAction("FIND_BY_DEPT", numeDept);
        int ok = 0;
        for (Angajat a : angajati){
            if (a.getDepartament().nume().equalsIgnoreCase(numeDept))
                System.out.println(a);
            ok = 1;
        }
        if (ok == 0)
            System.out.println("Niciun angajat în departamentul: " + numeDept);
    }

    void printAuditLog() {
        for (AuditEntry a : auditLog)
            System.out.println(a);
    }
}
