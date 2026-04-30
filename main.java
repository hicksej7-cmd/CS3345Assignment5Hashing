import java.io.*;
import java.util.*;

class Employee {
    int empNum;
    double hourlyRate;

    public Employee(int empNum, double hourlyRate) {
        this.empNum = empNum;
        this.hourlyRate = hourlyRate;
    }

    @Override
    public String toString() {
        return "Employee Number: " + empNum + ", Hourly Rate: " + hourlyRate;
    }
}

class EmployeeHeap {
    private List<Employee> heap;

    public EmployeeHeap() {
        heap = new ArrayList<>();
    }

    private int parent(int i) {
        return (i - 1) / 2;
    }

    private int left(int i) {
        return 2 * i + 1;
    }

    private int right(int i) {
        return 2 * i + 2;
    }

    private void swap(int i, int j) {
        Employee temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }

    private void heapifyUp(int i) {
        while (i > 0 && heap.get(i).empNum > heap.get(parent(i)).empNum) {
            swap(i, parent(i));
            i = parent(i);
        }
    }

    private void heapifyDown(int i, int size) {
        int largest = i;
        int l = left(i);
        int r = right(i);
        if (l < size && heap.get(l).empNum > heap.get(largest).empNum) largest = l;
        if (r < size && heap.get(r).empNum > heap.get(largest).empNum) largest = r;
        if (largest != i) {
            swap(i, largest);
            heapifyDown(largest, size);
        }
    }

    public void insert(Employee e) {
        heap.add(e);
        heapifyUp(heap.size() - 1);
    }

    public Employee removeMax() {
        if (heap.isEmpty()) return null;
        Employee max = heap.get(0);
        heap.set(0, heap.get(heap.size() - 1));
        heap.remove(heap.size() - 1);
        if (!heap.isEmpty()) heapifyDown(0, heap.size());
        return max;
    }

    public boolean delete(int empNum) {
        for (int i = 0; i < heap.size(); i++) {
            if (heap.get(i).empNum == empNum) {
                heap.set(i, heap.get(heap.size() - 1));
                heap.remove(heap.size() - 1);
                if (i < heap.size()) {
                    heapifyDown(i, heap.size());
                    heapifyUp(i);
                }
                return true;
            }
        }
        return false;
    }

    public void heapSort() {
        int n = heap.size();
        // Build max heap
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapifyDown(i, n);
        }
        // Extract elements
        for (int i = n - 1; i > 0; i--) {
            swap(0, i);
            heapifyDown(0, i);
        }
    }

    public List<Employee> getHeap() {
        return heap;
    }
}

public class main {
    public static void main(String[] args) {
        EmployeeHeap heap = new EmployeeHeap();
        try {
            Scanner fileScanner = new Scanner(new File("records.txt"));
            while (fileScanner.hasNextInt()) {
                int empNum = fileScanner.nextInt();
                double rate = fileScanner.nextDouble();
                heap.insert(new Employee(empNum, rate));
            }
            fileScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("records.txt not found.");
            return;
        }

        Scanner input = new Scanner(System.in);
        while (true) {
            System.out.println("Enter operation: insert, delete, or done");
            String op = input.next();
            if (op.equals("insert")) {
                System.out.println("Enter employee number:");
                int num = input.nextInt();
                System.out.println("Enter hourly rate:");
                double rate = input.nextDouble();
                heap.insert(new Employee(num, rate));
            } else if (op.equals("delete")) {
                System.out.println("Enter employee number to delete:");
                int num = input.nextInt();
                if (!heap.delete(num)) {
                    System.out.println("Employee not found.");
                }
            } else if (op.equals("done")) {
                break;
            } else {
                System.out.println("Invalid operation.");
            }
        }

        heap.heapSort();
        System.out.println("Sorted list:");
        for (Employee e : heap.getHeap()) {
            System.out.println(e);
        }
        input.close();
    }
}
