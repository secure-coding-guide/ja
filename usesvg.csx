if (Args.Count != 2)
{
    var args = Environment.GetCommandLineArgs();
    Console.WriteLine($"{args[0]} {args[1]} <input filename> <output filename>");
    return;
}
var map = new Dictionary<string, string>();
string pngfile, svgfile;
foreach(var file in Directory.EnumerateFiles("media", "*.svg"))
{
    svgfile = file.Replace("\\", "/");
    pngfile = svgfile.Replace(".svg", ".png");
    map[pngfile] = svgfile;
}
using (var input = new StreamReader(Args[0], Encoding.UTF8, true))
using (var output = new StreamWriter(Args[1], false, new UTF8Encoding(false)))
{
    string line;
    while(true)
    {
        line = input.ReadLine();
        if (line == null)
        {
            break;
        }
        foreach (var pngfile in map.Keys)
        {
            if (line.Contains(pngfile))
            {
                line = line.Replace(pngfile, map[pngfile]);
            }
        }
        output.WriteLine(line);
    }
}
