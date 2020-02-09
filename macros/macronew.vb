Sub DailyCCHPNewFormat()

    ' Constants
    Const CANCELLED_COL As String = "B"
    Const PICKUP_DATE_COL_IDX As Integer = 7
    Const CANCELED As Integer = 1

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
    Dim ActSh As String
   
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

    ' Adjust all columns values and remove not needed data from cells
    cleanUpColumnsData
    ' Move all the columns around to make it compatible with old format
    convertToOldFormat

    ' Copy notes
    copyPaste fromColumns:="N:N", toColumns:="AM:AM", special:=False

    ' delete only cancelled
    ' Delete rows out of date range
    For x = Cells(Cells.Rows.Count, PICKUP_DATE_COL_IDX).End(xlUp).Row To 2 Step -1
        If UCase(Range(CANCELLED_COL & x).Value) = CANCELED Or _
           Cells(x, PICKUP_DATE_COL_IDX).Value < today Or _
           Cells(x, PICKUP_DATE_COL_IDX).Value > tmrow Then
            Cells(x, PICKUP_DATE_COL_IDX).EntireRow.Delete
        End If
    Next
   
    ' twick Notes Column later need to separate from cooridinaotr initials:
    ' Copy coordinator initials and notes
    ' Modify column P in place
    concatCoordinatorAndNotes targetColumn:="B"

    ' Phone
    Columns("O:O").Select
    Selection.Copy
    ' Phone
    Range("R1").Select
    Selection.Insert Shift:=xlToRight

    deleteColumn columnDef:="P:P"   ' P contained coordinator + notes. Remove it since it was copied to B
    ' keep member ID alive
    deleteColumn columnDef:="F:F"   ' Delete DOB
    ' deleteColumn columnDef := "E:E" ' Delete DOB

    ' I contains origin
    Columns("I:I").Select
    ' Insert 4 empty columns
    Selection.Insert Shift:=xlToRight, CopyOrigin:=xlFormatFromLeftOrAbove
    Selection.Insert Shift:=xlToRight, CopyOrigin:=xlFormatFromLeftOrAbove
    Selection.Insert Shift:=xlToRight, CopyOrigin:=xlFormatFromLeftOrAbove
    Selection.Insert Shift:=xlToRight, CopyOrigin:=xlFormatFromLeftOrAbove

    deleteColumn columnDef:="Q:Q"   ' Q has notes column
   
    Cells.Select
   
    ActiveWorkbook.Worksheets(ActSh).Sort.SortFields.Clear
    ActiveWorkbook.Worksheets(ActSh).Sort.SortFields.Add Key:=Range("A2:A649"), SortOn:=xlSortOnValues, Order:=xlAscending, DataOption:=xlSortNormal
    With ActiveWorkbook.Worksheets(ActSh).Sort
        .SetRange Range("A1:W999")
        .Header = xlYes
        .MatchCase = False
        .Orientation = xlTopToBottom
        .SortMethod = xlPinYin
        .Apply
    End With

    copyPaste fromColumns:="G:G", toColumns:="T:T", special:=False

    Range("J3").Select
    ActiveCell.FormulaR1C1 = "=IF((AND(OR(RC[-9]-R[-1]C[-9]=1,RC[-4]=R[-1]C[-4]),RC[-3]="""",RC[-6]=R[-1]C[-6])),R[-1]C[-2]+TIME(1,30,0),"""")"
    Range("J3").Select
    Selection.AutoFill Destination:=Range("J3:J150"), Type:=xlFillDefault

    copyPaste fromColumns:="J:J", toColumns:="I:I", special:=True
    clearColumn columnDef:="J:J"

    Columns("I:I").Select
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

    Range("J2").Select
    ActiveCell.FormulaR1C1 = "=IF(RC[-3]<>"""",TEXT(RC[-3],""HH:MM""),CONCATENATE(TEXT(RC[-1],""HH:MM""),""_""))"
    Range("J2").Select
    Selection.AutoFill Destination:=Range("J2:J150"), Type:=xlFillDefault
   
    copyPaste fromColumns:="G:G", toColumns:="L:L", special:=False
    copyPaste fromColumns:="J:J", toColumns:="K:K", special:=True
    copyPaste fromColumns:="K:K", toColumns:="G:G", special:=False

    clearColumn columnDef:="I:J"
    clearColumn columnDef:="K:K"

    Columns("G:G").Select
    Selection.FormatConditions.Add Type:=xlTextString, String:="_", TextOperator:=xlContains
    Selection.FormatConditions(Selection.FormatConditions.Count).SetFirstPriority
    With Selection.FormatConditions(1).Font
        .TintAndShade = 0
    End With
    Selection.FormatConditions(1).StopIfTrue = False
    Columns("I:I").Select
    With Selection.Interior
        .Pattern = xlNone
        .TintAndShade = 0
        .PatternTintAndShade = 0
    End With
    Selection.FormatConditions.Delete

    copyPaste fromColumns:="L:L", toColumns:="I:I", special:=False
    clearColumn columnDef:="L:L"
   
    Columns("I:I").Select
    With Selection.Interior
        .PatternColor = 12632256
        .Color = 65535
        .TintAndShade = 0
        .PatternTintAndShade = 0
    End With
    Columns("O:O").Select
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
   
    Range("K2").Select
    ActiveCell.FormulaR1C1 = "=IF(RC[9]<>"""",RC[9],TIMEVALUE(SUBSTITUTE(RC[-4],""_"","""")))"
    Range("K2").Select
    Selection.AutoFill Destination:=Range("K2:K139"), Type:=xlFillDefault
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
        :=Range("F2:F649"), SortOn:=xlSortOnValues, Order:=xlAscending, _
        DataOption:=xlSortNormal
    ActiveWorkbook.Worksheets(ActSh).Sort.SortFields.Add Key _
        :=Range("K2:K649"), SortOn:=xlSortOnValues, Order:=xlAscending, _
        DataOption:=xlSortNormal
    With ActiveWorkbook.Worksheets(ActSh).Sort
        .SetRange Range("A1:U649")
        .Header = xlYes
        .MatchCase = False
        .Orientation = xlTopToBottom
        .SortMethod = xlPinYin
        .Apply
    End With
    clearColumn columnDef:="K:K"
    clearColumn columnDef:="Q:R"
    clearColumn columnDef:="T:T"
   
    ' clean all leftovers after last detail line
    numofrows = Cells(Rows.Count, "a").End(xlUp).Row
    Rows((numofrows + 1) & ":200").ClearContents
    Columns("P:P").Select
    Selection.FormatConditions.Add Type:=xlCellValue, Operator:=xlGreater, Formula1:="=2"
    Selection.FormatConditions(Selection.FormatConditions.Count).SetFirstPriority
    With Selection.FormatConditions(1).Interior
        .PatternColorIndex = xlAutomatic
        .Color = 3381759
        .TintAndShade = 0
    End With
    Selection.FormatConditions(1).StopIfTrue = False

    ' Delete column with original dates
    Columns("I:I").Select
    Selection.Cut
    Range("U1").Select
    Selection.Insert Shift:=xlToRight
    Columns("I:I").Select
    Selection.Insert Shift:=xlToRight, CopyOrigin:=xlFormatFromLeftOrAbove

    Application.ScreenUpdating = False
    Worksheets(ActSh).Select
    For Each r In Worksheets(ActSh).UsedRange.Rows
        n = r.Row
        If Worksheets(ActSh).Cells(n, 15) = "Must" Then
            Range("I" & n & ":O" & n).Select
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
    Range(Columns(17), Columns(18)).Select
    Selection.Delete Shift:=xlToLeft
   
    Columns("H:H").Select
    Selection.Insert Shift:=xlToRight, CopyOrigin:=xlFormatFromLeftOrAbove
    Columns("S:S").Select
    Selection.Insert Shift:=xlToRight, CopyOrigin:=xlFormatFromLeftOrAbove
    Columns("R:R").Select
    Selection.Replace What:="(415) ", Replacement:="", LookAt:=xlPart, _
        SearchOrder:=xlByRows, MatchCase:=False, SearchFormat:=False, _
        ReplaceFormat:=False

    ' format with _ in column G
    ' Find all the cell F with "_" and format font
    Application.ScreenUpdating = False
    Worksheets(ActiveSheet.Name).Select
    For Each r In Worksheets(ActiveSheet.Name).UsedRange.Rows
        n = r.Row
        If InStr(Cells(n, 7).Text, "_") Then
            Range("G" & n & ":G" & n).Select
            With Selection.Interior
                Selection.Font.Size = 8
                .Color = 49407
                Selection.horizontalAlignment = xlLeft
            End With
        Else
            Range("G" & n & ":G" & n).Select
            With Selection.Interior
                Selection.Font.Bold = True
                Selection.horizontalAlignment = xlRight
            End With
        End If
    Next r

    Application.ScreenUpdating = True

    ' Split coordinator and notes back and place them appropriate columns ...
    splitCoordinatorAndNotes srcCol:="B", coordinatorCol:="B", notesCol:="S"
   
    ' Find last day cell and insert empty line between dates
    r = Application.Match(CLng(tmrow), Range("F1:F100"), 0)
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
       MsgBox "Can not find next date " & tmrow
    End If

TCEnd:
 
    formatColumns
    Range("A1").Select
    MsgBox ("Completed OK" & vbNewLine & "Red time is calculated + 1.5 hrs from appointment time" & vbNewLine & vbNewLine & " Don't forget to Save As this file ")
End Sub

Private Sub cleanUpColumnsData()
    Dim timeValStr As String

    ' Delete prefix from tracking number
    Columns("A:A").Select
    ' This is important since there is a formula that substracts tracking numbers. They have to be numbers without prefix
    Selection.Replace What:="FL", Replacement:=" ", LookAt:=xlPart, SearchOrder:=xlByRows, MatchCase:=False, SearchFormat:=False, ReplaceFormat:=False
    ' Ride cancel
    Columns("F:F").Select
    Selection.Replace What:="0", Replacement:="", LookAt:=xlPart, SearchOrder:=xlByRows, MatchCase:=False, SearchFormat:=False, ReplaceFormat:=False
    ' Time columns
    ' Remove W/C from time column
    Columns("H:I").Select
    Selection.Replace What:="W/C", Replacement:="", LookAt:=xlPart, SearchOrder:=xlByRows, MatchCase:=False, SearchFormat:=False, ReplaceFormat:=False
    Selection.Replace What:="NONE", Replacement:="", LookAt:=xlPart, SearchOrder:=xlByRows, MatchCase:=False, SearchFormat:=False, ReplaceFormat:=False
    Selection.Replace What:="N/A", Replacement:="", LookAt:=xlPart, SearchOrder:=xlByRows, MatchCase:=False, SearchFormat:=False, ReplaceFormat:=False
    Selection.Replace What:="NA", Replacement:="", LookAt:=xlPart, SearchOrder:=xlByRows, MatchCase:=False, SearchFormat:=False, ReplaceFormat:=False
    Selection.Replace What:=";", Replacement:=":", LookAt:=xlPart, SearchOrder:=xlByRows, MatchCase:=False, SearchFormat:=False, ReplaceFormat:=False
    Selection.Replace What:="AN", Replacement:="AM", LookAt:=xlPart, SearchOrder:=xlByRows, MatchCase:=False, SearchFormat:=False, ReplaceFormat:=False
    ' For some reason Excel cannot recognize text as time. Force convert it
    rowsCnt = Cells(Cells.Rows.Count, 1).End(xlUp).Row
    Dim convertH As Boolean
    Dim convertI As Boolean
    convertH = True
    convertI = True
    For i = 2 To rowsCnt
        convertH = convertH And convertTime(Range("H" & i))
        convertI = convertI And convertTime(Range("I" & i))
    Next i
    If Not (convertH And convertI) Then
        Err.Raise vbObjectError + 1000, "DaylyCCHPNewFormat", _
        "One or more time values (Date of Service, Appointment Pick-up Time) contain invalid values. Please find cells marked by red color, fix values and run macros again", "", 0
    End If
    Selection.NumberFormat = "h:mm;@"
    ' Wheelchair
    Columns("L:L").Select
    Selection.Replace What:="1", Replacement:="Must", LookAt:=xlPart, SearchOrder:=xlByRows, MatchCase:=False, SearchFormat:=False, ReplaceFormat:=False
    Selection.Replace What:="0", Replacement:="", LookAt:=xlPart, SearchOrder:=xlByRows, MatchCase:=False, SearchFormat:=False, ReplaceFormat:=False
    highlightWheelChairColumns columnLetter:="L"
    ' Streets
    unifyStreetNames rangeDef:="J:K"
End Sub

Private Function convertTime(rng As Range) As Boolean
    On Error GoTo ErrorHandler
        timeStr = rng.Text
        Dim timeVal As Date
        If timeStr <> "" Then
            timeVal = TimeValue(timeStr)
            rng.ClearContents
            rng.Value = timeVal
        End If
        convertTime = True
        rng.Interior.Color = xlNone
        Exit Function
ErrorHandler:
        rng.Interior.Color = RGB(255, 0, 0)
        convertTime = False
        Exit Function
End Function

Private Sub convertToOldFormat()
    '--- Names changes
    ' TrackingNumber <-> REF_ID
    ' MemberNumber <-> MemberID
    ' LastName <-> LNAME
    ' FirstName <-> FNAME
    ' OpenedByName <-> Coordinator_Initials
    ' Ride Cancellation <-> Cancelled
    ' Date of Service <-> PICKUP_DATE
    ' Appointment Pick-up Time <-> Pickup_time
    ' Appointment Scheduled Time <-> Appt_time
    ' Pick Up Location <-> Origin
    ' Destination <-> Destination
    ' Wheelchair <-> WheelChair_YesNo
    ' Number of Passengers <-> Total_Passengers
    ' Notes <-> Notes
    ' Primary Contact Number <-> Telephone
    ' Date of Birth <-> DOB

    ' Autofilter causes errors when data is copied and pasted
    If ActiveSheet.AutoFilterMode Then
        ActiveSheet.AutoFilterMode = False
    End If
    '--- Column changes
    ' E <- B - MemberID
    ' B <- F - Cancelled
    ' F <- P - DOB
    ' P <- E - Coordinator_Initials
    copyPaste fromColumns:="E:E", toColumns:="CE:CE", special:=False
    copyPaste fromColumns:="B:B", toColumns:="CB:CB", special:=False
    copyPaste fromColumns:="F:F", toColumns:="CF:CF", special:=False
    copyPaste fromColumns:="P:P", toColumns:="CP:CP", special:=False
    clearColumn columnDef:="E:E"
    clearColumn columnDef:="B:B"
    clearColumn columnDef:="F:F"
    clearColumn columnDef:="P:P"
    copyPaste fromColumns:="CB:CB", toColumns:="E:E", special:=False
    copyPaste fromColumns:="CF:CF", toColumns:="B:B", special:=False
    copyPaste fromColumns:="CP:CP", toColumns:="F:F", special:=False
    copyPaste fromColumns:="CE:CE", toColumns:="P:P", special:=False
    ' Set dates columns format
    Columns("F:G").Select
    Selection.NumberFormat = "m/d;@"
End Sub

Private Sub unifyStreetNames(rangeDef As String)
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

Private Sub convertFormulasToValuesInSelection()
    Dim rng As Range
    For Each rng In Selection
        If rng.HasFormula Then
            rng.Formula = rng.Value
        End If
    Next rng
End Sub

Private Sub concatCoordinatorAndNotes(targetColumn As String)
    Dim i As Long
   
    Range(targetColumn & "1").ClearContents
    Range(targetColumn & "1").Formula = "Coordinator"
    ' 16 == P column
    For i = Cells(Cells.Rows.Count, 16).End(xlUp).Row To 2 Step -1
        Range(targetColumn & i).Value = Range("P" & i).Value & "#" & Range("N" & i).Value
    Next
End Sub

Private Sub splitCoordinatorAndNotes(srcCol As String, coordinatorCol As String, notesCol As String)
    Dim i As Long
    Dim rowsCnt As Long
    Dim concatVal As String
    Dim coordinator As String
    Dim notes As String

    rowsCnt = Cells(Cells.Rows.Count, 1).End(xlUp).Row
    For i = 2 To rowsCnt
        concatVal = Range(srcCol & i).Value
        coordinator = Left(concatVal, InStr(concatVal, "#") - 1)
        notes = Right(concatVal, Len(concatVal) - InStr(concatVal, "#"))
        Range(coordinatorCol & i).ClearContents
        Range(coordinatorCol & i).Value = coordinator
        Range(notesCol & i).ClearContents
        Range(notesCol & i).Value = notes
    Next i
End Sub

Private Sub highlightWheelChairColumns(columnLetter As String)
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

Private Sub allightSelectionTo(horizontalAlignment As Integer, verticalAlignment As Integer)
    With Selection
        .horizontalAlignment = horizontalAlignment
        .verticalAlignment = verticalAlignment
        .WrapText = False
        .Orientation = 0
        .AddIndent = False
        .IndentLevel = 0
        .ShrinkToFit = False
        .ReadingOrder = xlContext
        .MergeCells = False
    End With
End Sub

Private Sub formatColumns()
    Range("A1").FormulaR1C1 = "REF_ID"
    Columns("A:A").Select
    allightSelectionTo horizontalAlignment:=xlLeft, verticalAlignment:=xlCenter
   
    Range("B1").FormulaR1C1 = "Coordinator"
    Columns("B:B").Select
    setCalibriFont fontSize:=6, followTheme:=True
    allightSelectionTo horizontalAlignment:=xlLeft, verticalAlignment:=xlBottom

    Range("C1").FormulaR1C1 = "LNAME"
    Columns("C:C").Select
    allightSelectionTo horizontalAlignment:=xlLeft, verticalAlignment:=xlCenter

    Range("D1").FormulaR1C1 = "FNAME"
    Columns("D:D").Select
    allightSelectionTo horizontalAlignment:=xlLeft, verticalAlignment:=xlCenter

    Range("E1").FormulaR1C1 = "Member ID"
    Columns("E:E").Select
    allightSelectionTo horizontalAlignment:=xlLeft, verticalAlignment:=xlCenter

    Range("F1").FormulaR1C1 = "PICKUP_DATE"
    Columns("F:F").Select
    setCalibriFont fontSize:=6, followTheme:=True
    Selection.NumberFormat = "m/d;@"
    allightSelectionTo horizontalAlignment:=xlLeft, verticalAlignment:=xlBottom

    Range("G1").FormulaR1C1 = "PickupTime*"
    Columns("G:G").Select
    allightSelectionTo horizontalAlignment:=xlRight, verticalAlignment:=xlCenter
    Selection.NumberFormat = "General"

    ' --- Empty ---
    ' Columns("H:H").Select

    Range("I1").FormulaR1C1 = "Appt_time"
    Columns("I:I").Select
    allightSelectionTo horizontalAlignment:=xlRight, verticalAlignment:=xlCenter
    Selection.NumberFormat = "h:mm;@"

    ' --- Empty ---
    ' Columns("J:J").Select
    ' --- Empty ---
    ' Columns("K:K").Select
    ' --- Empty ---
    ' Columns("L:L").Select
    ' --- Empty ---
    ' Columns("M:M").Select

    Range("N1").FormulaR1C1 = "Origin"
    Columns("N:N").Select
    allightSelectionTo horizontalAlignment:=xlLeft, verticalAlignment:=xlCenter

    Range("O1").FormulaR1C1 = "Destination"
    Columns("O:O").Select
    allightSelectionTo horizontalAlignment:=xlLeft, verticalAlignment:=xlCenter

    Range("P1").FormulaR1C1 = "WheelChair_YesNo"
    Columns("P:P").Select
    setCalibriFont fontSize:=6, followTheme:=True
    allightSelectionTo horizontalAlignment:=xlLeft, verticalAlignment:=xlCenter

    Range("Q1").FormulaR1C1 = "TP"
    Columns("Q:Q").Select
    setCalibriFont fontSize:=6, followTheme:=True
    allightSelectionTo horizontalAlignment:=xlLeft, verticalAlignment:=xlCenter

    Range("R1").FormulaR1C1 = "Telephone"
    Columns("R:R").Select
    allightSelectionTo horizontalAlignment:=xlLeft, verticalAlignment:=xlCenter

    Range("S1").FormulaR1C1 = "Notes"
    Columns("S:S").Select
    allightSelectionTo horizontalAlignment:=xlLeft, verticalAlignment:=xlCenter

    ' --- Empty ---
    ' Columns("T:T").Select

    Range("U1").FormulaR1C1 = "Pickup_timeORIGINAL"
    Columns("U:U").Select
    Selection.NumberFormat = "h:mm;@"
    allightSelectionTo horizontalAlignment:=xlRight, verticalAlignment:=xlCenter

    '-------------------------------------------------------------------------------
    ' Bold header
    Rows("1:1").EntireRow.Select
    Selection.FormatConditions.Delete
    setCalibriFont fontSize:=10, followTheme:=False
    With Selection.Interior
        .Pattern = xlSolid
        .PatternColorIndex = xlAutomatic
        .ThemeColor = xlThemeColorDark1
        .TintAndShade = -0.249977111117893
        .PatternTintAndShade = 0
    End With
    Selection.Font.Bold = True
    allightSelectionTo horizontalAlignment:=xlCenter, verticalAlignment:=xlCenter
    ' Adjust widths
    Cells.Select
    Cells.EntireColumn.AutoFit
End Sub

Private Sub copyPaste(fromColumns As String, toColumns As String, special As Boolean)
    Columns(fromColumns).Select
    Selection.Copy
    Columns(toColumns).Select
    If special Then
        Selection.PasteSpecial Paste:=xlPasteValues, Operation:=xlNone, SkipBlanks:=False, Transpose:=False
    Else
        ActiveSheet.Paste
    End If
    Application.CutCopyMode = False
End Sub

Private Sub clearColumn(columnDef As String)
    Columns(columnDef).Select
    Selection.ClearContents
End Sub

Private Sub deleteColumn(columnDef As String)
    Columns(columnDef).Select
    Selection.Delete Shift:=xlToLeft
End Sub