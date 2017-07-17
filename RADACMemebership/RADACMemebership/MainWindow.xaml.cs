using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;
using System.Data.SQLite;
using System.Data;
using Outlook = Microsoft.Office.Interop.Outlook;
using System.IO;
using System.Text.RegularExpressions;


namespace RADACMemebership
{

    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {


        int IDForUpdate;
        public static string fullname;
        

        public MainWindow()
        {
            InitializeComponent();


            GenderCmbx.Items.Add("Male");
            GenderCmbx.Items.Add("Female");
            MemberShipClassCmbx.Items.Add("Junior");
            MemberShipClassCmbx.Items.Add("Senior");
            MemberShipClassCmbx.Items.Add("OAP");
            MemberShipClassCmbx.Items.Add("Lady");
            MemberShipClassCmbx.Items.Add("Disabled");
            TypeOfFishingCmbx.Items.Add("Coarse");
            TypeOfFishingCmbx.Items.Add("Game");
            TypeOfFishingCmbx.Items.Add("Both");
            MemberShipStatusCbmx.Items.Add("Current");
            MemberShipStatusCbmx.Items.Add("Past");
            MemberShipStatusCbmx.Items.Add("Waiting");
            MemberShipStatusCbmx.Items.Add("Expelled");
            MemberShipStatusCbmx.Items.Add("On Hold");
            MemberShipStatusCbmx.Items.Add("Honorary");
            MemberShipStatusCbmx.Items.Add("Declined");
            DilWorthSupCmbx.Items.Add("Paid");
            DilWorthSupCmbx.Items.Add("Not Paid");

            EditGenderCmbx.Items.Add("Male");
            EditGenderCmbx.Items.Add("Female");
            EditMemberShipClassCmbx.Items.Add("Junior");
            EditMemberShipClassCmbx.Items.Add("Senior");
            EditMemberShipClassCmbx.Items.Add("OAP");
            EditMemberShipClassCmbx.Items.Add("Lady");
            EditMemberShipClassCmbx.Items.Add("Disabled");
            EditTypeOfFishingCmbx.Items.Add("Coarse");
            EditTypeOfFishingCmbx.Items.Add("Game");
            EditTypeOfFishingCmbx.Items.Add("Both");
            EditMemberShipStatusCbmx.Items.Add("Current");
            EditMemberShipStatusCbmx.Items.Add("Past");
            EditMemberShipStatusCbmx.Items.Add("Waiting");
            EditMemberShipStatusCbmx.Items.Add("Expelled");
            EditMemberShipStatusCbmx.Items.Add("On Hold");
            EditMemberShipStatusCbmx.Items.Add("Honorary");
            EditMemberShipStatusCbmx.Items.Add("Declined");
            EditDilWorthSupCmbx.Items.Add("Paid");
            EditDilWorthSupCmbx.Items.Add("Not Paid");


            string fileName = "MemberShipDatabase.db";
            FileInfo f = new FileInfo(fileName);
            fullname = f.FullName;
            //int test = MemberDataGrid.Columns.Count;

            FillDataGrid();
            FillPendingList();




        }

        private void FillPendingList()
        {

            SQLiteConnection sqlite = new SQLiteConnection("Data Source=" + fullname);
            string sql = "SELECT * FROM MemberDetails WHERE MembershipStatus = 'Waiting'";
            SQLiteDataAdapter ad;
            DataTable dt = new DataTable();

            try
            {
                SQLiteCommand cmd;
                sqlite.Open();  //Initiate connection to the db
                cmd = sqlite.CreateCommand();
                cmd.CommandText = sql;  //set the passed query
                ad = new SQLiteDataAdapter(cmd);
                ad.Fill(dt); //fill the datasource

                if (dt.Rows.Count == 0)
                {
                    MessageBox.Show("No record found.");

                }
                else
                {
                    EnumerableRowCollection<DataRow> query = from row in dt.AsEnumerable()
                                                             orderby DateTime.Parse(row.Field<string>("ApplicationDate")) ascending
                                                             select row;
                    dt = query.AsDataView().ToTable();

                    dataGridForPending.ItemsSource = dt.DefaultView;
                }

            }
            catch (SQLiteException ex)
            {
                MessageBox.Show(ex.Message);
            }
            sqlite.Close();

        }

        private void FillDataGrid()
        {
            SQLiteConnection sqlite = new SQLiteConnection("Data Source=" + fullname);
            string query = "SELECT * FROM MemberDetails";

            DataSet ds;

            sqlite.Open();

            ds = new DataSet();
            var da = new SQLiteDataAdapter(query, sqlite);
            da.Fill(ds);

            MemberDataGrid.ItemsSource = ds.Tables[0].DefaultView;
        }

        public class DataClass
        {


            public DataClass()
            {

            }

            public DataTable selectQuery(string query)
            {
                string fileName = "MemberShipDatabase.db";
                FileInfo f = new FileInfo(fileName);
                string fullname = f.FullName;

                SQLiteConnection sqlite = new SQLiteConnection("Data Source=" + fullname);

                SQLiteDataAdapter ad;
                DataTable dt = new DataTable();

                try
                {
                    SQLiteCommand cmd;
                    sqlite.Open();  //Initiate connection to the db
                    cmd = sqlite.CreateCommand();
                    cmd.CommandText = query;  //set the passed query
                    ad = new SQLiteDataAdapter(cmd);
                    ad.Fill(dt); //fill the datasource
                }
                catch (SQLiteException ex)
                {
                    MessageBox.Show("test");
                }
                sqlite.Close();
                return dt;
            }

            public void Insert(string forename, string surname, string inital, string gender, string dob, string memclass, string fishtype, string doj, string memstat, string firstline, string secondline, string thirdline, string postcode, string email, string carreg, string tel, string notes, string appdate, string dilwortsup)
            {
                string fileName = "MemberShipDatabase.db";
                FileInfo f = new FileInfo(fileName);
                string fullname = f.FullName;

                SQLiteConnection sqlite = new SQLiteConnection("Data Source=" + fullname);

                SQLiteCommand insertSQL = new SQLiteCommand();

                insertSQL.CommandText = @"INSERT INTO MemberDetails (Forename, Surname, MidInital, Gender, DOB, MembershipClass, FishingType, JoiningDate, MembershipStatus, FirstLineAddress, SecondLineAddress, ThirdLineAddress, PostCode, Email, CarReg, Telephone, Notes, ApplicationDate, DilworthSup) VALUES (@Forename, @Surname, @MidInital, @Gender, @DOB, @MembershipClass, @FishingType, @DOJoining, @MemebershipStatus, @FirstLineAddress, @SecondLineAddress, @ThirdLineAddress, @PostCode, @Email, @CarReg, @Telephone, @Notes, @ApplicationDate, @DilworthSup)";
                insertSQL.Connection = sqlite;
                insertSQL.Parameters.Add(new SQLiteParameter("@Forename", forename));
                insertSQL.Parameters.Add(new SQLiteParameter("@Surname", surname));
                insertSQL.Parameters.Add(new SQLiteParameter("@MidInital", inital));
                insertSQL.Parameters.Add(new SQLiteParameter("@Gender", gender));
                insertSQL.Parameters.Add(new SQLiteParameter("@DOB", dob));
                insertSQL.Parameters.Add(new SQLiteParameter("@MembershipClass", memclass));
                insertSQL.Parameters.Add(new SQLiteParameter("@FishingType", fishtype));
                insertSQL.Parameters.Add(new SQLiteParameter("@DOJoining", doj));
                insertSQL.Parameters.Add(new SQLiteParameter("@MemebershipStatus", memstat));
                insertSQL.Parameters.Add(new SQLiteParameter("@FirstLineAddress", firstline));
                insertSQL.Parameters.Add(new SQLiteParameter("@SecondLineAddress", secondline));
                insertSQL.Parameters.Add(new SQLiteParameter("@ThirdLineAddress", thirdline));
                insertSQL.Parameters.Add(new SQLiteParameter("@PostCode", postcode));
                insertSQL.Parameters.Add(new SQLiteParameter("@Email", email));
                insertSQL.Parameters.Add(new SQLiteParameter("@CarReg", carreg));
                insertSQL.Parameters.Add(new SQLiteParameter("@Telephone", tel));
                insertSQL.Parameters.Add(new SQLiteParameter("@Notes", notes));
                insertSQL.Parameters.Add(new SQLiteParameter("@ApplicationDate", appdate));
                insertSQL.Parameters.Add(new SQLiteParameter("@DilworthSup", dilwortsup));


                try
                {
                    sqlite.Open();
                    insertSQL.ExecuteNonQuery();
                    sqlite.Close();
                }
                catch (Exception ex)
                {
                    throw new Exception(ex.Message);
                }
            }

        }

        private void AddUserBtn_Click(object sender, RoutedEventArgs e)
        {
            Boolean checkFlag = true;

            if (String.IsNullOrEmpty(FirstNameTbx.Text))
            {
                MessageBox.Show("No Forename provided.");
                checkFlag = false;

            } else if (String.IsNullOrEmpty(SecondNameTbx.Text))
            {
                MessageBox.Show("No Surname provided.");
                checkFlag = false;
            }
            else if (String.IsNullOrEmpty(InitalTbx.Text))
            {
                MessageBox.Show("No Inital provided.");
                checkFlag = false;
            }
            else if (MemberShipClassCmbx.SelectedItem == null)
            {
                MessageBox.Show("No membership class provided.");
                checkFlag = false;
            }
            else if (GenderCmbx.SelectedItem == null)
            {
                MessageBox.Show("No gender provided.");
                checkFlag = false;
            }
            else if (String.IsNullOrEmpty(DataOfBirthTbx.Text))
            {
                MessageBox.Show("No D.O.B provided.");
                checkFlag = false;
            }
            else if (DataOfBirthTbx.Text.Length > 10)
            {
                MessageBox.Show("Invalid D.O.B provided. DD-MM-YYYY");
                checkFlag = false;
            }
            else if (TypeOfFishingCmbx.SelectedItem == null)
            {
                MessageBox.Show("No fishing type provided.");
                checkFlag = false;
            }
            else if (String.IsNullOrEmpty(DateOfJoiningTbx.Text))
            {
                MessageBox.Show("No joining date provided.");
                checkFlag = false;
            }
            else if (String.IsNullOrEmpty(AppDateTbx.Text))
            {
                MessageBox.Show("No application date provided.");
                checkFlag = false;
            }
            else if (DateOfJoiningTbx.Text.Length > 10)
            {
                MessageBox.Show("Invalid joining date provided. DD-MM-YYYY");
                checkFlag = false;
            }
            else if (MemberShipStatusCbmx.SelectedItem == null)
            {
                MessageBox.Show("No membership status provided.");
                checkFlag = false;
            }
            else if (DilWorthSupCmbx.SelectedItem == null)
            {
                MessageBox.Show("No Dilworth Supplement status provided.");
                checkFlag = false;
            }
            else if (String.IsNullOrEmpty(FirstLineOfAddressTbx.Text))
            {
                MessageBox.Show("No first line of address provided.");
                checkFlag = false;
            }
            else if (String.IsNullOrEmpty(SecondLineofAddressTbx.Text))
            {
                MessageBox.Show("No second line of address provided.");
                checkFlag = false;
            }
            else if (String.IsNullOrEmpty(ThirdLineAddressTbx.Text))
            {
                MessageBox.Show("No third line of address provided.");
                checkFlag = false;
            }
            else if (String.IsNullOrEmpty(PostCodeTbx.Text))
            {
                MessageBox.Show("No postcode provided.");
                checkFlag = false;
            }
            else if (String.IsNullOrEmpty(EmailAddressTbx.Text))
            {
                MessageBox.Show("No email address provided.");
                checkFlag = false;
            }
            else if (String.IsNullOrEmpty(CarRegTbx.Text))
            {
                MessageBox.Show("No car registration provided.");
                checkFlag = false;
            }
            else if (String.IsNullOrEmpty(TelephoneTbx.Text))
            {
                MessageBox.Show("No car registration provided.");
                checkFlag = false;
            }

            if (checkFlag)
            {
                string firstname = FirstNameTbx.Text;
                string surname = SecondNameTbx.Text;
                string inital = InitalTbx.Text;
                string gender = GenderCmbx.Text;
                string DOB = DataOfBirthTbx.Text;
                string MembershipClass = MemberShipClassCmbx.Text;
                string TypeFishing = TypeOfFishingCmbx.Text;
                string DOJ = DateOfJoiningTbx.Text;
                string MembershipStatus = MemberShipStatusCbmx.Text;
                string Firstline = FirstLineOfAddressTbx.Text;
                string secondline = SecondLineofAddressTbx.Text;
                string thirdline = ThirdLineAddressTbx.Text;
                string postcode = PostCodeTbx.Text;
                string emailaddress = EmailAddressTbx.Text;
                string carReg = CarRegTbx.Text;
                string tel = TelephoneTbx.Text;
                string notes = NotesTbx.Text;
                string appdate = AppDateTbx.Text;
                string dilworth = DilWorthSupCmbx.Text;


                DataClass dc = new DataClass();

                dc.Insert(firstname, surname, inital, gender, DOB, MembershipClass, TypeFishing, DOJ, MembershipStatus, Firstline, secondline, thirdline, postcode, emailaddress, carReg, tel, notes, appdate, dilworth);

                FillDataGrid();
                FillPendingList();

                MessageBox.Show("Database updated.");

                FirstNameTbx.Text = "";
                SecondNameTbx.Text = "";
                InitalTbx.Text = "";
                GenderCmbx.Text = "";
                DataOfBirthTbx.Text = "";
                MemberShipClassCmbx.Text = "";
                TypeOfFishingCmbx.Text = "";
                DateOfJoiningTbx.Text = "";
                MemberShipStatusCbmx.Text = "";
                FirstLineOfAddressTbx.Text = "";
                SecondLineofAddressTbx.Text = "";
                ThirdLineAddressTbx.Text = "";
                PostCodeTbx.Text = "";
                EmailAddressTbx.Text = "";
                CarRegTbx.Text = "";
                TelephoneTbx.Text = "";
                NotesTbx.Text = "";
                AppDateTbx.Text = "";
                DilWorthSupCmbx.Text = "";

            }

        }

        private void SelectBtn_Click(object sender, RoutedEventArgs e)
        {
            if (IDTbx.Text != "")
            {

                SQLiteConnection sqlite = new SQLiteConnection("Data Source=" + fullname);
                string sql = "SELECT * FROM MemberDetails WHERE Id =" + IDTbx.Text;
                SQLiteDataAdapter ad;
                DataTable dt = new DataTable();

                try
                {
                    SQLiteCommand cmd;
                    sqlite.Open();  //Initiate connection to the db
                    cmd = sqlite.CreateCommand();
                    cmd.CommandText = sql;  //set the passed query
                    ad = new SQLiteDataAdapter(cmd);
                    ad.Fill(dt); //fill the datasource

                    if (dt.Rows.Count == 0)
                    {
                        MessageBox.Show("No record found.");
                        IDTbx.Text = "";

                    }
                    else
                    {

                        IDForUpdate = Convert.ToInt32(IDTbx.Text);

                        foreach (DataRow row in dt.Rows)
                        {
                            EditFirstNameTbx.Text = row["Forename"].ToString();
                            EditSecondNameTbx.Text = row["Surname"].ToString();
                            EditInitalTbx.Text = row["MidInital"].ToString();
                            EditGenderCmbx.Text = row["Gender"].ToString();
                            EditDataOfBirthTbx.Text = row["DOB"].ToString();
                            EditMemberShipClassCmbx.Text = row["MembershipClass"].ToString();
                            EditTypeOfFishingCmbx.Text = row["FishingType"].ToString();
                            EditDateOfJoiningTbx.Text = row["JoiningDate"].ToString();
                            EditMemberShipStatusCbmx.Text = row["MembershipStatus"].ToString();
                            FirstLineOfAddressTbxEdit.Text = row["FirstLineAddress"].ToString();
                            SecondLineofAddressTbxEdit.Text = row["SecondLineAddress"].ToString();
                            EditThirdLineAddressTbx.Text = row["ThirdLineAddress"].ToString();
                            EditPostCodeTbx.Text = row["PostCode"].ToString();
                            EditEmailAddressTbx.Text = row["Email"].ToString();
                            EditCarRegTbx.Text = row["CarReg"].ToString();
                            EditTelephoneTbx.Text = row["Telephone"].ToString();
                            EditNotesTbx.Text = row["Notes"].ToString();
                            EditAppDateTbx.Text = row["ApplicationDate"].ToString();
                            EditDilWorthSupCmbx.Text = row["DilworthSup"].ToString();
                        }
                    }

                }
                catch (SQLiteException ex)
                {
                    MessageBox.Show("test");
                }
                sqlite.Close();
            } else
            {
                MessageBox.Show("No ID provided.");
            }

        }

        private void UpdateBtn_Click(object sender, RoutedEventArgs e)
        {
            SQLiteConnection sqlite = new SQLiteConnection("Data Source=" + fullname);
            SQLiteCommand UpdateSQL = new SQLiteCommand();

            UpdateSQL.CommandText = @"UPDATE MemberDetails Set Forename = @Forename, Surname = @Surname, MidInital = @MidInital, Gender = @Gender, DOB = @DOB, MembershipClass = @MembershipClass, FishingType = @FishingType, JoiningDate = @DOJoining, MembershipStatus = @MemebershipStatus, FirstLineAddress = @FirstLineAddress, SecondLineAddress = @SecondLineAddress, ThirdLineAddress = @ThirdLineAddress, PostCode = @PostCode, Email = @Email, CarReg = @CarReg, Telephone = @Telephone, Notes = @Notes, ApplicationDate = @ApplicationDate, DilworthSup = @DilworthSup Where Id =" + IDForUpdate;
            UpdateSQL.Connection = sqlite;
            UpdateSQL.Parameters.Add(new SQLiteParameter("@Forename", EditFirstNameTbx.Text));
            UpdateSQL.Parameters.Add(new SQLiteParameter("@Surname", EditSecondNameTbx.Text));
            UpdateSQL.Parameters.Add(new SQLiteParameter("@MidInital", EditInitalTbx.Text));
            UpdateSQL.Parameters.Add(new SQLiteParameter("@Gender", EditGenderCmbx.Text));
            UpdateSQL.Parameters.Add(new SQLiteParameter("@DOB", EditDataOfBirthTbx.Text));
            UpdateSQL.Parameters.Add(new SQLiteParameter("@MembershipClass", EditMemberShipClassCmbx.Text));
            UpdateSQL.Parameters.Add(new SQLiteParameter("@FishingType", EditTypeOfFishingCmbx.Text));
            UpdateSQL.Parameters.Add(new SQLiteParameter("@DOJoining", EditDateOfJoiningTbx.Text));
            UpdateSQL.Parameters.Add(new SQLiteParameter("@MemebershipStatus", EditMemberShipStatusCbmx.Text));
            UpdateSQL.Parameters.Add(new SQLiteParameter("@FirstLineAddress", FirstLineOfAddressTbxEdit.Text));
            UpdateSQL.Parameters.Add(new SQLiteParameter("@SecondLineAddress", SecondLineofAddressTbxEdit.Text));
            UpdateSQL.Parameters.Add(new SQLiteParameter("@ThirdLineAddress", EditThirdLineAddressTbx.Text));
            UpdateSQL.Parameters.Add(new SQLiteParameter("@PostCode", EditPostCodeTbx.Text));
            UpdateSQL.Parameters.Add(new SQLiteParameter("@Email", EditEmailAddressTbx.Text));
            UpdateSQL.Parameters.Add(new SQLiteParameter("@CarReg", EditCarRegTbx.Text));
            UpdateSQL.Parameters.Add(new SQLiteParameter("@Telephone", EditTelephoneTbx.Text));
            UpdateSQL.Parameters.Add(new SQLiteParameter("@Notes", EditNotesTbx.Text));
            UpdateSQL.Parameters.Add(new SQLiteParameter("@ApplicationDate", EditAppDateTbx.Text));
            UpdateSQL.Parameters.Add(new SQLiteParameter("@DilworthSup", EditDilWorthSupCmbx.Text));


            try
            {

                sqlite.Open();
                UpdateSQL.ExecuteNonQuery();
                sqlite.Close();
                FillDataGrid();
                FillPendingList();

                MessageBox.Show("Record successfully updated.");

                EditFirstNameTbx.Text = "";
                EditSecondNameTbx.Text = "";
                EditInitalTbx.Text = "";
                EditGenderCmbx.Text = "";
                EditDataOfBirthTbx.Text = "";
                EditMemberShipClassCmbx.Text = "";
                EditTypeOfFishingCmbx.Text = "";
                EditDateOfJoiningTbx.Text = "";
                EditMemberShipStatusCbmx.Text = "";
                FirstLineOfAddressTbxEdit.Text = "";
                SecondLineofAddressTbxEdit.Text = "";
                EditThirdLineAddressTbx.Text = "";
                EditPostCodeTbx.Text = "";
                EditEmailAddressTbx.Text = "";
                EditCarRegTbx.Text = "";
                EditTelephoneTbx.Text = "";
                EditNotesTbx.Text = "";
                EditAppDateTbx.Text = "";
                EditDilWorthSupCmbx.Text = "";

            }
            catch (Exception ex)
            {
                throw new Exception(ex.Message);
            }

        }

        private void MemberDataGrid_LoadingRow(object sender, DataGridRowEventArgs e)
        {
            string DOB = ((System.Data.DataRowView)(e.Row.DataContext)).Row.ItemArray[5].ToString();
            string MemClass = ((System.Data.DataRowView)(e.Row.DataContext)).Row.ItemArray[6].ToString();
            string MemStatus = ((System.Data.DataRowView)(e.Row.DataContext)).Row.ItemArray[9].ToString();
            DOB.Replace('-', '/');
            DateTime dt = Convert.ToDateTime(DOB);

            // var today = DateTime.Today;
            // Calculate the age.
            // var age = today.Year - dt.Year;
            // Go back to the year the person was born in case of a leap year
            // if (dt > today.AddYears(-age)) age--;

            //  TimeSpan tm = (DateTime.Now - dt);
            // long age = (long)(tm.Days / 365);

            int age = ((DateTime.Now.Year - dt.Year) * 372 + (DateTime.Now.Month - dt.Month) * 31 + (DateTime.Now.Day - dt.Day)) / 372;

            try
            {
                if (age >= 65 && MemClass == "Senior" && MemStatus == "Senior" || age >= 65 && MemClass == "Junior")
                {
                    e.Row.Background = new SolidColorBrush(Colors.LightGreen);
                }
                else if (age < 18 && MemClass == "Senior" || age < 18 && MemClass == "OAP")
                {
                    e.Row.Background = new SolidColorBrush(Colors.LightSkyBlue);
                }
                else if (age >= 18 && age < 64 && MemClass == "Junior" || age >= 18 && age < 64 && MemClass == "OAP")
                {
                    e.Row.Background = new SolidColorBrush(Colors.PaleVioletRed);
                }
            }
            catch
            {
                MessageBox.Show("Error in age ranges.");
            }
        }

        private void EmailBtn_Click(object sender, RoutedEventArgs e)
        {

            try
            {
                List<string> lstAllRecipients = new List<string>();

                SQLiteConnection sqlite = new SQLiteConnection("Data Source=" + fullname);
                string sql = "SELECT * FROM MemberDetails";
                SQLiteDataAdapter ad;
                DataTable dt = new DataTable();

                try
                {
                    SQLiteCommand cmd;
                    sqlite.Open();  //Initiate connection to the db
                    cmd = sqlite.CreateCommand();
                    cmd.CommandText = sql;  //set the passed query
                    ad = new SQLiteDataAdapter(cmd);
                    ad.Fill(dt); //fill the datasource

                    if (dt.Rows.Count == 0)
                    {
                        MessageBox.Show("No record found.");

                    }
                    else
                    {
                        foreach (DataRow row in dt.Rows)
                        {

                            lstAllRecipients.Add(row["Email"].ToString());

                        }
                    }

                }
                catch (SQLiteException ex)
                {
                    MessageBox.Show("test");
                }
                sqlite.Close();

                Outlook.Application outlookApp = new Outlook.Application();
                Outlook._MailItem oMailItem = (Outlook._MailItem)outlookApp.CreateItem(Outlook.OlItemType.olMailItem);
                Outlook.Inspector oInspector = oMailItem.GetInspector;
                // Thread.Sleep(10000);

                // Recipient
                Outlook.Recipients oRecips = (Outlook.Recipients)oMailItem.Recipients;
                foreach (String recipient in lstAllRecipients)
                {
                    Outlook.Recipient oRecip = (Outlook.Recipient)oRecips.Add(recipient);
                    oRecip.Resolve();
                }

                //Add CC
                /*Outlook.Recipient oCCRecip = oRecips.Add("THIYAGARAJAN.DURAIRAJAN@testmail.com");
                oCCRecip.Type = (int)Outlook.OlMailRecipientType.olCC;
                oCCRecip.Resolve();

                //Add Subject
                oMailItem.Subject = "Test Mail"; */

                // body, bcc etc...

                //Display the mailbox
                oMailItem.Display(true);
            }
            catch (Exception objEx)
            {
                MessageBox.Show(objEx.Message);
            }
        }

        private void LabelBtn_Click(object sender, RoutedEventArgs e)
        {

            try
            {
                List<string> lstAllRecipients = new List<string>();

                SQLiteConnection sqlite = new SQLiteConnection("Data Source=" + fullname);
                string sql = "SELECT * FROM MemberDetails WHERE MembershipStatus = 'Current' OR MembershipStatus = 'Honorary'";
                SQLiteDataAdapter ad;
                DataTable dt = new DataTable();

                try
                {
                    SQLiteCommand cmd;
                    sqlite.Open();  //Initiate connection to the db
                    cmd = sqlite.CreateCommand();
                    cmd.CommandText = sql;  //set the passed query
                    ad = new SQLiteDataAdapter(cmd);
                    ad.Fill(dt); //fill the datasource

                    if (dt.Rows.Count == 0)
                    {
                        MessageBox.Show("No record found.");

                    }
                    else
                    {
                        string[,] ExcelData = new string[dt.Rows.Count, 9];
                        int i = 0;

                        foreach (DataRow row in dt.Rows)
                        {
                         
                            for (int j = 0; j < ExcelData.GetLength(1); j++)
                            {

                                //lstAllRecipients.Add(row["Email"].ToString());

                                switch (j)
                                {

                                    case 0: ExcelData[i, j] = row["Id"].ToString(); break;

                                    case 1: ExcelData[i, j] = row["Forename"].ToString(); break;

                                    case 2: ExcelData[i, j] = row["Surname"].ToString(); break;

                                    case 3: ExcelData[i, j] = row["FirstLineAddress"].ToString(); break;

                                    case 4: ExcelData[i, j] = row["SecondLineAddress"].ToString(); break;

                                    case 5: ExcelData[i, j] = row["ThirdLineAddress"].ToString(); break;

                                    case 6: ExcelData[i, j] = row["PostCode"].ToString(); break;

                                    case 7: ExcelData[i, j] = row["MembershipClass"].ToString(); break;

                                    case 8: ExcelData[i, j] = row["DilworthSup"].ToString(); break;

                                }

                                

                                //ID,FN,SN,FLA,SLA,TLA,PC,FC,DS

                            }

                            i++;

                        }

                        
                         
                 Microsoft.Win32.SaveFileDialog dlg = new Microsoft.Win32.SaveFileDialog();
                 dlg.FileName = ""; // Default file name
                 dlg.InitialDirectory = "C:";
                 dlg.Title = "Save as Excel File";
                 dlg.DefaultExt = ".xlsx"; // Default file extension
                 dlg.Filter = "Excel Files(2003)|*.xls|Excel Files(2007)|*.xlsx"; // Filter files by extension

                 // Show save file dialog box
                 Nullable<bool> result = dlg.ShowDialog();

                 // Process save file dialog box results
                 if (result == true)
                 {

                     Microsoft.Office.Interop.Excel.Application ExcelApp = new Microsoft.Office.Interop.Excel.Application();
                     ExcelApp.Application.Workbooks.Add(Type.Missing);

                     for(int k = 1; k < 9 + 1; k++)
                     {
                                switch (k - 1)
                                {

                                    case 0: ExcelApp.Cells[1, k] = "Id"; break;
                                    
                                    case 1: ExcelApp.Cells[1, k] = "Forename"; break;

                                    case 2: ExcelApp.Cells[1, k] = "Surname";  break;

                                    case 3: ExcelApp.Cells[1, k] = "FirstLineAddress"; break;

                                    case 4: ExcelApp.Cells[1, k] = "SecondLineAddress"; break;

                                    case 5: ExcelApp.Cells[1, k] = "ThirdLineAddress"; break;

                                    case 6: ExcelApp.Cells[1, k] = "PostCode"; break;

                                    case 7: ExcelApp.Cells[1, k] = "MembershipClass"; break;

                                    case 8: ExcelApp.Cells[1, k] = "DilworthSup"; break;

                                }
                                
                     }


                            for (int f = 0; f < ExcelData.GetLength(0); f++)
                            {
                                for (int j = 0; j < ExcelData.GetLength(1); j++)
                                {
                                    ExcelApp.Cells[f + 2, j + 1] = ExcelData[f, j];
                                    
                                }
                            }
                            // Save document
                            string filename = dlg.FileName;

                            ExcelApp.ActiveWorkbook.SaveCopyAs(dlg.FileName.ToString());
                            ExcelApp.ActiveWorkbook.Saved = true;
                            ExcelApp.Quit();
                 } 



                    }

                }
                catch (SQLiteException ex)
                {
                    MessageBox.Show("test");
                }
                sqlite.Close();
          
            }
            catch
            {

            }



    }
    }
}

