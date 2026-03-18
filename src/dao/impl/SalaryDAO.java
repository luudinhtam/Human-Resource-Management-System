package dao.impl;

import dao.interfaces.ISalaryDAO;
import entity.Salary;
import util.FileManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public final class SalaryDAO implements ISalaryDAO {

    private static final String FILE_PATH = "salary.txt";

    private final ArrayList<Salary> salaryList = new ArrayList<Salary>();
    private final FileManager fileManager;

    public SalaryDAO() throws IOException {
        this.fileManager = new FileManager(FILE_PATH);
        loadFromFile();
    }

    // ── CRUD ──────────────────────────────────────────────────────────

    @Override
    public void save(Salary salary) throws IOException {
        // Remove existing record for same employee + period (upsert)

        // Handling data loss problem that happens because of error between operations Remove and Add.
        List<Salary> backup = new ArrayList<>(salaryList);

        try {

            removeFromList(salary.getEmployeeId(), salary.getMonth(), salary.getYear());
            salaryList.add(salary);
            saveToFile();

        } catch (IOException e) {

            salaryList.clear();
            salaryList.addAll(backup);

            System.out.println("Error occurred. Data has been restored.");
            throw e;
        }
    }

    @Override
    public void deleteByPeriod(String employeeId, int month, int year) throws IOException {
        boolean removed = removeFromList(employeeId, month, year);
        if (removed == true)
            saveToFile();
    }

    // ── Queries ───────────────────────────────────────────────────────

    @Override
    public Salary findByEmployeeAndPeriod(String employeeId, int month, int year) throws IOException {
        for (Salary s : salaryList)
            if (s.getEmployeeId().equals(employeeId)
                    && s.getMonth() == month
                    && s.getYear() == year)
                return s;
        return null;
    }

    @Override
    public List<Salary> findByPeriod(int month, int year) throws IOException {
        return search(s -> s.getMonth() == month && s.getYear() == year);
    }

    @Override
    public List<Salary> findAll() throws IOException {
        return new ArrayList<Salary>(salaryList);
    }

    /**
     * Flexible search using Predicate.
     * Example: search(s -> s.getTotalSalary() > 20_000_000)
     */
    @Override
    public List<Salary> search(Predicate<Salary> predicate) throws IOException {
        List<Salary> result = new ArrayList<Salary>();
        for (Salary s : salaryList)
            if (predicate.test(s))
                result.add(s);
        return result;
    }

    // ── File I/O ──────────────────────────────────────────────────────

    @Override
    public void saveToFile() throws IOException {
        List<String> lines = new ArrayList<String>();
        for (Salary s : salaryList)
            lines.add(s.toString());
        fileManager.writeLines(lines);
    }

    private void loadFromFile() throws IOException {
        salaryList.clear();
        List<String> lines = fileManager.readLines();
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty())
                continue;
            try {
                salaryList.add(Salary.fromString(line));
            } catch (Exception e) {
                System.out.println("[WARN] Skipping invalid salary record: " + line);
            }
        }
    }

    // ── Helper ────────────────────────────────────────────────────────

    /**
     * Removes matching record from list without persisting. Returns true if
     * removed.
     */
    private boolean removeFromList(String employeeId, int month, int year) {
        for (int i = 0; i < salaryList.size(); i++) {
            Salary s = salaryList.get(i);
            if (s.getEmployeeId().equals(employeeId)
                    && s.getMonth() == month
                    && s.getYear() == year) {
                salaryList.remove(i);
                return true;
            }
        }
        return false;
    }
}
