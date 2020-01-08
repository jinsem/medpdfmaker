Sub DailyCCHPOldFormat()

    ' Constants
    Const CANCELLED_COL As String = "B"
    Const PICKUP_DATE_COL_IDX As Integer = 7

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
    Dim ActSh as String

    LastR = Range("A1:A" & Range("A1").End(xlDown).Row).Rows.Count
    ActSh = ActiveSheet.Name

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
    
    ' delete only cancelled
    ' Delete rows out of date range
    For x = Cells(Cells.Rows.Count, PICKUP_DATE_COL_IDX).End(xlUp).Row To 2 Step -1
        If UCase(Range(CANCELLED_COL & x).Value) = UCase("Cancelled") Or _
           Cells(x, PICKUP_DATE_COL_IDX).Value < today Or _
           Cells(x, PICKUP_DATE_COL_IDX).Value > tmrow Then
            Cells(x, PICKUP_DATE_COL_IDX).EntireRow.Delete
        End If
    Next
    
    ' twick Notes Column later need to separate from cooridinaotr initials:
    ' Copy coordinator initials and notes
    ' Modify column P in place 
    concatCoordinatorAndNotes targetColumn := "B"

    ' Phone 
    Columns("O:O").Select
    Selection.Copy
    ' Phone 
    Range("R1").Select
    Selection.Insert Shift:=xlToRight

    ' P contained coordinator + notes. Remove it since it was copied to B 
    Columns("P:P").Select
    Selection.Delete Shift:=xlToLeft

    ' Delete member ID and DOB
    Columns("E:F").Select
    Selection.Delete Shift:=xlToLeft
    ' H contains origin 
    Columns("H:H").Select
    ' Insert 4 empty columns 
    Selection.Insert Shift:=xlToRight, CopyOrigin:=xlFormatFromLeftOrAbove
    Selection.Insert Shift:=xlToRight, CopyOrigin:=xlFormatFromLeftOrAbove
    Selection.Insert Shift:=xlToRight, CopyOrigin:=xlFormatFromLeftOrAbove
    Selection.Insert Shift:=xlToRight, CopyOrigin:=xlFormatFromLeftOrAbove
    ' P has notes column
    Columns("P:P").Select
    Selection.Delete Shift:=xlToLeft
    
    ' Wheelchair YN column
    highlightWheelChairColumns columnLetter := "N"

    Columns("E:E").Select
    Selection.NumberFormat = "m/d;@"
    Columns("F:G").Select
    Selection.Replace What:="AM", Replacement:="AM", LookAt:=xlPart, SearchOrder:=xlByRows, MatchCase:=False, SearchFormat:=False, ReplaceFormat:=False
    Selection.Replace What:="PM", Replacement:="PM", LookAt:=xlPart, SearchOrder:=xlByRows, MatchCase:=False, SearchFormat:=False, ReplaceFormat:=False
    Selection.NumberFormat = "h:mm;@"
    Columns("O:O").Select
    allightSelectionTo horizontalAlignment := xlLeft, verticalAlignment := xlCenter
    Columns("B:B").Select
    allightSelectionTo horizontalAlignment := xlLeft, verticalAlignment := xlCenter
    Columns("E:E").Select
    allightSelectionTo horizontalAlignment := xlLeft, verticalAlignment := xlCenter

    Cells.Select
    
    ActiveWorkbook.Worksheets(ActSh).Sort.SortFields.Clear
    ActiveWorkbook.Worksheets(ActSh).Sort.SortFields.Add Key _
        :=Range("A2:A649"), SortOn:=xlSortOnValues, Order:=xlAscending, _
        DataOption:=xlSortNormal
    With ActiveWorkbook.Worksheets(ActSh).Sort
        .SetRange Range("A1:W999")
        .Header = xlYes
        .MatchCase = False
        .Orientation = xlTopToBottom
        .SortMethod = xlPinYin
        .Apply
    End With
    Range("K1").Select
    ActiveCell.FormulaR1C1 = "XXXXX"
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
    allightSelectionTo horizontalAlignment := xlLeft, verticalAlignment := xlCenter
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
    Cells.Select
    ActiveWorkbook.Worksheets(ActSh).Sort.SortFields.Clear
    ActiveWorkbook.Worksheets(ActSh).Sort.SortFields.Add Key _
        :=Range("A1"), SortOn:=xlSortOnValues, Order:=xlAscending, DataOption:=xlSortNormal
    With ActiveWorkbook.Worksheets(ActSh).Sort
        .SetRange Range("A1:U649")
        .Header = xlYes
        .MatchCase = False
        .Orientation = xlTopToBottom
        .SortMethod = xlPinYin
        .Apply
    End With
    ActiveWorkbook.Worksheets(ActSh).Sort.SortFields.Clear
    ActiveWorkbook.Worksheets(ActSh).Sort.SortFields.Add Key _
        :=Range("B2:B649"), SortOn:=xlSortOnValues, Order:=xlAscending, _
        DataOption:=xlSortNormal
    With ActiveWorkbook.Worksheets(ActSh).Sort
        .SetRange Range("A1:U649")
        .Header = xlYes
        .MatchCase = False
        .Orientation = xlTopToBottom
        .SortMethod = xlPinYin
        .Apply
    End With
    ActiveWorkbook.Worksheets(ActSh).Sort.SortFields.Clear
    ActiveWorkbook.Worksheets(ActSh).Sort.SortFields.Add Key _
        :=Range("E2:E649"), SortOn:=xlSortOnValues, Order:=xlAscending, _
        DataOption:=xlSortNormal
    ActiveWorkbook.Worksheets(ActSh).Sort.SortFields.Add Key _
        :=Range("J2:J649"), SortOn:=xlSortOnValues, Order:=xlAscending, _
        DataOption:=xlSortNormal
    With ActiveWorkbook.Worksheets(ActSh).Sort
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
    unifyStreetNames rangeDef := "L:M"

    ' Delete column with original dates
    Columns("H:H").Select
    Selection.Cut
    Range("T1").Select
    Selection.Insert Shift:=xlToRight
    Columns("H:H").Select
    Selection.Insert Shift:=xlToRight, CopyOrigin:=xlFormatFromLeftOrAbove

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
    allightSelectionTo horizontalAlignment := xlLeft, verticalAlignment := xlCenter
    Range("A1").Select
    allightSelectionTo horizontalAlignment := xlCenter, verticalAlignment := xlCenter
    Columns("G:G").Select
    allightSelectionTo horizontalAlignment := xlRight, verticalAlignment := xlCenter
    Range("G1").Select
    allightSelectionTo horizontalAlignment := xlCenter, verticalAlignment := xlCenter
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
    Columns("U:X").Select
    Application.CutCopyMode = False
    Selection.ClearContents
    
    Range("B1").Select
    With Selection.Interior
        .Pattern = xlSolid
        .PatternColorIndex = xlAutomatic
        .ThemeColor = xlThemeColorDark1
        .TintAndShade = -0.249977111117893
        .PatternTintAndShade = 0
    End With
      
    ' formating for new notes/inititanl
    formatColumns

    ' Find last day cell and insert empty line between dates
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

