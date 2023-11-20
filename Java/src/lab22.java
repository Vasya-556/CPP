import java.util.*;

class CPUQueue {
    private Queue<CPUProcess> queue = new LinkedList<>();
    private int maxSize;

    public CPUQueue(int maxSize) {
        this.maxSize = maxSize;
    }

    public void enqueue(CPUProcess process) {
        if (queue.size() < maxSize) {
            queue.add(process);
        } else {
            System.out.println("Queue is full. Unable to enqueue process.");
        }
    }

    public CPUProcess dequeue() {
        return queue.poll();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

class CPUProcess {
    private int generationInterval;
    private int serviceTime;

    public CPUProcess(int generationInterval, int serviceTime) {
        this.generationInterval = generationInterval;
        this.serviceTime = serviceTime;
    }

    public int getGenerationInterval() {
        return generationInterval;
    }

    public int getServiceTime() {
        return serviceTime;
    }
}

class CPU {
    private int processCount;
    private int maxQueueLength;
    private Map<Integer, Integer> processorsUsage;

    public CPU(int processCount, int lowerBound, int upperBound) {
        this.processCount = 0;
        this.maxQueueLength = 0;
        this.processorsUsage = new HashMap<>();

        CPUQueue[] queues = new CPUQueue[processCount];
        for (int i = 0; i < processCount; i++) {
            queues[i] = new CPUQueue(i + 1);
        }

        Random random = new Random();
        int currentTime = 0;

        while (true) {
            for (int i = 0; i < processCount; i++) {
                if (random.nextDouble() < 0.5) {
                    int generationInterval = random.nextInt(upperBound - lowerBound + 1) + lowerBound;
                    queues[i].enqueue(new CPUProcess(generationInterval, random.nextInt(upperBound - lowerBound + 1) + lowerBound));
                }
            }

            for (int i = 0; i < processCount; i++) {
                if (!queues[i].isEmpty()) {
                    processorsUsage.put(i + 1, processorsUsage.getOrDefault(i + 1, 0) + 1);
                    CPUProcess currentProcess = queues[i].dequeue();
                    currentTime += currentProcess.getServiceTime();

                    if (i == processCount - 1) {
                        maxQueueLength = Math.max(maxQueueLength, queues[i].isEmpty() ? 0 : queues[i].dequeue().getGenerationInterval());
                    } else {
                        queues[i + 1].enqueue(new CPUProcess(currentTime + currentProcess.getGenerationInterval(), currentProcess.getServiceTime()));
                    }
                }
            }

            if (processorsUsage.size() == processCount) {
                break;
            }
        }
        this.processCount = currentTime;
    }

    public int getProcessCount() {
        return processCount;
    }

    public int getMaxQueueLength() {
        return maxQueueLength;
    }

    public Map<Integer, Integer> getProcessorsUsage() {
        return processorsUsage;
    }
}
public class lab22 {
    public static void main(String[] args) {
        int processCount = 3;
        int lowerBound = 1;
        int upperBound = 10;

        CPU cpu = new CPU(processCount, lowerBound, upperBound);

        System.out.println("Number of processors: " + cpu.getProcessCount());
        System.out.println("Maximum queue length: " + cpu.getMaxQueueLength());
        System.out.println("Processor usage:");

        for (Map.Entry<Integer, Integer> processor : cpu.getProcessorsUsage().entrySet()) {
            double percentage = (double) processor.getValue() / cpu.getProcessCount() * 100;
            System.out.printf("Processor %d: %d processes (%.2f%%)%n", processor.getKey(), processor.getValue(), percentage);
        }
    }
}
