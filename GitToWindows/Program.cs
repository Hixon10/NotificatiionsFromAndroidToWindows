using System;
using System.Diagnostics;
using System.IO;
using System.Runtime.InteropServices;

namespace GitToWindows
{
    internal class Program
    {
        private const string GIT_DIRECOTRY = @"";
        
        private const string GITHUB_LOGIN = "";
        private const string GITHUB_PERSONAL_TOKEN = "";
        private const string GITHUB_PULL_URL = @"";

        [DllImport("User32.dll", CharSet = CharSet.Unicode)]
        private static extern int MessageBox(IntPtr h, string m, string c, int type);
        
        public static void Main(string[] args)
        {
            Process process = new Process();
            process.StartInfo.WorkingDirectory = GIT_DIRECOTRY;
            process.StartInfo.FileName = "git";
            process.StartInfo.Arguments = "pull " + GITHUB_PULL_URL;
            process.StartInfo.UseShellExecute = false;
            process.StartInfo.RedirectStandardOutput = true;
            process.StartInfo.RedirectStandardError = true;
            process.Start();
            
            string output = process.StandardOutput.ReadToEnd().Trim();
            process.WaitForExit();
            
            if (!"Already up to date.".Equals(output))
            {
                System.Media.SystemSounds.Hand.Play();
                string lastEventPackage = File.ReadAllText(GIT_DIRECOTRY + @"\events.txt").Trim();
                string[] parts = lastEventPackage.Split(' ');
                DateTime eventDateTime = JavaTimeStampToDateTime(long.Parse(parts[1]));

                MessageBox((IntPtr)0, parts[0] + " at " + eventDateTime, "New event on android", 0);
            }
            else
            {
                Console.WriteLine("There are no new events");
            }
        }
        
        public static DateTime JavaTimeStampToDateTime(long javaTimeStamp)
        {
            // Java timestamp is milliseconds past epoch
            DateTime dateTime = new DateTime(1970, 1, 1, 0, 0, 0, 0, DateTimeKind.Utc);
            dateTime = dateTime.AddMilliseconds(javaTimeStamp).ToLocalTime();
            return dateTime;
        }
    }
}