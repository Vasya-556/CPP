using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.Net;
using System.Net.Sockets;
using System.Runtime.Serialization.Formatters.Binary;
using System.IO;
using System.Threading;

namespace CS3
{
    public partial class Server : Form
    {
        public Server()
        {
            InitializeComponent();
            StartServerAsync();
        }
        private Socket serverSocket;
        private Socket clientSocket;

        public async Task StartServerAsync()
        {
            try
            {
                serverSocket = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
                serverSocket.Bind(new IPEndPoint(IPAddress.Any, 4000));
                serverSocket.Listen(5);

                while (true)
                {
                    clientSocket = await serverSocket.AcceptAsync();
                    _ = HandleMessageAsync(clientSocket);
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private async Task HandleMessageAsync(Socket client)
        {
            try
            {
                using (NetworkStream stream = new NetworkStream(client))
                {
                    byte[] buffer = new byte[1024];
                    int bytesRead = await stream.ReadAsync(buffer, 0, buffer.Length);
                    string data = Encoding.ASCII.GetString(buffer, 0, bytesRead);

                    string[] parts = data.Split(',');
                    string objectName = parts[0];
                    string state = parts[1];

                    ChangeObjectState(objectName, state);
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
            finally
            {
                client.Close();
            }
        }

        public void ChangeObjectState(string Object, string State)
        {
            if (Object == "Button")
            {
                if (State == "disabled")
                {
                    button1.Enabled = true;
                }
                else
                {
                    button1.Enabled = false;
                }
            }
            else{
                if (State == "visible")
                {
                    groupBox1.Visible = true;
                }
                else if (State == "invisible")
                {
                    groupBox1.Visible = false;
                }
                else if (State == "white")
                {
                    groupBox1.BackColor = Color.White;
                }
                else if (State == "red")
                {
                    groupBox1.BackColor = Color.Red;
                }
                else
                {
                    groupBox1.BackColor = Color.Yellow;
                }
            }
        }
    }
}