private Sub concatCoordinatorAndNotes(targetColumn as String)
    Dim i As Long
    
    Range(targetColumn & "1").ClearContents
    Range(targetColumn & "1").Formula = "Coordinator"
    ' 16 == P column
    For i = Cells(Cells.Rows.Count, 16).End(xlUp).Row To 2 Step -1
        Range(targetColumn & i).Value = Range("P" & i).Value & "#" & Range("N" & i).Value
    Next
End Sub

private Sub highlightWheelChairColumns(columnLetter as String)
    Columns(columnLetter & ":" & columnLetter).Select
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
End Sub

Private Sub allightSelectionTo(horizontalAlignment as Integer, verticalAlignment as Integer)
    With Selection
        .HorizontalAlignment = horizontalAlignment
        .VerticalAlignment = verticalAlignment
        .WrapText = False
        .Orientation = 0
        .AddIndent = False
        .IndentLevel = 0
        .ShrinkToFit = False
        .ReadingOrder = xlContext
        .MergeCells = False
    End With
End Sub

Private Sub unifyStreetNames(rangeDef as String)
    Columns(rangeDef).Select
    Selection.Replace What:="STREET", Replacement:="ST", LookAt:=xlPart, _
        SearchOrder:=xlByRows, MatchCase:=False, SearchFormat:=False, _
        ReplaceFormat:=False
    Selection.Replace What:="AVENUE", Replacement:="AVE", LookAt:=xlPart, _
        SearchOrder:=xlByRows, MatchCase:=False, SearchFormat:=False, _
        ReplaceFormat:=False
    Selection.Replace What:="#*", Replacement:="", LookAt:=xlPart, _
        SearchOrder:=xlByRows, MatchCase:=False, SearchFormat:=False, _
        ReplaceFormat:=False
    Selection.Replace What:="/*", Replacement:="", LookAt:=xlPart, _
        SearchOrder:=xlByRows, MatchCase:=False, SearchFormat:=False, _
        ReplaceFormat:=False
    Selection.Replace What:=".,", Replacement:="", LookAt:=xlPart, _
        SearchOrder:=xlByRows, MatchCase:=False, SearchFormat:=False, _
        ReplaceFormat:=False
End Sub

Private Sub formatColumns
    ' REF_ID
    Columns("A:A").Select
    ' Coordinator
    Columns("B:B").Select
    setCalibriFont fontSize:=6, followTheme:=True
    ' LNAME
    Columns("C:C").Select
    ' FNAME
    Columns("D:D").Select
    ' PICKUP_DATE
    Columns("E:E").Select
    setCalibriFont fontSize:=6, followTheme:=True
    ' PickupTime*
    Columns("F:F").Select
    ' --- Empty --- 
    Columns("G:G").Select
    ' Appt_time
    Columns("H:H").Select
    ' --- Empty --- 
    Columns("I:I").Select
    ' --- Empty --- 
    Columns("J:J").Select
    ' --- Empty --- 
    Columns("K:K").Select
    ' --- Empty --- 
    Columns("L:L").Select
    ' Origin
    Columns("M:M").Select
    ' Destination
    Columns("N:N").Select
    ' WheelChair_YesNo
    Columns("O:O").Select
    setCalibriFont fontSize:=6, followTheme:=True
    ' TP
    Columns("P:P").Select
    setCalibriFont fontSize:=6, followTheme:=True
    ' Telephone
    Columns("Q:Q").Select
    ' Notes
    Columns("R:R").Select
    ' --- Empty --- 
    Columns("S:S").Select
    ' Pickup_timeORIGINAL
    Columns("T:T").Select
    ' Bold header 
    Rows("1:1").EntireRow.Select
    setCalibriFont fontSize:=10, followTheme:=False
    With Selection.Interior
        .Pattern = xlSolid
        .PatternColorIndex = xlAutomatic
        .ThemeColor = xlThemeColorDark1
        .TintAndShade = -0.249977111117893
        .PatternTintAndShade = 0
    End With
    Selection.Font.Bold = True
    ' Adjust widths
    Cells.Select
    Cells.EntireColumn.AutoFit
End Sub 