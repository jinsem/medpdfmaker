Sub DailyCCHPNewFormat()

    Dim d1
    d1 = ".053113.v1"
    Dim x As Long
    Dim iCol As Integer
    Dim tmrow As Date
    Dim today As Date
    Dim Check1
    Dim Check2
  
    Check1 = True
    Check2 = True
    sw = True

    Columns("N:N").Select
    Selection.Copy
    ActiveWindow.LargeScroll ToRight:=1
    Range("AM1").Select
    ActiveSheet.Paste
    Range("Q1").Select
    ActiveCell.FormulaR1C1 = "Notes11"
    ActiveWindow.SmallScroll ToRight:=-2
    
    ' twick Notes Column later need to separate from cooridinaotr initials:
    ' fill formula till the end
    Dim LastR As Long
    LastR = Range("A1:A" & Range("A1").End(xlDown).Row).Rows.Count
    
    Range("Q1").Formula = "Coordinator"
    Range("Q2").Formula = "=CONCATENATE(P2,""#"",N2)"
    Range("Q2" & ":Q" & LastR).FillDown
    ' copy values only and paste into coordinator
    Columns("Q:Q").Select
    Selection.Copy
    Range("R1").Select
    Selection.PasteSpecial Paste:=xlPasteValues, Operation:=xlNone, SkipBlanks:=False, Transpose:=False
    Columns("P:P").Select
    Application.CutCopyMode = False
    Selection.ClearContents
    Columns("R:R").Select
    Selection.Copy
    Range("P1").Select
    ActiveSheet.Paste
    Columns("Q:Q").Select
    Application.CutCopyMode = False
    Selection.ClearContents
    Columns("R:R").Select
    Selection.ClearContents

    If Range("Z1").Value = Empty Then
        ' delete only cancelled
        iCol = 7 'Filter all on Col G
        For x = Cells(Cells.Rows.Count, iCol).End(xlUp).Row To 2 Step -1
            If UCase(Range("b" & x).Value) = UCase("Cancelled") Then
            Cells(x, iCol).EntireRow.Delete
            End If
        Next
        Range("Z1").Select
        ActiveCell.FormulaR1C1 = "x1"
        c1 = Range("Z1").Value
        Range("B2").Select
    End If
    
    If Range("B2").Value <> Empty And UCase(Range("B2").Value) <> UCase("Cancelled") Then ' check after entered manuall dates
        Range("Z1").Select
        ActiveCell.FormulaR1C1 = "x3"
        c1 = Range("Z1").Value
        Range("B2").Select
    End If
    
    dd = Weekday(Date)
    If dd = 1 Then
        ddd = "Sunday"
    ElseIf dd = 2 Then
        ddd = "Monday"
    ElseIf dd = 3 Then
        ddd = "Tuesday"
    ElseIf dd = 4 Then
        ddd = "Wednesday"
    ElseIf dd = 5 Then
        ddd = "Thursday"
    ElseIf dd = 6 Then
        ddd = "Friday"
    ElseIf dd = 7 Then
        ddd = "Saturday"
    End If
    
    Range("B2:B3").Select
    With Selection.Interior
        .Pattern = xlSolid
        .PatternColorIndex = xlAutomatic
        .Color = 5296274    'green
        .TintAndShade = 0
        .PatternTintAndShade = 0
    End With
    Range("B2").Select
       
    If Range("B2").Value = Empty Then
        today = DateAdd("d", 1, Date)  ' today = DateAdd("d", 1, Date)
    Else
        Check1 = IsDate(Range("B2").Value)
        If Check1 = True Then
            today = Cells(2, "b").Value
        End If
    End If
    
    If Range("B3").Value = Empty Then
        tmrow = DateAdd("d", 2, Date)
    Else
        Check2 = IsDate(Range("B3").Value)
        If Check2 = True Then
            tmrow = Cells(3, "B").Value
        End If
    End If
    
    If Check1 = False Or Check2 = False Then
        MsgBox (c1 & vbNewLine & "Dates invalid..." & vbNewLine & " Correct format: 01/15/2013 " & vbNewLine & vbNewLine & " B2: Date From " & vbNewLine & " B3: Date To")
        sw = 0
        GoTo TCEnd
    End If
     
    If c1 = "x3" Then
        If MsgBox(c1 & vbNewLine & vbNewLine & "Dates selected:   " & today & "    to  " & tmrow & vbNewLine & "Press YES to continue with these dates" & vbNewLine & vbNewLine & "To enter new dates: press NO, type dates in B2,B3 and restart program", vbYesNo, "Confirmation") = vbNo Then
            GoTo TCEnd
        End If
    Else
        If MsgBox(c1 & d1 & vbNewLine & vbNewLine & "All cancelled trips deleted " & vbNewLine & "Default dates selected:   " & today & "    to  " & tmrow & vbNewLine & "Press YES to continue with these dates" & vbNewLine & vbNewLine & "Or press NO and enter other dates in cell B2, B3", vbYesNo, "Confirmation") = vbNo Then
            GoTo TCEnd
        End If
    End If
    
    Range("Z1").Select
    ActiveCell.FormulaR1C1 = "x2"
    c1 = Range("Z1").Value
    Range("B2").Select
   
    ' Delete rows out of date range
    iCol = 7
    For x = Cells(Cells.Rows.Count, iCol).End(xlUp).Row To 2 Step -1
        If Cells(x, iCol).Value < today Or Cells(x, iCol).Value > tmrow Then
            Cells(x, iCol).EntireRow.Delete
        End If
    Next
    Columns("O:O").Select
    Selection.Copy
    Range("R1").Select
    Selection.Insert Shift:=xlToRight
    Columns("B:B").Select
    Selection.Delete Shift:=xlToLeft
    Columns("O:O").Select
    Selection.Cut
    Columns("B:B").Select
    Selection.Insert Shift:=xlToRight
    Columns("E:F").Select
    Selection.Delete Shift:=xlToLeft
    Columns("H:H").Select
    Selection.Insert Shift:=xlToRight, CopyOrigin:=xlFormatFromLeftOrAbove
    Selection.Insert Shift:=xlToRight, CopyOrigin:=xlFormatFromLeftOrAbove
    Columns("I:I").Select
    Selection.Insert Shift:=xlToRight, CopyOrigin:=xlFormatFromLeftOrAbove
    Columns("J:J").Select
    Selection.Insert Shift:=xlToRight, CopyOrigin:=xlFormatFromLeftOrAbove
    Columns("P:P").Select
    Selection.Delete Shift:=xlToLeft
    Columns("N:N").Select
    Selection.FormatConditions.Add Type:=xlCellValue, Operator:=xlGreater, Formula1:="=""No"""
    Selection.FormatConditions(Selection.FormatConditions.Count).SetFirstPriority
    With Selection.FormatConditions(1).Font
         .Color = -16383844
         .TintAndShade = 0
    End With
    With Selection.FormatConditions(1).Interior
        .PatternColorIndex = xlAutomatic
        .Color = 13551615
        .TintAndShade = 0
    End With
    Selection.FormatConditions(1).StopIfTrue = False
    Columns("E:E").Select
    Selection.NumberFormat = "m/d;@"
    Columns("F:G").Select
    Selection.Replace What:="AM", Replacement:="AM", LookAt:=xlPart, SearchOrder:=xlByRows, MatchCase:=False, SearchFormat:=False, ReplaceFormat:=False
    Selection.Replace What:="PM", Replacement:="PM", LookAt:=xlPart, SearchOrder:=xlByRows, MatchCase:=False, SearchFormat:=False, ReplaceFormat:=False
    Selection.NumberFormat = "h:mm;@"
    Columns("B:B").ColumnWidth = 5.43
    Columns("E:E").ColumnWidth = 5.86
    Columns("B:B").Select
    With Selection.Font
        .Name = "Calibri"
        .Size = 6
        .Strikethrough = False
        .Superscript = False
        .Subscript = False
        .OutlineFont = False
        .Shadow = False
        .Underline = xlUnderlineStyleNone
        .TintAndShade = 0
    End With
    Columns("E:E").Select
    With Selection.Font
        .Name = "Calibri"
        .Size = 6
        .Strikethrough = False
        .Superscript = False
        .Subscript = False
        .OutlineFont = False
        .Shadow = False
        .Underline = xlUnderlineStyleNone
        .TintAndShade = 0
    End With
    Columns("N:O").Select
    With Selection.Font
        .Name = "Calibri"
        .Size = 6
        .Strikethrough = False
        .Superscript = False
        .Subscript = False
        .OutlineFont = False
        .Shadow = False
        .Underline = xlUnderlineStyleNone
        .TintAndShade = 0
    End With
    Selection.ColumnWidth = 6.43
    Selection.ColumnWidth = 3.29
    Columns("O:O").Select
    With Selection
        .HorizontalAlignment = xlLeft
        .Orientation = 0
        .AddIndent = False
        .IndentLevel = 0
        .ShrinkToFit = False
        .ReadingOrder = xlContext
        .MergeCells = False
    End With
    Columns("B:B").Select
    With Selection
        .HorizontalAlignment = xlLeft
        .Orientation = 0
        .AddIndent = False
        .IndentLevel = 0
        .ShrinkToFit = False
        .ReadingOrder = xlContext
        .MergeCells = False
    End With
    Columns("E:E").Select
    With Selection
        .HorizontalAlignment = xlLeft
        .Orientation = 0
        .AddIndent = False
        .IndentLevel = 0
        .ShrinkToFit = False
        .ReadingOrder = xlContext
        .MergeCells = False
    End With
    Rows("1:1").Select
    With Selection.Font
        .Name = "Calibri"
        .Size = 10
        .Strikethrough = False
        .Superscript = False
        .Subscript = False
        .OutlineFont = False
        .Shadow = False
        .Underline = xlUnderlineStyleNone
        .TintAndShade = 0
    End With
    Cells.Select
    ActiveWorkbook.Worksheets("SFTAXI - MONTHLY EXPORT").Sort.SortFields.Clear
    ActiveWorkbook.Worksheets("SFTAXI - MONTHLY EXPORT").Sort.SortFields.Add Key _
        :=Range("A2:A649"), SortOn:=xlSortOnValues, Order:=xlAscending, _
        DataOption:=xlSortNormal
    With ActiveWorkbook.Worksheets("SFTAXI - MONTHLY EXPORT").Sort
        .SetRange Range("A1:W999")
        .Header = xlYes
        .MatchCase = False
        .Orientation = xlTopToBottom
        .SortMethod = xlPinYin
        .Apply
    End With
    Range("K1").Select
    ActiveCell.FormulaR1C1 = "XXXXX"
    Range("I3").Select
    Columns("F:F").Select
    Application.CutCopyMode = False
    Selection.Copy
    Columns("S:S").Select
    ActiveSheet.Paste
    Application.CutCopyMode = False
    Selection.NumberFormat = "h:mm;@"

    Range("I3").Select
    ActiveCell.FormulaR1C1 = _
        "=IF((AND(RC[-8]-R[-1]C[-8]=1,RC[-3]="""",RC[-5]=R[-1]C[-5])),R[-1]C[-2]+TIME(2,0,0),"""")"
    Range("I3").Select
    Selection.AutoFill Destination:=Range("I3:I150"), Type:=xlFillDefault
    Range("I3:I150").Select
    Columns("I:I").Select
    Selection.Copy
    Columns("H:H").Select
    Selection.PasteSpecial Paste:=xlPasteValues, Operation:=xlNone, SkipBlanks:=False, Transpose:=False
    Columns("H:H").Select
    Application.CutCopyMode = False
    Selection.NumberFormat = "h:mm;@"
    Selection.FormatConditions.Add Type:=xlCellValue, Operator:=xlLess, Formula1:="=1"
    Selection.FormatConditions(Selection.FormatConditions.Count).SetFirstPriority
    With Selection.FormatConditions(1).Font
        .Color = -16383844
        .TintAndShade = 0
    End With
    With Selection.FormatConditions(1).Interior
        .PatternColorIndex = xlAutomatic
        .Color = 13551615
        .TintAndShade = 0
    End With
    Selection.FormatConditions(1).StopIfTrue = False
    Columns("I:I").Select
    Selection.ClearContents

    Range("I2").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(RC[-3]<>"""",TEXT(RC[-3],""HH:MM""),CONCATENATE(TEXT(RC[-1],""HH:MM""),""_""))"
    Range("I2").Select
    Selection.AutoFill Destination:=Range("I2:I150"), Type:=xlFillDefault
    Range("I2:I150").Select
    Columns("F:F").Select
    Selection.Copy
    Columns("K:K").Select
    ActiveSheet.Paste
    Range("K1").Select
    Application.CutCopyMode = False
    ActiveCell.FormulaR1C1 = "Pickup_timeORIGINAL"
    Columns("F:F").Select
    Selection.NumberFormat = "General"
    Columns("F:F").Select
    Columns("I:I").Select
    Selection.Copy
    Columns("J:J").Select
    Selection.PasteSpecial Paste:=xlPasteValues, Operation:=xlNone, SkipBlanks:=False, Transpose:=False
    Columns("J:J").Select
    Application.CutCopyMode = False
    Selection.Copy
    Range("F1").Select
    ActiveSheet.Paste
    Range("F1").Select
    Application.CutCopyMode = False
    ActiveCell.FormulaR1C1 = "PickupTime*"
    Columns("H:I").Select
    Selection.ClearContents
    Columns("F:F").Select
    Selection.FormatConditions.Add Type:=xlTextString, String:="_", TextOperator:=xlContains
    Selection.FormatConditions(Selection.FormatConditions.Count).SetFirstPriority
    With Selection.FormatConditions(1).Font
        '.Color = -16383844
        .TintAndShade = 0
    End With
    Selection.FormatConditions(1).StopIfTrue = False
    Columns("H:H").Select
    With Selection.Interior
        .Pattern = xlNone
        .TintAndShade = 0
        .PatternTintAndShade = 0
    End With
    Columns("H:H").Select
    Selection.FormatConditions.Delete
    Columns("J:J").Select
    Selection.ClearContents
    Columns("K:K").Select
    Selection.Copy
    Columns("H:H").Select
    ActiveSheet.Paste
    Columns("K:K").Select
    Application.CutCopyMode = False
    Selection.ClearContents
    Range("I2").Select
    
    Range("K2:K3").Select
    Range("K3").Activate
    
    Columns("H:H").Select
    With Selection.Interior
        .PatternColor = 12632256
        .Color = 65535
        .TintAndShade = 0
        .PatternTintAndShade = 0
    End With
    Columns("G:G").Select
    With Selection
        .HorizontalAlignment = xlLeft
        .Orientation = 0
        .AddIndent = False
        .IndentLevel = 0
        .ShrinkToFit = False
        .ReadingOrder = xlContext
        .MergeCells = False
    End With
    Columns("B:B").ColumnWidth = 4.29
    Columns("C:C").ColumnWidth = 10
    Columns("D:D").ColumnWidth = 12.14
    Columns("E:E").ColumnWidth = 5.14
    Columns("F:F").ColumnWidth = 8.29
    Selection.ColumnWidth = 8.43
    Columns("H:K").Select
    Selection.ColumnWidth = 7.71
    Columns("L:L").ColumnWidth = 38.14
    Columns("M:M").ColumnWidth = 39.43
    Columns("N:N").Select
    Selection.Replace What:="Yes(Must)", Replacement:="Must", LookAt:=xlPart, SearchOrder:=xlByRows, MatchCase:=False, SearchFormat:=False, ReplaceFormat:=False
    Range("N5").Select
    Selection.Copy
    Cells.Replace What:="Yes (Must)", Replacement:="Must", LookAt:=xlPart, SearchOrder:=xlByRows, MatchCase:=False, SearchFormat:=False, ReplaceFormat:=False
    Columns("N:N").Select
    Selection.FormatConditions.Add Type:=xlTextString, String:="Must", TextOperator:=xlContains
    Selection.FormatConditions(Selection.FormatConditions.Count).SetFirstPriority
    With Selection.FormatConditions(1).Font
        .Color = -16383844
        .TintAndShade = 0
    End With
    With Selection.FormatConditions(1).Interior
        .PatternColorIndex = xlAutomatic
        .Color = 13551615
        .TintAndShade = 0
    End With
    Selection.FormatConditions(1).StopIfTrue = False
    
    Range("J1").Select
    Application.CutCopyMode = False
    ActiveCell.FormulaR1C1 = "SortTime"
    Range("J2").Select
    ActiveCell.FormulaR1C1 = "=IF(RC[9]<>"""",RC[9],TIMEVALUE(SUBSTITUTE(RC[-4],""_"","""")))"
    Columns("J:J").Select
    Selection.NumberFormat = "h:mm;@"
    Range("J2").Select
    Selection.AutoFill Destination:=Range("J2:J139"), Type:=xlFillDefault
    Range("J2:J139").Select
    Cells.Select
    ActiveWorkbook.Worksheets("SFTAXI - MONTHLY EXPORT").Sort.SortFields.Clear
    ActiveWorkbook.Worksheets("SFTAXI - MONTHLY EXPORT").Sort.SortFields.Add Key _
        :=Range("A1"), SortOn:=xlSortOnValues, Order:=xlAscending, DataOption:=xlSortNormal
    With ActiveWorkbook.Worksheets("SFTAXI - MONTHLY EXPORT").Sort
        .SetRange Range("A1:U649")
        .Header = xlYes
        .MatchCase = False
        .Orientation = xlTopToBottom
        .SortMethod = xlPinYin
        .Apply
    End With
    ActiveWorkbook.Worksheets("SFTAXI - MONTHLY EXPORT").Sort.SortFields.Clear
    ActiveWorkbook.Worksheets("SFTAXI - MONTHLY EXPORT").Sort.SortFields.Add Key _
        :=Range("B2:B649"), SortOn:=xlSortOnValues, Order:=xlAscending, _
        DataOption:=xlSortNormal
    With ActiveWorkbook.Worksheets("SFTAXI - MONTHLY EXPORT").Sort
        .SetRange Range("A1:U649")
        .Header = xlYes
        .MatchCase = False
        .Orientation = xlTopToBottom
        .SortMethod = xlPinYin
        .Apply
    End With
    ActiveWorkbook.Worksheets("SFTAXI - MONTHLY EXPORT").Sort.SortFields.Clear
    ActiveWorkbook.Worksheets("SFTAXI - MONTHLY EXPORT").Sort.SortFields.Add Key _
        :=Range("E2:E649"), SortOn:=xlSortOnValues, Order:=xlAscending, _
        DataOption:=xlSortNormal
    ActiveWorkbook.Worksheets("SFTAXI - MONTHLY EXPORT").Sort.SortFields.Add Key _
        :=Range("J2:J649"), SortOn:=xlSortOnValues, Order:=xlAscending, _
        DataOption:=xlSortNormal
    With ActiveWorkbook.Worksheets("SFTAXI - MONTHLY EXPORT").Sort
        .SetRange Range("A1:U649")
        .Header = xlYes
        .MatchCase = False
        .Orientation = xlTopToBottom
        .SortMethod = xlPinYin
        .Apply
    End With
    Columns("J:J").Select
    Selection.ClearContents
    Columns("P:Q").Select
    Range("Q1").Activate
    Selection.ClearContents
    Columns("S:S").Select
    Selection.ClearContents
    
    Columns("L:M").Select
    Columns("L:M").Select
    Selection.Replace What:="#*", Replacement:="", LookAt:=xlPart, _
        SearchOrder:=xlByRows, MatchCase:=False, SearchFormat:=False, _
        ReplaceFormat:=False
    Selection.Replace What:="/*", Replacement:="", LookAt:=xlPart, _
        SearchOrder:=xlByRows, MatchCase:=False, SearchFormat:=False, _
        ReplaceFormat:=False
    Selection.Replace What:=".,", Replacement:="", LookAt:=xlPart, _
        SearchOrder:=xlByRows, MatchCase:=False, SearchFormat:=False, _
        ReplaceFormat:=False
    Selection.ColumnWidth = 30.43

    ' clean all leftovers after last detail line
    numofrows = Cells(Rows.Count, "a").End(xlUp).Row
    Rows((numofrows + 1) & ":200").ClearContents
    Columns("O:O").Select
    Selection.FormatConditions.Add Type:=xlCellValue, Operator:=xlGreater, Formula1:="=2"
    Selection.FormatConditions(Selection.FormatConditions.Count).SetFirstPriority
    With Selection.FormatConditions(1).Interior
        .PatternColorIndex = xlAutomatic
        .Color = 3381759
        .TintAndShade = 0
    End With
    Selection.FormatConditions(1).StopIfTrue = False
    Range("O1").Select
    ActiveCell.FormulaR1C1 = "TP"
    Rows("1:1").Select
    With Selection.Font
        .Name = "Calibri"
        .Size = 8
        .Strikethrough = False
        .Superscript = False
        .Subscript = False
        .OutlineFont = False
        .Shadow = False
        .Underline = xlUnderlineStyleNone
        .TintAndShade = 0
    End With
    Columns("L:M").Select
    Selection.Replace What:="STREET", Replacement:="ST", LookAt:=xlPart, _
        SearchOrder:=xlByRows, MatchCase:=False, SearchFormat:=False, _
        ReplaceFormat:=False
    Selection.Replace What:="AVENUE", Replacement:="AVE", LookAt:=xlPart, _
        SearchOrder:=xlByRows, MatchCase:=False, SearchFormat:=False, _
        ReplaceFormat:=False

    ' Delete column with original dates
    Columns("H:H").Select
    Selection.Cut
    Range("T1").Select
    Selection.Insert Shift:=xlToRight
    ActiveWindow.SmallScroll ToRight:=-3
    ActiveWindow.LargeScroll ToRight:=-2
    Columns("H:H").Select
    Selection.Insert Shift:=xlToRight, CopyOrigin:=xlFormatFromLeftOrAbove

    ActSh = ActiveSheet.Name

    Application.ScreenUpdating = False
    Worksheets(ActSh).Select
    For Each r In Worksheets(ActSh).UsedRange.Rows

    n = r.Row
    If Worksheets(ActSh).Cells(n, 14) = "Must" Then
        Range("H" & n & ":N" & n).Select
        With Selection.Interior
            .Pattern = xlSolid
            .PatternColorIndex = xlAutomatic
            .Color = 5287936
            .TintAndShade = 0
            .PatternTintAndShade = 0
        End With
    End If
    Next r
    Application.ScreenUpdating = True

    ' move phone to left
    Range(Columns(16), Columns(17)).Select
    Selection.Delete Shift:=xlToLeft
    
    ' highlight first row
    Rows("1:1").EntireRow.Select
    Selection.FormatConditions.Delete
    ActiveCell.Range("A1:P1").Select
    With Selection.Interior
        .PatternColorIndex = xlAutomatic
        .ThemeColor = xlThemeColorDark1
        .TintAndShade = -0.249977111117893
        .PatternTintAndShade = 0
    End With

    Columns("A:A").Select
    With Selection
        .HorizontalAlignment = xlLeft
        .Orientation = 0
        .AddIndent = False
        .IndentLevel = 0
        .ShrinkToFit = False
        .ReadingOrder = xlContext
        .MergeCells = False
    End With
    Range("A1").Select
    With Selection
        .HorizontalAlignment = xlCenter
        .VerticalAlignment = xlCenter
        .WrapText = False
        .Orientation = 0
        .AddIndent = False
        .IndentLevel = 0
        .ShrinkToFit = False
        .ReadingOrder = xlContext
        .MergeCells = False
    End With
    Columns("G:G").Select
    With Selection
        .HorizontalAlignment = xlRight
        .Orientation = 0
        .AddIndent = False
        .IndentLevel = 0
        .ShrinkToFit = False
        .ReadingOrder = xlContext
        .MergeCells = False
    End With
    Range("G1").Select
    With Selection
        .HorizontalAlignment = xlCenter
        .VerticalAlignment = xlCenter
        .WrapText = False
        .Orientation = 0
        .AddIndent = False
        .IndentLevel = 0
        .ShrinkToFit = False
        .ReadingOrder = xlContext
        .MergeCells = False
    End With
    Columns("G:G").Select
    Selection.Insert Shift:=xlToRight, CopyOrigin:=xlFormatFromLeftOrAbove
    Columns("R:R").Select
    Selection.Insert Shift:=xlToRight, CopyOrigin:=xlFormatFromLeftOrAbove
    Range("R1").Select
    ActiveCell.FormulaR1C1 = "Notes"
    Range("S1").Select
    With Selection.Interior
        .Pattern = xlNone
        .TintAndShade = 0
        .PatternTintAndShade = 0
    End With
    Columns("Q:Q").Select
    Selection.Replace What:="(415) ", Replacement:="", LookAt:=xlPart, _
        SearchOrder:=xlByRows, MatchCase:=False, SearchFormat:=False, _
        ReplaceFormat:=False
    Selection.ColumnWidth = 10.29
    Selection.ColumnWidth = 12.14


    ' format with _ in column F
    ' Find all the cell F with "_" and format font

    Application.ScreenUpdating = False
    Worksheets(ActiveSheet.Name).Select
    For Each r In Worksheets(ActiveSheet.Name).UsedRange.Rows
    n = r.Row
    If InStr(Cells(n, 6).Text, "_") Then
        Range("F" & n & ":F" & n).Select
        With Selection.Interior
            Selection.Font.Size = 8
            .Color = 49407
            Selection.HorizontalAlignment = xlLeft
        End With
    Else
        Range("F" & n & ":F" & n).Select
        With Selection.Interior
            'Selection.Font.Size = 22
            Selection.Font.Bold = True
            Selection.HorizontalAlignment = xlRight
        End With
    End If

    Next r
    Application.ScreenUpdating = True

    '  Notes...
    
    Dim LastRow As Long
    LastRow = Range("A1:A" & Range("A1").End(xlDown).Row).Rows.Count
    
    Range("U1").Formula = "Coordinator"
    Range("U2").Formula = "=LEFT(B2,FIND(""#"",B2)-1)"
    Range("U2" & ":U" & LastRow).FillDown
    
    Range("V1").Formula = "Notes"
    Range("V2").Formula = "=RIGHT(B2,LEN(B2)-FIND(""#"",B2))"
    Range("V2" & ":V" & LastRow).FillDown
    Columns("U:V").Select
    Selection.Copy
    Range("W1").Select
    Selection.PasteSpecial Paste:=xlPasteValues, Operation:=xlNone, SkipBlanks:=False, Transpose:=False
    Columns("W:W").Select
    Columns("B:B").Select
    Selection.Font.Size = 6
    Application.CutCopyMode = False
    Selection.ClearContents
    Columns("W:W").Select
    Selection.Copy
    Range("B1").Select
    ActiveSheet.Paste
    ActiveWindow.LargeScroll ToRight:=1
    Columns("X:X").Select
    Application.CutCopyMode = False
    Selection.Copy
    ActiveWindow.LargeScroll ToRight:=-1
    Range("R1").Select
    ActiveSheet.Paste
    ActiveWindow.SmallScroll ToRight:=6
    Selection.ColumnWidth = 13.57
    Columns("U:X").Select
    Application.CutCopyMode = False
    Selection.ClearContents
    
    Columns("B:B").Select
    Selection.ColumnWidth = 4.71
    Range("B1").Select
    With Selection.Interior
        .Pattern = xlSolid
        .PatternColorIndex = xlAutomatic
        .ThemeColor = xlThemeColorDark1
        .TintAndShade = -0.249977111117893
        .PatternTintAndShade = 0
    End With
    Selection.Font.Bold = True
   
   
    ' formating for new notes/inititanl
    Columns("B:B").Select
    With Selection.Font
        .Name = "Calibri"
        .Size = 6
        .Strikethrough = False
        .Superscript = False
        .Subscript = False
        .OutlineFont = False
        .Shadow = False
        .Underline = xlUnderlineStyleNone
        .ThemeColor = xlThemeColorLight1
        .TintAndShade = 0
        .ThemeFont = xlThemeFontMinor
    End With
    Range("B1").Select
    With Selection.Font
        .Name = "Calibri"
        .Size = 9
        .Strikethrough = False
        .Superscript = False
        .Subscript = False
        .OutlineFont = False
        .Shadow = False
        .Underline = xlUnderlineStyleNone
        .ThemeColor = xlThemeColorLight1
        .TintAndShade = 0
        .ThemeFont = xlThemeFontMinor
    End With
    Range("R1").Select
    With Selection.Font
        .Name = "Calibri"
        .Size = 9
        .Strikethrough = False
        .Superscript = False
        .Subscript = False
        .OutlineFont = False
        .Shadow = False
        .Underline = xlUnderlineStyleNone
        .ThemeColor = xlThemeColorLight1
        .TintAndShade = 0
        .ThemeFont = xlThemeFontMinor
    End With
    Selection.Font.Bold = True
    With Selection.Interior
        .Pattern = xlSolid
        .PatternColorIndex = xlAutomatic
        .ThemeColor = xlThemeColorDark1
        .TintAndShade = -0.249977111117893
        .PatternTintAndShade = 0
    End With
   
    ' Find last day cell and insert empty line between dates 03/13/2013
    r = Application.Match(CLng(tmrow), Range("E1:E100"), 0)
    If Not IsError(r) Then
        Rows(r & ":" & r).Select
        Selection.Insert Shift:=xlDown, CopyOrigin:=xlFormatFromLeftOrAbove
       
        Rows(r & ":" & r).Select
        With Selection.Interior
            .PatternColorIndex = xlAutomatic
            .ThemeColor = xlThemeColorDark1
            .TintAndShade = -0.349986266670736
            .PatternTintAndShade = 0
       End With
    Else
       MsgBox "3. Can not find next date " & tmrow
    End If

TCEnd:
 
    If c1 = "x1" Or c1 = "x3" Then
        Range("Z1").Select
        ActiveCell.FormulaR1C1 = "x2"
        c1 = Range("Z1").Value
        Range("B2").Select
    Else
        Range("B2").Select
        MsgBox (c1 & vbNewLine & "Completed OK" & vbNewLine & "Red time is calculated + 2 hrs from appointment time" & vbNewLine & vbNewLine & " Don't forget to Save As this file ")
    End If
End Sub