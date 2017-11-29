if (Args.Count != 2)
{
    var args = Environment.GetCommandLineArgs();
    Console.WriteLine($"{args[0]} {args[1]} <input filename> <output filename>");
    return;
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
        if (line.StartsWith("#"))
        {
            line += $"<!-- x{Guid.NewGuid().GetHashCode():x8} -->";
        }
        output.WriteLine(line);
    }
}
