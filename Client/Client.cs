using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Net.Sockets;
using System.Net;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.IO;
using System.Security.AccessControl;

namespace Client
{
    public partial class Client : Form
    {
        private Socket clientSocket;
        public Client()
        {
            InitializeComponent();
        }

        private void WhiteRadioButton_CheckedChanged(object sender, EventArgs e)
        {
            SendMessage("Panel", "white");
        }

        private void RedRadioButton_CheckedChanged(object sender, EventArgs e)
        {
            SendMessage("Panel", "red");
        }

        private void YellowRadioButton_CheckedChanged(object sender, EventArgs e)
        {
            SendMessage("Panel", "yellow");
        }

        private void ButtonCheckBox_CheckedChanged(object sender, EventArgs e)
        {
            SendMessage("Button", ButtonCheckBox.Checked ? "enabled" : "disabled");
        }

        private void PanelCheckBox_CheckedChanged(object sender, EventArgs e)
        {
            SendMessage("Panel", PanelCheckBox.Checked ? "visible" : "invisible");
        }

        public void SendMessage(string Object, string State)
        {
            string data = Object + "," + State;
            try
            {
                clientSocket = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
                clientSocket.Connect("localhost", 4000);
                NetworkStream stream = new NetworkStream(clientSocket);
                byte[] buffer = Encoding.ASCII.GetBytes(data);
                stream.Write(buffer, 0, buffer.Length);
                clientSocket.Close();
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }
    }
}
