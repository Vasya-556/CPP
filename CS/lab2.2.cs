using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CS
{
	class CPUQueue
	{
		private Queue<CPUProcess> queue = new Queue<CPUProcess>();
		private int maxSize;

		public CPUQueue(int maxSize)
		{
			this.maxSize = maxSize;
		}

		public void Enqueue(CPUProcess process)
		{
			if (queue.Count < maxSize)
			{
				queue.Enqueue(process);
			}
			else
			{
				Console.WriteLine("Queue is full. Unable to enqueue process.");
			}
		}

		public CPUProcess Dequeue()
		{
			return queue.Dequeue();
		}

		public bool IsEmpty()
		{
			return queue.Count == 0;
		}
	}

	class CPUProcess
	{
		public int GenerationInterval { get; set; }
		public int ServiceTime { get; set; }

		public CPUProcess(int generationInterval, int serviceTime)
		{
			GenerationInterval = generationInterval;
			ServiceTime = serviceTime;
		}
	}

	class CPU
	{
		public int ProcessCount { get; private set; }
		public int MaxQueueLength { get; private set; }
		public Dictionary<int, int> ProcessorsUsage { get; private set; }

		public CPU(int processCount, int lowerBound, int upperBound)
		{
			ProcessorsUsage = new Dictionary<int, int>();

			CPUQueue[] queues = new CPUQueue[processCount];
			for (int i = 0; i < processCount; i++)
			{
				queues[i] = new CPUQueue(i + 1);
			}

			Random random = new Random();
			int currentTime = 0;

			while (true)
			{
				for (int i = 0; i < processCount; i++)
				{
					if (random.NextDouble() < 0.5)
					{
						int generationInterval = random.Next(lowerBound, upperBound + 1);
						queues[i].Enqueue(new CPUProcess(generationInterval, random.Next(lowerBound, upperBound + 1)));
					}
				}

				for (int i = 0; i < processCount; i++)
				{
					if (!queues[i].IsEmpty())
					{
						ProcessorsUsage[i + 1] = ProcessorsUsage.TryGetValue(i + 1, out int count) ? count + 1 : 1;
						CPUProcess currentProcess = queues[i].Dequeue();
						currentTime += currentProcess.ServiceTime;

						if (i == processCount - 1)
						{
							MaxQueueLength = Math.Max(MaxQueueLength, queues[i].IsEmpty() ? 0 : queues[i].Dequeue().GenerationInterval);
						}
						else
						{
							queues[i + 1].Enqueue(new CPUProcess(currentTime + currentProcess.GenerationInterval, currentProcess.ServiceTime));
						}
					}
				}

				if (ProcessorsUsage.Count == processCount)
				{
					break;
				}
			}

			ProcessCount = currentTime;
		}
	}
	class lab22
    {
		static void Main(string[] args)
		{
			int processCount = 3;
			int lowerBound = 1;
			int upperBound = 10;

			CPU cpu = new CPU(processCount, lowerBound, upperBound);

			Console.WriteLine($"Number of processors: {cpu.ProcessCount}");
			Console.WriteLine($"Maximum queue length: {cpu.MaxQueueLength}");
			Console.WriteLine("Processor usage:");

			foreach (var processor in cpu.ProcessorsUsage)
			{
				double percentage = (double)processor.Value / cpu.ProcessCount * 100;
				Console.WriteLine($"Processor {processor.Key}: {processor.Value} processes ({percentage:F2}%)");
			}
			Console.ReadKey();
		}
	}
}
