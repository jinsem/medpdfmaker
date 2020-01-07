Sub DailyCCHPOldFormat()

    ' Constants
    Const CANCELLED_COL As String = "B"
    Const PICKUP_DATE_COL_IDX As Integer = 7
    Const WORK_BOOK_NAME as String = "SFTAXI - MONTHLY EXPORT"

    ' Simple counter
    Dim x As Long
    Dim today As Date
    today = DateAdd("d", 1, Date)
    Dim tmrow As Date
    tmrow = DateAdd("d", 2, Date)
    Dim datesPromt As String
    Dim inputResponse As Variant
    Dim defaultDates As String
    Dim LastR As Long

    LastR = Range("A1:A" & Range("A1").End(xlDown).Row).Rows.Count

    datesPromt = "All cancelled trips will be deleted " & vbNewLine _
                 & "Default dates selected: " & today & " to  " & tmrow & vbNewLine _
                 & "Edit dates if needed and press OK " & vbNewLine & vbNewLine _
                 & "Or press Cancel to stop macros"
    defaultDates = Format(today, "mm/dd/yyyy") & "-" & Format(tmrow, "mm/dd/yyyy")
    inputResponse = InputBox(prompt:=datesPromt, Title:="Enter dates", Default:=defaultDates)
    If inputResponse = False Then
        GoTo TCEnd
    Else
        Dim datesRange() As String
        datesRange = Split(inputResponse, "-")
        If UBound(datesRange) - LBound(datesRange) + 1 <> 2 Then
            MsgBox (c1 & vbNewLine & "Dates invalid." & vbNewLine & " Correct format: 01/15/2013-01/15/2013")
            GoTo TCEnd
        End If
        today = CDate(datesRange(0))
        tmrow = CDate(datesRange(1))
    End If

    ' Copy notes
    Columns("N:N").Select
    Selection.Copy
    Range("AM1").Select
    ActiveSheet.Paste
    
    ' twick Notes Column later need to separate from cooridinaotr initials:
    ' fill formula till the end
    ' Copy coordinator initials and notes
    Range("R1").Formula = "Coordinator"
    Range("R2").Formula = "=CONCATENATE(P2,""#"",N2)"
    Range("R2" & ":R" & LastR).FillDown
    Columns("R:R").Select
    convertFormulasToValuesInSelection

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


    ' delete only cancelled
    ' Delete rows out of date range
    For x = Cells(Cells.Rows.Count, PICKUP_DATE_COL_IDX).End(xlUp).Row To 2 Step -1
        If UCase(Range(CANCELLED_COL & x).Value) = UCase("Cancelled") Or _
           Cells(x, PICKUP_DATE_COL_IDX).Value < today Or _
           Cells(x, PICKUP_DATE_COL_IDX).Value > tmrow Then
            Cells(x, PICKUP_DATE_COL_IDX).EntireRow.Delete
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
    setCalibriFont fontSize:=6, followTheme:=False
    Columns("E:E").Select
    setCalibriFont fontSize:=6, followTheme:=False
    Columns("N:O").Select
    setCalibriFont fontSize:=6, followTheme:=False
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
    ActiveWorkbook.Worksheets(WORK_BOOK_NAME).Sort.SortFields.Clear
    ActiveWorkbook.Worksheets(WORK_BOOK_NAME).Sort.SortFields.Add Key _
        :=Range("A2:A649"), SortOn:=xlSortOnValues, Order:=xlAscending, _
        DataOption:=xlSortNormal
    With ActiveWorkbook.Worksheets(WORK_BOOK_NAME).Sort
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
    ActiveWorkbook.Worksheets(WORK_BOOK_NAME).Sort.SortFields.Clear
    ActiveWorkbook.Worksheets(WORK_BOOK_NAME).Sort.SortFields.Add Key _
        :=Range("A1"), SortOn:=xlSortOnValues, Order:=xlAscending, DataOption:=xlSortNormal
    With ActiveWorkbook.Worksheets(WORK_BOOK_NAME).Sort
        .SetRange Range("A1:U649")
        .Header = xlYes
        .MatchCase = False
        .Orientation = xlTopToBottom
        .SortMethod = xlPinYin
        .Apply
    End With
    ActiveWorkbook.Worksheets(WORK_BOOK_NAME).Sort.SortFields.Clear
    ActiveWorkbook.Worksheets(WORK_BOOK_NAME).Sort.SortFields.Add Key _
        :=Range("B2:B649"), SortOn:=xlSortOnValues, Order:=xlAscending, _
        DataOption:=xlSortNormal
    With ActiveWorkbook.Worksheets(WORK_BOOK_NAME).Sort
        .SetRange Range("A1:U649")
        .Header = xlYes
        .MatchCase = False
        .Orientation = xlTopToBottom
        .SortMethod = xlPinYin
        .Apply
    End With
    ActiveWorkbook.Worksheets(WORK_BOOK_NAME).Sort.SortFields.Clear
    ActiveWorkbook.Worksheets(WORK_BOOK_NAME).Sort.SortFields.Add Key _
        :=Range("E2:E649"), SortOn:=xlSortOnValues, Order:=xlAscending, _
        DataOption:=xlSortNormal
    ActiveWorkbook.Worksheets(WORK_BOOK_NAME).Sort.SortFields.Add Key _
        :=Range("J2:J649"), SortOn:=xlSortOnValues, Order:=xlAscending, _
        DataOption:=xlSortNormal
    With ActiveWorkbook.Worksheets(WORK_BOOK_NAME).Sort
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
    setCalibriFont fontSize:=8, followTheme:=True
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
    Columns("X:X").Select
    Application.CutCopyMode = False
    Selection.Copy
    Range("R1").Select
    ActiveSheet.Paste
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
    setCalibriFont fontSize:=6, followTheme:=True
    Range("B1").Select
    setCalibriFont fontSize:=9, followTheme:=True
    Range("R1").Select
    setCalibriFont fontSize:=9, followTheme:=True
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
 
    Range("A1").Select
    MsgBox ("Completed OK" & vbNewLine & "Red time is calculated + 2 hrs from appointment time" & vbNewLine & vbNewLine & " Don't forget to Save As this file ")
End Sub

Private Sub setCalibriFont(fontSize As Integer, followTheme As Boolean)
    With Selection.Font
        .Name = "Calibri"
        .Size = fontSize
        .Strikethrough = False
        .Superscript = False
        .Subscript = False
        .OutlineFont = False
        .Shadow = False
        .Underline = xlUnderlineStyleNone
        .TintAndShade = 0
    End With
    If followTheme Then
        With Selection.Font
            .ThemeColor = xlThemeColorLight1
            .ThemeFont = xlThemeFontMinor
        End With
    End If
End Sub

private Sub convertFormulasToValuesInSelection()
    Dim rng As Range
    For Each rng In Selection
        If rng.HasFormula Then
            rng.Formula = rng.Value
        End If
    Next rng
End Sub