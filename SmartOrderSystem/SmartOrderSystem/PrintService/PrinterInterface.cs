using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using bpac;

namespace SmartOrderSystem.PrintService
{
  class PrinterInterface
  {

    public String pathToTemplate = "C:\\Program Files\\Brother bPAC3 SDK\\Templates\\nameplate1.lbx";

    public void printLabel()
    {

      bpac.Document doc = new Document();

      if (doc.Open(pathToTemplate) != false)
      {

        Console.WriteLine("Setting Comany and Name field:");
        doc.GetObject("objCompany").Text = "Wer ist mein Schatz?";
        doc.GetObject("objName").Text = "Chrissi!!!!!";

        Console.WriteLine("SetMediaById to true:");
        doc.SetMediaById(doc.Printer.GetMediaId(), true);

        Console.WriteLine("Now start print:");
        doc.StartPrint("", PrintOptionConstants.bpoDefault);

        Console.WriteLine("Print out:");
        doc.PrintOut(1, PrintOptionConstants.bpoDefault);

        Console.WriteLine("End print and close doc:");
        doc.EndPrint();
        doc.Close();

        Console.WriteLine("And finished! Yeah, Yeah, Yeah!!!");
      }
      else
      {
        Console.WriteLine("Oh no!!! Error: " + doc.ErrorCode);
      }

    }

  }
}
