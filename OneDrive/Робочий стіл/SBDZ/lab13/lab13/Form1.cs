using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;
using MongoDB.Driver;
using System.Diagnostics;
using System.Windows.Forms;

namespace lab13
{
    public partial class Form1 : Form
    {
        static MongoClient client = new MongoClient("mongodb://localhost:27017");
        static IMongoDatabase db = client.GetDatabase("lab13");
        static IMongoCollection<Coach> collection = db.GetCollection<Coach>("Coach");
        public Form1()
        {
            InitializeComponent();
            LoadData();
        }
        public void LoadData()
        {
            dataGridView1.Rows.Clear();

            // Fetch data from MongoDB
            var coaches = collection.Find(FilterDefinition<Coach>.Empty).ToList();

            foreach (var coach in coaches)
            {
                int rowIndex = dataGridView1.Rows.Add();

                dataGridView1.Rows[rowIndex].Cells["Id"].Value = coach.Id;
                dataGridView1.Rows[rowIndex].Cells["name"].Value = coach.Name;
            }
        }

        private void button1_Click(object sender, EventArgs e)
        {
            string Name = textBox1.Text;

            if (!string.IsNullOrEmpty(Name))
            {
                Coach newCoach = new Coach(Name);

                collection.InsertOne(newCoach);

                textBox1.Text = "";
            }
            else
            {
                MessageBox.Show("Please enter a name.");
            }
        }

        private void button2_Click(object sender, EventArgs e)
        {
            string Name = textBox2.Text;

            if (!string.IsNullOrEmpty(Name))
            {
                var filter = Builders<Coach>.Filter.Eq(c => c.Name, Name);

                var result = collection.DeleteMany(filter);

                textBox2.Text = "";
            }
            else
            {
                MessageBox.Show("Please enter a name to delete.");
            }
        }

        private void button4_Click(object sender, EventArgs e)
        {
            string Name = textBox2.Text;

            if (!string.IsNullOrEmpty(Name))
            {
                var filter = Builders<Coach>.Filter.Eq(c => c.Name, Name);

                var coaches = collection.Find(filter).ToList();

                dataGridView1.Rows.Clear();

                foreach (var coach in coaches)
                {
                    int rowIndex = dataGridView1.Rows.Add();
                    dataGridView1.Rows[rowIndex].Cells["Id"].Value = coach.Id;
                    dataGridView1.Rows[rowIndex].Cells["Name"].Value = coach.Name;
                }

                if (coaches.Count == 0)
                {
                    MessageBox.Show($"No coaches found with name '{Name}'.");
                }
            }
            else
            {
                MessageBox.Show("Please enter a name to search.");
            }
        }

        private void button3_Click(object sender, EventArgs e)
        {
            string oldName = textBox3.Text;
            string newName = textBox4.Text;

            if (!string.IsNullOrEmpty(oldName) && !string.IsNullOrEmpty(newName))
            {
                var filter = Builders<Coach>.Filter.Eq(c => c.Name, oldName);

                var update = Builders<Coach>.Update.Set(c => c.Name, newName);

                var result = collection.UpdateMany(filter, update);

                textBox3.Text = "";
                textBox4.Text = "";
            }
            else
            {
                MessageBox.Show("Please enter both old and new names.");
            }
        }

        private void button5_Click(object sender, EventArgs e)
        {
            LoadData();
        }
    }
}
