;//////////////////////////////////////////////////////////////////////////////
;----------------------- WRESTLING MPIRE 2008: CAREER ISSUES ------------------
;//////////////////////////////////////////////////////////////////////////////

;//////////////////////////////////////////////////////////////////////////////
;-------------------------- 27. CONFIRM UNIVERSE ------------------------------
;//////////////////////////////////////////////////////////////////////////////
Function ConfirmUniverse()
;frame rating
timer=CreateTimer(30)
;MAIN LOOP
foc=1 : oldfoc=foc
go=0 : gotim=0 : keytim=20
While go=0

 Cls
 frames=WaitTimer(timer)
 For framer=1 To frames
	
	;timers
	keytim=keytim-1
	If keytim<1 Then keytim=0
	
	;PORTAL
    gotim=gotim+1
	If gotim>20 And keytim=0
	 ;cancel
	 If KeyDown(1) Then go=-1
	 ;proceed
	 If KeyDown(28) Or ButtonPressed() Or MouseDown(1)
	  If foc=3 Then go=-1 Else go=1
	 EndIf
	EndIf
	;music
	ManageMusic(-1) 
	
	;CONFIGURATION 
	If gotim>20 And keytim=0
	 ;highlight option
	 If KeyDown(200) Or JoyYDir()=-1 Then foc=foc-1 : PlaySound sMenuSelect : keytim=6
	 If KeyDown(208) Or JoyYDir()=1 Then foc=foc+1 : PlaySound sMenuSelect : keytim=6   
	EndIf  
	;limits
	If foc<1 Then foc=3
	If foc>3 Then foc=1     
	
 UpdateWorld
 Next
 RenderWorld 1

 ;DISPLAY
 DrawImage gBackground,rX#(400),rY#(300)
 DrawImage gLogo(1),rX#(400),rY#(135)
 ;DrawImage gMDickie,rX#(400),rY#(530) 
 ;message
 SetFont font(5)
 y=rY#(325)
 Outline("This folder already has a universe on file.",rX#(400),y-28,0,0,0,255,255,255)
 Outline("Would you like to insert a new character",rX#(400),y,0,0,0,255,255,255)
 Outline("into that one or start from scratch?",rX#(400),y+28,0,0,0,255,255,255) 
 ;options
 DrawOption(1,rX#(400),rY#(425),"INHERIT UNIVERSE","")  
 DrawOption(2,rX#(400),rY#(465),"REPLACE UNIVERSE","") 
 DrawOption(3,rX#(400),rY#(560),"<<< BACK <<<","")   
 ;cursor
 If foc<>oldfoc Then oldfoc=foc : PlaySound sMenuSelect  
 DrawImage gCursor,MouseX(),MouseY()

 Flip
 ;screenshot (F12)
 If KeyHit(88) Then Screenshot()

Wend
;proceed
If go>0 Then PlaySound sMenuGo Else PlaySound sMenuBack
FreeTimer timer
;re-use slot
If go=1 And foc=1
 Loader("Please Wait","Generating Career")
 SwitchSlot(targetSlot)
 cupSlot=3+slot
 gamInherit=1
 GenerateCareer() 
 slotActive(slot)=1
 SaveUniverse()
 SaveProgress(slot)
 SaveWorld(slot)
 SaveChars(slot)
 SavePhotos(slot) 
 editChar=gamChar
 screen=51 : screenAgenda=5
EndIf
;reset slot
If go=1 And foc=2
 Loader("Please Wait","Generating Career")
 SwitchSlot(0)
 slot=targetSlot
 cupSlot=3+slot 
 gamInherit=0
 GenerateCareer()  
 slotActive(slot)=1
 SaveUniverse()
 SaveProgress(slot)
 SaveWorld(slot)
 SaveChars(slot)
 SavePhotos(slot)  
 editChar=gamChar
 screen=51 : screenAgenda=5
EndIf
;cancel
If go=-1 Then slotActive(slot)=0 : screen=10
End Function

;//////////////////////////////////////////////////////////////////////////////
;------------------------------ 20. CALENDAR ----------------------------------
;//////////////////////////////////////////////////////////////////////////////
Function Calendar()
;force tournament date if not resolved
If TournamentStatus(gamChar)>0
 If gamSchedule(gamDate-1)=3 Or gamDate=1 Then gamSchedule(gamDate)=3
EndIf
;monitor schedule
fed=charFed(gamChar)
For date=gamDate To 48
 ;confirm tournament details (withdraw if eliminated)
 If gamSchedule(date)=3
  gamOpponent(date)=0 : gamMatch(date)=2 : gamGimmick(date)=0
  For b=1 To 32
   If cupBracket(cupSlot,b,1)=gamChar And cupResult(cupSlot,b)>0 And cupResult(cupSlot,b)<>1 Then gamSchedule(date)=0
   If cupBracket(cupSlot,b,2)=gamChar And cupResult(cupSlot,b)>0 And cupResult(cupSlot,b)<>2 Then gamSchedule(date)=0
  Next
 EndIf
 ;forecast inter-promotional matches
 If gamSchedule(date)=4 And gamRivalFed(date)=0 Then gamMatch(date)=14 : gamGimmick(date)=0 
 If gamSchedule(date)=4 And gamRivalFed(date)>0
  gamMatch(date)=3 : gamGimmick(date)=0
  If TitleHolder(gamChar,3) And fedChampTag(gamRivalFed(date),1)>0 Then gamOpponent(date)=fedChampTag(gamRivalFed(date),1)
  If TitleHolder(gamChar,2) And fedChampInter(gamRivalFed(date))>0 Then gamOpponent(date)=fedChampInter(gamRivalFed(date)) 
  If TitleHolder(gamChar,1) And fedChampWorld(gamRivalFed(date))>0 Then gamOpponent(date)=fedChampWorld(gamRivalFed(date))
;new belts
If TitleHolder(gamChar,5) And fedChampWomens(gamRivalFed(date))>0 Then gamOpponent(date)=fedChampWomens(gamRivalFed(date))
If TitleHolder(gamChar,6) And fedChampUS(gamRivalFed(date))>0 Then gamOpponent(date)=fedChampUS(gamRivalFed(date))
If TitleHolder(gamChar,7) And fedChampTV(gamRivalFed(date))>0 Then gamOpponent(date)=fedChampTV(gamRivalFed(date))
;end 
  If TitleHolder(gamChar,0)=0 And TitleHolder(gamOpponent(date),0)>0 Then gamOpponent(date)=0
  If TitleHolder(gamChar,0)>0 And TitleHolder(gamOpponent(date),0)=0 Then gamOpponent(date)=0
 EndIf
Next
;confirm today's tournament fixture
If gamSchedule(gamDate)=3
 For b=1 To 32
  If cupResult(cupSlot,b)=0
   If cupBracket(cupSlot,b,1)=gamChar Then gamOpponent(gamDate)=cupBracket(cupSlot,b,2) : cupFoc(cupSlot)=b
   If cupBracket(cupSlot,b,2)=gamChar Then gamOpponent(gamDate)=cupBracket(cupSlot,b,1) : cupFoc(cupSlot)=b
  EndIf
 Next
EndIf
;prepare today's event
If gamSchedule(gamDate)>0
 fed=charFed(gamChar)
 If fed=<6 And gamOpponent(gamDate)>0 And charFed(gamOpponent(gamDate))<>fed Then fed=0
 If gamSchedule(gamDate)=>4 And gamSchedule(gamDate)=<6 Then fed=0
 If gamOpponent(gamDate)=0 Then gamOpponent(gamDate)=AssignOpponent(gamChar,gamDate,0)
 If gamMatch(gamDate)=0 Then AssignMatch(gamDate)
 GetMatchRules(gamMatch(gamDate))
 If matchPreset=>15 And matchPreset=<17 And gamGimmick(gamDate)=2 Then gamGimmick(gamDate)=0 ;prevent cage in rumbles
 If matchPreset=18 Then gamGimmick(gamDate)=2 ;guarantee cage in escape challenge
 If optFX=0 And gamGimmick(gamDate)=6 Then gamGimmick(gamDate)=0 ;no explosives if no FX
 AddGimmick(gamGimmick(gamDate))
 If charPartner(gamChar)>0 And matchTeams=>0 ;team format
  If no_wrestlers<4 Then no_wrestlers=4
  If matchTeams=<0
   If matchRules>0 Then matchTeams=2 Else matchTeams=1
  EndIf 
  If gamGimmick(gamDate)=>6 And gamGimmick(gamDate)=<8 Then matchTeams=1
  If optReferees=<1 Then no_refs=0
  no_plays=no_wrestlers+no_refs
 EndIf
 If gamSchedule(gamDate)=4 And gamRivalFed(gamDate)=0 ;6-man inter-promotional 
  matchTeams=0 : no_wrestlers=6 : no_refs=1
  If optReferees=0 Then no_refs=0
  If optReferees=3 Then no_refs=2
  no_plays=no_wrestlers+no_refs 
 EndIf
 ConstructMatch()
 AssignPromo()
 GenerateArena(fed,gamSchedule(gamDate),1)
 arenaPreset=gamVenue(gamDate) 
 If optFog>0 Then arenaAtmos=gamAtmos(gamDate) Else arenaAtmos=0
 If gamAttendance(gamDate)=0 Then gamAttendance(gamDate)=GenerateAttendance()
 If gamGimmick(gamDate)=11 Then gamAttendance(gamDate)=0 : arenaAttendance=0
 arenaAttendance=TranslateAttendance(gamAttendance(gamDate)) 
 arenaRopes=gamRopes(gamDate)
 arenaPads=gamPads(gamDate)
 arenaCanvas=gamCanvas(gamDate)
 arenaApron=gamApron(gamDate)
 arenaMatting=gamMatting(gamDate) 
EndIf
;frame rating
timer=CreateTimer(30)
;MAIN LOOP
scroll=gamScroll : scrollTim=0 : scrollSpeed=5
foc=9 : oldfoc=foc : subfoc=0
go=0 : gotim=0 : keytim=20
While go=0

 Cls
 frames=WaitTimer(timer)
 For framer=1 To frames
	
	;timers
	keytim=keytim-1
	If keytim<1 Then keytim=0
	
	;PORTAL
    gotim=gotim+1
	If gotim>20 And keytim=0
	 ;leave
	 If KeyDown(1) Then go=-1
	 ;proceed
	 If KeyDown(28) Or ButtonPressed() Or (MouseDown(1) And MouseY()>rY#(300))
	  If foc=10 Then go=-1 Else go=1
	 EndIf
	EndIf
	;music
	ManageMusic(-1) 
	
	;CONFIGURATION 
	If gotim>20 And keytim=0
	 ;highlight option
	 If KeyDown(200) Or JoyYDir()=-1
	  newFoc=foc-1
	  If foc=1 Or foc=4 Then newFoc=foc
	  If foc=7 Then newFoc=3  
	  foc=newFoc : keytim=5
	 EndIf
	 If KeyDown(208) Or JoyYDir()=1
	  newFoc=foc+1
	  If foc=3 Or foc=6 Then newFoc=7
	  If foc=10 Then newFoc=foc
	  foc=newFoc : keytim=5
	 EndIf
	 If (KeyDown(203) Or JoyXDir()=-1) And foc=>1 And foc=<6
	  If foc=>4 And foc=<6 Then newFoc=foc-3 Else newFoc=foc
	  foc=newFoc : keytim=5
	 EndIf
	 If (KeyDown(205) Or JoyXDir()=1) And foc=>1 And foc=<6
	  If foc=>1 And foc=<3 Then newFoc=foc+3 Else newFoc=foc
	  foc=newFoc : keytim=5
	 EndIf
	 ;improve bank balance
	 If KeyDown(48)
	  If KeyDown(12) Then charBank(gamChar)=charBank(gamChar)-1000 : PlaySound sCash : keytim=5
	  If KeyDown(13) Then charBank(gamChar)=charBank(gamChar)+1000 : PlaySound sCash : keytim=5
	 EndIf
	EndIf  
	;limits
	If foc<1 Then foc=10
	If foc>10 Then foc=1 
	
	;SCROLLING
	;aim for current
	scrollDelay=50
	If MouseY()<rY#(185)-45 Or MouseY()>rY#(185)+45 Or scrollTim>scrollDelay/2 Then scrollTim=scrollTim-1
	If scrollTim<0 Then scrollTim=0
	If scrollTim=0 Then scrollTarget=-((GetMonth(gamDate)-1)*255)
	If MouseDown(1)=0 Then scrollSpeed=scrollSpeed+1  
	If scrollSpeed>100 Then scrollSpeed=100
	;override with browsing
	;If KeyDown(203)
	 ;If scrollSpeed<5 Then scrollSpeed=5
	 ;scrollTarget=scrollTarget+scrollSpeed : scrollTim=scrollDelay 
	;EndIf
	;If KeyDown(205)
	 ;If scrollSpeed<5 Then scrollSpeed=5
	 ;scrollTarget=scrollTarget-scrollSpeed : scrollTim=scrollDelay
	;EndIf
	If MouseY()>rY#(185)-45 And MouseY()<rY#(185)+45 And MouseDown(1)
	 If scrollTim<scrollDelay-1 Then void=MouseXSpeed()
	 scroll=scroll+MouseXSpeed()
	 scrollSpeed=0 : scrollTim=scrollDelay 
	EndIf
	If scrollTim>0 And scrollTim<scrollDelay Then scrollSpeed=0
	;pursue target  
	If scrollTarget>0 Then scrollTarget=0
	If scrollTarget<-(11*255) Then scrollTarget=-(11*255)  
	If scroll<scrollTarget Then scroll=scroll+scrollSpeed
	If scroll>scrollTarget Then scroll=scroll-scrollSpeed 
	If scroll=>scrollTarget-scrollSpeed And scroll=<scrollTarget+scrollSpeed Then scroll=scrollTarget  
	;limits
	If scroll>0 Then scroll=0
	If scroll<-(11*255) Then scroll=-(11*255)  
	If scroll=scrollTarget And scrollTim<scrollDelay Then scrollSpeed=0
	
 UpdateWorld
 Next
 RenderWorld 1

 ;DISPLAY
 DrawImage gBackground,rX#(400),rY#(300)
 DrawImage gFed(charFed(gamChar)),rX#(400),rY#(60)
 ;profile
 char=gamChar
 If foc=4 And charPartner(gamChar)>0 Then char=charPartner(gamChar)
 If foc=5 And charManager(gamChar)>0 Then char=charManager(gamChar)
 If subFoc>0 And gamSchedule(subFoc)>0 And gamOpponent(subFoc)>0 And InjuryDate(subFoc)=0 Then char=gamOpponent(subFoc)
 If subFoc>gamDate And gamMatch(subFoc)=10 Then char=gamChar
 If subFoc>gamDate And (gamMatch(subFoc)=14 Or gamMatch(subFoc)=15) Then char=gamChar
 If (subFoc=gamDate And no_wrestlers>4 And matchTeams=<0) Or (subFoc>gamDate And gamMatch(subFoc)=16) Then char=gamChar
 DrawProfile(char,-1,-1,0)
 ;SCHEDULE
 subFoc=0
 x=rX#(400)+scroll : y=rY#(185)
 For month=1 To 12
  ;month boxes
  SetFont font(3)
  If month=GetMonth(gamDate)
   DrawImage gMonth(2),x,y
   Outline(textMonth$(month)+" "+gamYear,x,y+25,0,0,0,255,255,255)
  Else
   DrawImage gMonth(1),x,y
   Outline(textMonth$(month)+" "+gamYear,x,y+25,0,0,0,155,155,155)
  EndIf
  ;week boxes
  dateX=x-75
  For week=1 To 4
   If scrollTim<scrollDelay And MouseX()=>dateX-23 And MouseX()=<dateX+26 And MouseY()=>y-37 And MouseY()=<y+20
    subFoc=GetDate(month,week)
   EndIf
   If (GetMonth(gamDate)=month And GetWeek(gamDate)=week) Or GetDate(month,week)=subFoc
    DrawImage gDateHighlight,dateX,y-13
    Color 255,240,100 : Rect dateX-25,y-38,51,51,0
   EndIf
   If InjuryDate(GetDate(month,week))
    DrawImage gInjuryDate,dateX,y-13
   Else
    If gamSchedule(GetDate(month,week))=-2 Then DrawImage gCourtDate,dateX,y-13
    If gamSchedule(GetDate(month,week))=>0 Then DrawImage gEvent(gamSchedule(GetDate(month,week))),dateX,y-13
   EndIf
   If GetDate(month,week)<gamDate
    DrawImage gHistory(0),dateX,y-13
    If gamSchedule(GetDate(month,week))>0 Or gamSchedule(GetDate(month,week))=-2
     If gamResult(GetDate(month,week))=1 Then DrawImage gHistory(1),dateX,y-13
     If gamResult(GetDate(month,week))=3 Then DrawImage gHistory(2),dateX,y-13
    EndIf
   EndIf
   Color 0,0,0 : Rect dateX-24,y-37,8,11,1
   Color 155,155,155 : Rect dateX-26,y-39,9,12,1
   If (GetMonth(gamDate)=month And GetWeek(gamDate)=week) Or GetDate(month,week)=subFoc
    Color 255,255,255 : Rect dateX-26,y-39,9,12,1
   EndIf
   Color 0,0,0 : Rect dateX-26,y-39,9,12,0
   SetFont fontStat(0)
   Outline(week,dateX-21,y-34,0,0,0,0,0,0)
   If gamMission>0 And GetDate(month,week)=gamDeadline Then DrawImage charPhoto(fedBooker(charFed(gamChar))),dateX-25,y-28
   dateX=dateX+50
  Next
  x=x+255
 Next
 ;DESCRIPTION
 ;establish which details to show
 If subFoc>0 Then showDate=subFoc Else showDate=gamDate
 showEvent=gamSchedule(showDate)
 If InjuryDate(showDate) Then showEvent=-1 
 ;event type
 y=rY#(269)
 SetFont font(4)
 If showEvent=-2 Then Outline("COURT CASE",rX#(400),y-11,0,0,0,150,100,50)
 If showEvent=-1 Then Outline("INJURED",rX#(400),y-11,0,0,0,200,50,50)
 If showEvent=0 Then Outline("EMPTY",rX#(400),y-11,0,0,0,150,150,150)
 If fed=7
  If showEvent=1 Then Outline("TRAINING SESSION",rX#(400),y-21,0,0,0,90,150,210)
  If showEvent=2 Then Outline("STUDENT SHOWCASE",rX#(400),y-21,0,0,0,200,100,230)
 Else
  If showEvent=1 Then Outline("TV TAPING",rX#(400),y-21,0,0,0,90,150,210)
  If showEvent=2 Then Outline("PAY-PER-VIEW",rX#(400),y-21,0,0,0,200,100,230)
 EndIf
 If showEvent=3 Then Outline("TOURNAMENT",rX#(400),y-21,0,0,0,250,200,100)
 If showEvent=4 Then Outline("INTER-PROMOTIONAL CONTEST",rX#(400),y-21,0,0,0,100,220,80)
 If showEvent=5 Then Outline("CHARITY EVENT",rX#(400),y-21,0,0,0,225,75,105)
 If showEvent=6 Then Outline(Upper$(charName$(fedRoster(9,fedSize(9))))+" MEMORIAL",rX#(400),y-21,0,0,0,150,100,100)
 ;opponent line
 SetFont font(4)
 v=gamOpponent(showDate)
 If showEvent=-2
  If showDate<gamDate Then Outline("A matter was settled in court...",rX#(400),y+10,0,0,0,255,255,255)
  If showDate=>gamDate Then Outline("A matter is to be settled in court...",rX#(400),y+10,0,0,0,255,255,255)
 EndIf
 If showEvent=-1
  If gamSchedule(showDate)=-1 Then Outline("Unable to compete...",rX#(400),y+10,0,0,0,255,255,255)
  If gamSchedule(showDate)>-1
   If InjuryStatus(gamChar)=<1 Then Outline("Unable to compete until next week...",rX#(400),y+10,0,0,0,255,255,255)
   If InjuryStatus(gamChar)>1 Then Outline("Unable to compete for the next "+InjuryStatus(gamChar)+" weeks...",rX#(400),y+10,0,0,0,255,255,255)
  EndIf
 EndIf
 If showEvent=0 Then Outline("Nothing scheduled...",rX#(400),y+10,0,0,0,255,255,255)
 If showEvent>0
  If showDate<gamDate
   If gamResult(showDate)=0 Then Outline("Competed against "+charName$(v),rX#(400),y,0,0,0,255,255,255)
   If gamResult(showDate)=1 Then Outline("Lost to "+charName$(v),rX#(400),y,0,0,0,255,255,255)
   If gamResult(showDate)=2 Then Outline("Tied with "+charName$(v),rX#(400),y,0,0,0,255,255,255)
   If gamResult(showDate)=3 Then Outline("Defeated "+charName$(v),rX#(400),y,0,0,0,255,255,255)
  Else
   namer$="???"
   If v>0
    namer$=charName$(v)
    If charPartner(gamChar)>0 Or (showDate=gamDate And matchTeams>0) Or (gamSchedule(showDate)=3 And cupTeams(cupSlot)=1) Or gamMatch(showDate)=12 Or gamMatch(showDate)=13
     If charPartner(v)>0 Then namer$=charTeamName$(v) Else namer$=charName$(v)+"'s Team"
    EndIf
   EndIf
   If showDate=gamDate And no_wrestlers=>3 And no_wrestlers=<4 And matchTeams=<0
    For count=1 To no_wrestlers
     If pChar(count)<>gamChar And pChar(count)<>v Then namer$=namer$+" Vs "+charName$(pChar(count))
    Next
   EndIf
   If showDate>gamDate And gamMatch(showDate)=10 Then namer$=namer$+" Vs ???"
   If showDate>gamDate And (gamMatch(showDate)=14 Or gamMatch(showDate)=15) Then namer$=namer$+" Vs ???" : namer$=namer$+" Vs ???"
   If (showDate=gamDate And no_wrestlers>4 And matchTeams=<0) Or (showDate>gamDate And gamMatch(showDate)=16) Then namer$="Multiple Opponents"
   If gamSchedule(showDate)=4 And showDate=>gamDate 
    If gamRivalFed(showDate)>0 Then namer$=fedName$(gamRivalFed(showDate)) Else namer$="Multiple Opponents"
   EndIf
   Outline("Vs "+namer$,rX#(400),y,0,0,0,255,255,255)
  EndIf
 EndIf
 ;context info
 If showEvent>0
  SetFont font(2)
  namer$="Match To Be Announced"
  If gamMatch(showDate)>0
   namer$=textMatch$(gamMatch(showDate))
   If gamGimmick(showDate)>0 Then namer$="'"+textGimmick$(gamGimmick(showDate))+"' "+namer$
  EndIf
  Outline(namer$,rX#(400),y+20,0,0,0,200,200,200)
 EndIf
 ;OPTIONS
 y=328
 DrawOption(1,rX#(400)-103,rY#(y),"STUDY ROSTERS","")
 DrawOption(2,rX#(400)-103,rY#(y+36),"EDIT CHARACTER","")
 DrawOption(3,rX#(400)-103,rY#(y+72),"TRAINING","")
 DrawOption(4,rX#(400)+103,rY#(y),"RECRUIT PARTNER","")
 DrawOption(5,rX#(400)+103,rY#(y+36),"HIRE MANAGER","")
 DrawOption(6,rX#(400)+103,rY#(y+72),"ARRANGE MATCH","")
 DrawOption(7,rX#(400),rY#(y+113),"STUDY DATABASE","") 
 DrawOption(8,rX#(400),rY#(y+150),"RETIRE!","")
 DrawOption(9,rX#(400),rY#(524),">>> PROCEED >>>","") 
 DrawOption(10,rX#(400),rY#(560),"<<< SAVE & EXIT <<<","")  
 ;cursor
 If foc<>oldfoc Then oldfoc=foc : PlaySound sMenuSelect 
 If subFoc<>oldSubFoc Then oldSubFoc=subFoc : PlaySound sMenuSelect 
 DrawImage gCursor,MouseX(),MouseY()

 Flip
 ;screenshot (F12)
 If KeyHit(88) Then Screenshot()

Wend
;leave
FreeTimer timer
If go=1 Then PlaySound sMenuGo Else PlaySound sMenuBack 
editFoc=0 : editScroll=0 : editFilter=0
gamScroll=scroll
;access option
If go=1 
 If foc=1 Then screen=11 : screenAgenda=2 ;study rosters
 If foc=2 Then editChar=gamChar : editFed=fed : screen=51 : screenAgenda=1 ;edit character
 If foc=3 Then screen=54 ;training
 If foc=4 Then fed=charFed(gamChar) : screen=12 : screenAgenda=6 ;recruit partner
 If foc=5 Then fed=charFed(gamChar) : screen=12 : screenAgenda=3 ;hire manager
 If foc=6 Then fed=charFed(gamChar) : screen=12 : screenAgenda=4 ;arrange match
 If foc=7 Then screen=28 : screenAgenda=2  ;database
 If foc=8 Then negChar=fedBooker(charFed(gamChar)) : negTopic=1 : negSetting=1 : screen=53  ;retire
EndIf
;proceed
If go=1 And foc=9
 If gamSchedule(gamDate)>0 And (InjuryStatus(gamChar)=0 Or gamAgreement(10)>0)
  screen=50 : screenAgenda=0
  If gamSchedule(gamDate)=3 Then screen=15 : screenAgenda=11
 Else
  If gamSchedule(gamDate)=3
   For count=gamDate To 48
    If gamSchedule(count)=3 Then gamSchedule(count)=0 
   Next
   For b=1 To 32
    For count=1 To 2
     If cupBracket(cupSlot,b,count)=gamChar
      Repeat
       cupBracket(cupSlot,b,count)=Rnd(1,no_chars)
      Until charFed(cupBracket(cupSlot,b,count))=charFed(gamChar) And cupBracket(cupSlot,b,count)<>gamChar
     EndIf
    Next
   Next
  EndIf
  If InjuryStatus(gamChar)>0 Then gamSchedule(gamDate)=-1
  gamAttendance(gamDate)=0
  screen=24
 EndIf
EndIf
;save & exit
If go=-1
 GrabImage slotPreview(slot),(rX#(400)-225)-18,rY#(25)+41
 Loader("Please Wait","Saving Career")
 SaveImage(slotPreview(slot),"Data/Previews/Preview0"+slot+".bmp")
 SaveUniverse()
 SaveProgress(slot)
 SaveWorld(slot)
 SaveChars(slot)
 screen=10
EndIf
End Function

;/////////////////////////////////////////////////////////////////
;---------------------- RELATED FUNCTIONS ------------------------
;/////////////////////////////////////////////////////////////////

;GENERATE CAREER
Function GenerateCareer()
 ;get character slot
 Repeat
  gamChar=Rnd(1,no_chars)
 Until charFed(gamChar)=7 And gamChar<>fedBooker(charFed(gamChar)) And TitleHolder(gamChar,0)=0
 GenerateCharacter(gamChar)
 gamCasted=0
 For cyc=1 To 20
  gamCastList(cyc)=0
 Next
 ;reset schedule
 If gamInherit=0 Then GetRealDate()
 ResetSchedule(1)
 gamSchedule(gamDate)=1 : gamVenue(gamDate)=0
 GenerateArena(charFed(gamChar),gamSchedule(gamDate),1)
 cupSize(cupSlot)=0
 gamMission=0 : gamTarget=0 : gamDeadline=0
 gamMatch(gamDate)=2 : gamPromo(gamDate)=4
 randy=Rnd(0,2)
 If (randy=0 Or promoLocked(78)) And optReferees>0 Then gamPromo(gamDate)=78
 If promoLocked(4) Then gamPromo(gamDate)=4
 ;reset promotions
 For promotion=1 To 9
  If gamInherit=0 Then fedFatality(promotion)=0
  charExperience(gamChar,promotion)=0
  charMatches(gamChar,promotion)=0
  charWins(gamChar,promotion)=0 
  For title=0 To 7 ;new belts orig =4
   charTitles(gamChar,promotion,title)=0
   If gamInherit=0
    For count=1 To 10
     fedHistCount(promotion,title,count)=0
     fedHistChar(promotion,title,count)=0
     fedHistPartner(promotion,title,count)=0
     fedHistDate(promotion,title,count)=0
     fedHistYear(promotion,title,count)=0
    Next
    WriteHistory(promotion,title)
   EndIf
  Next
  If gamInherit=0
   champ=fedChampWorld(promotion)
   If charTitles(fedChampWorld(promotion),promotion,1)<1 Then charTitles(fedChampWorld(promotion),promotion,1)=1
   If charTitles(fedChampInter(promotion),promotion,2)<1 Then charTitles(fedChampInter(promotion),promotion,2)=1
   If charTitles(fedChampTag(promotion,1),promotion,3)<1 Then charTitles(fedChampTag(promotion,1),promotion,3)=1
   If charTitles(fedChampTag(promotion,2),promotion,3)<1 Then charTitles(fedChampTag(promotion,2),promotion,3)=1
   If charTitles(fedCupHolder(promotion),promotion,4)<1 Then charTitles(fedCupHolder(promotion),promotion,4)=1
;new belts
If charTitles(fedChampWomens(promotion),promotion,5)<1 Then charTitles(fedChampWomens(promotion),promotion,5)=1
If charTitles(fedChampUS(promotion),promotion,6)<1 Then charTitles(fedChampUS(promotion),promotion,6)=1
If charTitles(fedChampTV(promotion),promotion,7)<1 Then charTitles(fedChampTV(promotion),promotion,7)=1
;end
  EndIf
 Next
 RankPromotions()
 ;reset character status
 charPopularity(gamChar)=50
 charStrength(gamChar)=50
 charSkill(gamChar)=50
 charAgility(gamChar)=50
 charStamina(gamChar)=50
 charToughness(gamChar)=50
 charHappiness(gamChar)=75
 charAttitude(gamChar)=75
 ResetHealthStatus(gamChar)
 charWeapon(gamChar)=0
 ;reset knowledge
 For style=1 To 4
  For move=1 To 200
   moveLearned(style,move)=0
  Next
 Next
 moveLearned(1,1)=2 : moveLearned(1,2)=2
 moveLearned(1,3)=2 : moveLearned(1,7)=2 
 moveLearned(2,1)=2 
 moveLearned(3,1)=2 
 moveLearned(4,1)=2 
 For style=1 To 5
  For move=1 To 50
   attackLearned(style,move)=0
   crushLearned(style,move)=0
  Next
 Next
 For move=1 To 200
  tauntLearned(move)=0
 Next
 For coz=1 To 3
  For count=1 To 200
   costumeLearned(coz,count)=0
  Next
 Next
 For count=1 To 100
  musicLearned(count)=0
 Next
 ;reset career status
 charBank(gamChar)=5000
 charManager(gamChar)=0
 charWorth(gamChar)=CalculateWorth(gamChar,7)
 charSalary(gamChar)=0
 charContract(gamChar)=0
 charClause(gamChar,1)=2
 charClause(gamChar,2)=0
 charClause(gamChar,3)=0
 For count=1 To 50
  gamAgreement(count)=0
 Next
 ;reset relationship history
 For char=1 To no_chars
  ResetOldValues(char)
  If char<>gamChar And gamInherit=0 Then ResetCareerStatus(char)
  For count=1 To 3
   gamNegotiated(char,count)=0
  Next
  charRelationship(gamChar,char)=0
  charRelationship(char,gamChar)=0
  For v=1 To no_chars
   charHistory(char,v)=0 : charTeamHistory(char,v)=0
  Next
  charTeamHistory(char,charPartner(char))=2
  charTeamHistory(char,charManager(char))=3
  If charPartner(char)=gamChar Then charPartner(char)=0
  If charManager(char)=gamChar Then charManager(char)=0
  If gamInherit=0 Then charTradeReaction(char)=0
  If gamInherit=0 Then charWeightChange(char)=0
  If char<>gamChar And gamInherit=0
   charHealth(char)=Rnd(25,100)
   randy=Rnd(0,charToughness(char))
   If randy=<5 Then charInjured(char,Rnd(0,5))=Rnd(1,8) : charHealth(char)=0
  EndIf
 Next
End Function

;RESET HEALTH STATUS
Function ResetHealthStatus(char) 
 charHealth(char)=100
 charEyeShape(char)=40
 charVacant(char)=0
 charTrainCourse(char)=0
 For limb=0 To 5
  charInjured(char,limb)=0
  charOldInjured(char,limb)=0 
 Next
 For limb=1 To 50
  charLimb(char,limb)=1
 Next
End Function

;RESET CAREER STATUS
Function ResetCareerStatus(char) 
 If charAge(char)=>40 Then charPeaked(char)=charAge(char) Else charPeaked(char)=0
 matchLoops=charAge(char)-16
 matchLoops=Rnd(matchLoops,matchLoops*4)
 If charFed(char)=8 Then matchLoops=matchLoops/5
 If matchLoops<1 Then matchLoops=1
 For promotion=1 To 9
  charMatches(char,promotion)=0 
  charWins(char,promotion)=0
 Next
 For count=1 To matchLoops
  randy=Rnd(1,50)
  If randy=<7
   charMatches(char,randy)=charMatches(char,randy)+1 
  Else
   charMatches(char,charFed(char))=charMatches(char,charFed(char))+1
  EndIf
 Next
 For promotion=1 To 9
  charExperience(char,promotion)=Int(PercentOf#(charMatches(char,promotion),150))
  For count=1 To charMatches(char,promotion)
   randy=Rnd(40,100)
   If randy=<GetValue(char) Then charWins(char,promotion)=charWins(char,promotion)+1
  Next
  ;factor#=GetPercent#(GetValue(char)-40,60)
  ;charWins(char,promotion)=Int(PercentOf#(charMatches(char,promotion),factor#))
  ;charWins(char,promotion)=Rnd(charWins(char,promotion),charMatches(char,promotion))
  ;If charWins(char,promotion)<0 Then charWins(char,promotion)=0
  ;If charWins(char,promotion)>charMatches(char,promotion) Then charWins(char,promotion)=charMatches(char,promotion) 
 Next
 GenerateContract(char)
End Function

;RESET SCHEDULE
Function ResetSchedule(start)
 rotor=Rnd(0,1) : showWeek=Rnd(1,4)
 For date=start To 48
  ;events
  gamSchedule(date)=0
  If date=>gamDate
   gamSchedule(date)=1
   If GetWeek(date)=showWeek Then gamSchedule(date)=2
  EndIf
  randy=Rnd(0,8)
  If randy=<1 Then gamSchedule(date)=0
  ;matches
  gamRivalFed(date)=Rnd(1,6)
  gamOpponent(date)=0
  gamMatch(date)=0
  gamGimmick(date)=0
  gamPromo(date)=0
  gamResult(date)=0
  ;venues
  gamVenue(date)=0
  gamAttendance(date)=0
  gamAtmos(date)=0
  gamRopes(date)=0
  gamPads(date)=0
  gamCanvas(date)=0
  gamApron(date)=0
  gamMatting(date)=0
 Next
 ;negate missions
 gamMission=0
 gamTarget=0
 gamDeadline=0
 cupSize(cupSlot)=0
 ;generate new location
 GenerateArena(charFed(gamChar),gamSchedule(gamDate),0)
End Function

;CALCULATE REAL DATE!
Function GetRealDate()
 ;identify week
 weeker=Int(Left$(CurrentDate$(),2))
 If weeker=<7 Then week=1
 If weeker=>8 And weeker=<15 Then week=2
 If weeker=>16 And weeker=<23 Then week=3
 If weeker>23 Then week=4
 ;identify month
 For count=1 To 12
  monther$=Mid$(CurrentDate$(),4,3)
  If monther$=Left$(textMonth$(count),3) Then month=count
 Next
 ;translate to game
 gamDate=GetDate(month,week)
 gamYear=Int(Right$(CurrentDate$(),4))
End Function

;CALCULATE DATE FROM WEEK & MONTH
Function GetDate(month,week)
 value=((month-1)*4)+week
 Return value
End Function

;EXTRACT MONTH FROM DATE
Function GetMonth(date)
 value=1
 While date>0
  date=date-4
  If date>0 Then value=value+1
 Wend
 Return value
End Function

;EXTRACT WEEK FROM DATE
Function GetWeek(date)
 value=date-((GetMonth(date)-1)*4)
 Return value
End Function

;DESCRIBE DATE
Function DescribeDate$(date,year)
 dateline$=textWeek$(GetWeek(date))+" of "+textMonth$(GetMonth(date))
 If year>0 Then dateline$=dateline$+" "+year
 Return dateline$
End Function

;FORTHCOMING OPPONENT?
Function ForthcomingOpponent(char)
 value=0
 For date=gamDate To 48
  If gamSchedule(date)>0 And gamOpponent(date)=char Then value=1 : Exit
 Next
 Return value
End Function

;FIND NEXT FREE DATE
Function NextDate()
 value=0
 For date=gamDate To 48
  If date>gamDate And gamSchedule(date)=>1 And gamSchedule(date)=<2 And gamOpponent(date)=0 Then value=date : Exit
 Next
 Return value
End Function

;FIND NEXT OPPONENT
Function NextOpponent()
 value=0
 For date=gamDate To 48
  If gamSchedule(date)>0 And gamOpponent(date)>0 Then value=gamOpponent(date) : Exit
 Next
 Return value
End Function

;FIND LAST OPPONENT
Function LastOpponent()
 value=0
 For count=1 To 48
  date=gamDate-count
  If date>0
   If gamSchedule(date)>0 And gamOpponent(date)>0 Then value=gamOpponent(date) : Exit
  EndIf
 Next
 Return value
End Function

;FIND LAST RESULT
Function LastResult()
 value=0
 For count=1 To 48
  date=gamDate-count
  If date>0
   If gamSchedule(date)>0 And gamResult(date)>0 Then value=gamResult(date)
  EndIf
  If value>0 Then Exit
 Next
 Return value
End Function

;FIND LAST EVENT
Function LastEvent()
 value=0
 For count=1 To 48
  date=gamDate-count
  If date>0
   If gamSchedule(date)<>0 Then value=gamSchedule(date) : Exit
  EndIf
 Next
 Return value
End Function

;ALREADY BOOKED TO FACE OPPONENT?
Function OpponentBooked(char)
 value=0
 For date=gamDate To 48
  If gamSchedule(date)>0 And gamOpponent(date)=char Then value=1
 Next
 Return value
End Function

;COUNT CLIENTS
Function CountClients(char)
 value=0
 For count=1 To fedSize(charFed(char))
  v=fedRoster(charFed(char),count)
  If charManager(v)=char Then value=value+1
 Next
 Return value
End Function

;ASSIGN MATCH
Function AssignMatch(date)
 ;match type
 match=2
 If charMatches(gamChar,charFed(gamChar))>0
  chance=30
  If charRelationship(gamChar,gamOpponent(date))<0 Then chance=chance/2
  If gamSchedule(date)=>2 Then chance=chance/2
  If screen=23 Then chance=chance/2
  If chance<15 Then chance=15
  randy=Rnd(0,chance)
  If randy=<3 Then match=Rnd(3,9) ;one-on-one rule variations
  If randy=4 And charPartner(gamChar)=0 Then match=10 ;triple threat
  If randy=>5 And randy=<6 And charPartner(gamChar)=0 And optContent>0 Then match=12 ;tag-team
  If randy=7 And charPartner(gamChar)=0 And optContent>0 Then match=13 ;team battle (for solo player)
  If randy=>6 And randy=<7 And charPartner(gamChar)>0 Then match=13 ;team battle (for team player)
  If randy=8 And charPartner(gamChar)=0 And charRelationship(gamChar,gamOpponent(date))<0 Then match=11 ;handicap match
  If randy=9 And charPartner(gamChar)=0 And optContent>0 Then match=Rnd(14,16) ;4-way variations
  If randy=10 And charPartner(gamChar)=0 And charFed(gamChar)=<6 Then match=18 ;cage escape
  If randy=11 And optContent=2 Then match=19 ;six-man tag
  If randy=12 And optContent=2 Then match=20 ;eight-man elimination
  If randy=13 And charPartner(gamChar)=0
   If charFed(gamChar)=1 Then match=Rnd(8,9) ;favour brutality in FedOn
   If charFed(gamChar)=2 And optContent>0 Then match=Rnd(14,16) ;favour battle royals in USA
   If charFed(gamChar)=3 Then match=Rnd(3,5) ;favour endurance in England
   If charFed(gamChar)=4 Then match=7 ;favour shoot fights in Japan
   If charFed(gamChar)=5 Then match=6 ;favour submission in Canada
   If charFed(gamChar)=6 Then match=10 ;favour triple threat in Mexico
  EndIf
  If randy=14 And charPartner(gamChar)=0
   If charFed(gamChar)=2 Then match=18 ;favour cage escape in USA
   If charFed(gamChar)=4 Then match=17 ;favour sumo in Japan
  EndIf 
  If gamMatch(date-1)=match Then match=2 ;prevent repetition
 EndIf
 gamMatch(date)=match
 ;gimmicks
 If gamGimmick(date)=0 And charFed(gamChar)=<6 And charMatches(gamChar,charFed(gamChar))>0
  gimmick=0
  chance=80
  If charRelationship(gamChar,gamOpponent(date))<0 Then chance=chance/2
  If gamSchedule(date)=>2 Then chance=chance/2
  If screen=23 Then chance=chance/2
  If chance<20 Then chance=20
  randy=Rnd(0,chance)
  If randy=<2 Then gimmick=1 ;hardcore rules
  If (randy=>3 And randy=<4) Or (randy=5 And gamMatch(date)=>6 And gamMatch(date)=<7)
   If gamMatch(date)<>15 And gamMatch(date)<>16 And gamMatch(date)<>17 Then gimmick=2 ;steel cage
  EndIf
  If randy=6 Then gimmick=3 ;barbed wire
  If randy=7 Then gimmick=4 ;electrified
  If randy=8 Then gimmick=5 ;inferno
  If randy=9 Then gimmick=6 ;blast
  If randy=10 Then gimmick=7 ;minefield
  If randy=11 And gamMatch(date)<>12 Then gimmick=8 ;glass
  If randy=12
   If charFed(gamChar)=1 Then gimmick=1 ;favour hardcore in FedOn
   If charFed(gamChar)=2 Then gimmick=2 ;favour cages in USA
   If charFed(gamChar)=3 Then gimmick=3 ;favour barbed wire in England
   If charFed(gamChar)=4 Then gimmick=Rnd(4,7) ;favour explosives in Japan
  EndIf
  If charRelationship(gamChar,gamOpponent(date))<0 ;And charPartner(gamChar)=0
   ;If randy=13 Then gimmick=9 ;hair vs hair
   ;If randy=14 Then gimmick=10 ;loser leaves
   If randy=15 Then gimmick=11 ;empty arena  
   If randy=16 And gamMatch(date)=>2 And gamMatch(date)=<10 Then gimmick=13 ;multiple refs
  EndIf
  If (charRelationship(gamChar,gamOpponent(date))<0 And charPopularity(gamChar)<charPopularity(gamOpponent(date))-5) Or TitleHolder(gamOpponent(date),0)>0
   If randy=17 And TitleHolder(gamChar,0)=0 And gamMatch(date)<>3 And gamMatch(date)<>4 And gamMatch(date)<>5
    gimmick=12 ;race against time
   EndIf
  EndIf
  If gamGimmick(date-1)=gimmick Then gimmick=0 ;prevent repetition
  If gamMatch(date)=18 Then gimmick=2 ;force cage
  gamGimmick(date)=gimmick
 EndIf
End Function

;GET MATCH PROPERTIES
Function GetMatchRules(match)
 ;defaults
 matchPreset=match
 matchLocation=0
 matchShoot=0
 ;confrontation
 If match=1
  no_wrestlers=2 : no_refs=0 : matchType=5 : matchRules=0 : matchTeams=0
  matchPins=0 : matchSubs=0 : matchKOs=1 : matchBlood=0 : matchCountOuts=0
  matchTimeLim=0
 EndIf
 ;normal
 If match=2
  no_wrestlers=2 : no_refs=1 : matchType=1 : matchRules=2 : matchTeams=0
  matchPins=1 : matchSubs=1 : matchKOs=0 : matchBlood=0 : matchCountOuts=2
  matchTimeLim=10
 EndIf
 ;best of three
 If match=3
  no_wrestlers=2 : no_refs=1 : matchType=2 : matchRules=2 : matchTeams=0
  matchPins=1 : matchSubs=1 : matchKOs=0 : matchBlood=0 : matchCountOuts=2
  matchTimeLim=15
 EndIf
 ;iron man
 If match=4
  no_wrestlers=2 : no_refs=1 : matchType=3 : matchRules=2 : matchTeams=0
  matchPins=1 : matchSubs=1 : matchKOs=0 : matchBlood=0 : matchCountOuts=2
  matchTimeLim=10
 EndIf
 ;24/7 challenge
 If match=5
  no_wrestlers=2 : no_refs=1 : matchType=4 : matchRules=2 : matchTeams=0
  matchPins=1 : matchSubs=1 : matchKOs=0 : matchBlood=0 : matchCountOuts=2
  matchTimeLim=10
 EndIf
 ;submission
 If match=6
  no_wrestlers=2 : no_refs=1 : matchType=1 : matchRules=2 : matchTeams=0
  matchPins=0 : matchSubs=1 : matchKOs=0 : matchBlood=0 : matchCountOuts=1
  matchTimeLim=10
 EndIf
 ;shoot fight
 If match=7
  no_wrestlers=2 : no_refs=1 : matchType=5 : matchRules=2 : matchTeams=0
  matchPins=0 : matchSubs=0 : matchKOs=1 : matchBlood=0 : matchCountOuts=1
  matchTimeLim=10 : matchShoot=1
 EndIf 
 ;last man standing
 If match=8
  no_wrestlers=2 : no_refs=1 : matchType=5 : matchRules=0 : matchTeams=0
  matchPins=0 : matchSubs=0 : matchKOs=1 : matchBlood=0 : matchCountOuts=0
  matchTimeLim=0
 EndIf
 ;first blood
 If match=9
  no_wrestlers=2 : no_refs=1 : matchType=5 : matchRules=0 : matchTeams=0
  matchPins=0 : matchSubs=0 : matchKOs=0 : matchBlood=1 : matchCountOuts=0
  matchTimeLim=0
 EndIf
 ;triple threat
 If match=10
  no_wrestlers=3 : no_refs=1 : matchType=1 : matchRules=2 : matchTeams=0
  matchPins=1 : matchSubs=1 : matchKOs=0 : matchBlood=0 : matchCountOuts=2
  matchTimeLim=10
 EndIf
 ;handicap
 If match=11
  no_wrestlers=3 : no_refs=1 : matchType=1 : matchRules=2 : matchTeams=1
  matchPins=1 : matchSubs=1 : matchKOs=0 : matchBlood=0 : matchCountOuts=2
  matchTimeLim=10
 EndIf
 ;tag team
 If match=12
  no_wrestlers=4 : no_refs=1 : matchType=1 : matchRules=2 : matchTeams=2
  matchPins=1 : matchSubs=1 : matchKOs=0 : matchBlood=0 : matchCountOuts=2
  matchTimeLim=15 
 EndIf
 ;team battle
 If match=13
  no_wrestlers=4 : no_refs=1 : matchType=5 : matchRules=2 : matchTeams=1
  matchPins=1 : matchSubs=1 : matchKOs=0 : matchBlood=0 : matchCountOuts=2
  matchTimeLim=15
 EndIf
 ;royal brawl
 If match=14
  no_wrestlers=4 : no_refs=1 : matchType=5 : matchRules=2 : matchTeams=0
  matchPins=1 : matchSubs=1 : matchKOs=0 : matchBlood=0 : matchCountOuts=2
  matchTimeLim=0
 EndIf
 ;battle royal
 If match=15
  If optContent=0 Then no_wrestlers=4
  If optContent=1 Then no_wrestlers=8
  If optContent=2 Then no_wrestlers=12
  no_refs=0 : matchType=5 : matchRules=0 : matchTeams=0
  matchPins=0 : matchSubs=0 : matchKOs=0 : matchBlood=0 : matchCountOuts=3
  matchTimeLim=0
 EndIf
 ;timed battle royal
 If match=16
  If optContent=0 Then no_wrestlers=6
  If optContent=1 Then no_wrestlers=12
  If optContent=2 Then no_wrestlers=18
  no_refs=0 : matchType=5 : matchRules=0 : matchTeams=-1
  matchPins=0 : matchSubs=0 : matchKOs=0 : matchBlood=0 : matchCountOuts=3
  matchTimeLim=1
 EndIf
 ;sumo match
 If match=17
  no_wrestlers=2 : no_refs=1 : matchType=5 : matchRules=2 : matchTeams=0
  matchPins=0 : matchSubs=1 : matchKOs=1 : matchBlood=0 : matchCountOuts=3
  matchTimeLim=10
 EndIf
 ;cage escape
 If match=18
  no_wrestlers=2 : no_refs=0 : matchType=5 : matchRules=0 : matchTeams=0
  matchPins=0 : matchSubs=0 : matchKOs=0 : matchBlood=0 : matchCountOuts=3
  matchTimeLim=0
 EndIf
 ;six man tag
 If match=19
  no_wrestlers=6 : no_refs=1 : matchType=1 : matchRules=2 : matchTeams=2
  matchPins=1 : matchSubs=1 : matchKOs=0 : matchBlood=0 : matchCountOuts=2
  matchTimeLim=15 
 EndIf
 ;eight man tag
 If match=20
  no_wrestlers=8 : no_refs=1 : matchType=5 : matchRules=2 : matchTeams=2
  matchPins=1 : matchSubs=1 : matchKOs=0 : matchBlood=0 : matchCountOuts=2
  matchTimeLim=0 
 EndIf
 ;time limit filter
 If matchTimeLim>0 And matchTeams=>0
  If optLength=3 Then matchTimeLim=matchTimeLim+5
 EndIf
 ;size limit
 If fed>0 And no_wrestlers>fedSize(fed) Then no_wrestlers=fedSize(fed) : no_refs=0
 ;referee filter
 If optReferees=0 Then no_refs=0
 If optReferees=1 And no_wrestlers>3 Then no_refs=0
 If optReferees=2 And no_wrestlers>4 Then no_refs=0
 If optReferees=3 And no_wrestlers>8 Then no_refs=0
 no_plays=no_wrestlers+no_refs
End Function

;ADD GIMMICK TO CAREER MATCH
Function AddGimmick(gimmick)
 ;reset by default
 matchCage=0 : matchRopes=0 : matchBlastTim=0
 If optContent=0 Then no_items=10 : no_weaps=10
 If optContent=1 Then no_items=20 : no_weaps=20
 If optContent=2 Then no_items=25 : no_weaps=25
 itemSelection=1 : itemLayout=1 
 weapSelection=1 : weapLayout=1 
 matchChamps=1 : matchPromo=0
 ;hardcore rules
 If gimmick=1 
  no_items=no_items+(no_items/4)
  itemSelection=0 : itemLayout=0
  no_weaps=no_weaps+(no_weaps/4)
  weapSelection=0 : weapLayout=0 
  matchRules=0 
 EndIf
 If gimmick=>1 And gimmick=<8 And matchRules>1 Then matchRules=1
 ;cages
 If gimmick=2
  matchCage=Rnd(-1,4)
  If matchCage<1 Then matchCage=1
  If matchPreset=6 Or matchPreset=7 Then matchRules=2 
 EndIf
 ;novelty ropes
 If gimmick=3 Then matchRopes=1 ;barbed wire
 If gimmick=4 Then matchRopes=2 ;electrified
 If gimmick=5 Then matchRopes=3 ;inferno 
 ;blast
 If gimmick=6
  If optLength=1 Then matchBlastTim=3
  If optLength=2 Then matchBlastTim=5
  If optLength=3 Then matchBlastTim=7
 EndIf
 ;minefield
 If gimmick=7
  no_weaps=no_weaps+(no_weaps/4)
  weapSelection=20 : weapLayout=5
 EndIf
 ;hall of mirrors
 If gimmick=8
  no_items=no_items+(no_items/4)
  itemSelection=15 : itemLayout=5 
 EndIf
 ;stipulations
 If gimmick=9 Then matchChamps=6 ;hair vs hair
 If gimmick=10 Then matchChamps=7 ;loser leaves
 If gimmick=11 Then gamAttendance(gamDate)=0 : arenaAttendance=0 : matchRules=0 ;empty arena
 ;race against time
 If gimmick=12 And matchTimeLim>0 Then matchTimeLim=optLength
 ;multiple referees
 If gimmick=13 Then no_refs=2 : no_plays=no_wrestlers+no_refs
 ;item/weapon block
 If game=1 And gamAgreement(19)>0 Then no_items=0 : no_weaps=0
 ;negate titles
 If matchTeams=-1 Or fed=0 Or fed=>7 Then matchChamps=0
 If no_wrestlers>4 And matchTeams=<0 And matchChamps=<1 Then matchChamps=5
 If screenAgenda=10 Or screenAgenda=11 Or screenAgenda=12 Then matchChamps=0
 If game=0 Then matchChamps=0
End Function

;ASSIGN OPPONENT
Function AssignOpponent(char,date,task) ;0=main, 1=additional
 v=0 : its=0
 Repeat
  satisfied=1
  ;random by default
  v=fedRoster(charFed(char),Rnd(1,fedSize(charFed(char))))
  If gamSchedule(date)=>4 Then v=Rnd(1,no_chars)
  If gamSchedule(date)=4 And charFed(char)=charFed(gamChar)
   v=fedRoster(gamRivalFed(date),Rnd(1,fedSize(gamRivalFed(date))))
   If its=0 And TitleHolder(char,3) Then v=fedChampTag(gamRivalFed(date),1)
   If its=0 And TitleHolder(char,2) Then v=fedChampInter(gamRivalFed(date))
   If its=0 And TitleHolder(char,1) Then v=fedChampWorld(gamRivalFed(date))
;new belts
If its=0 And TitleHolder(char,5) Then v=fedChampWomens(gamRivalFed(date))
  
If its=0 And TitleHolder(char,6) Then v=fedChampUS(gamRivalFed(date))

If its=0 And TitleHolder(char,7) Then v=fedChampTV(gamRivalFed(date))

;end
  EndIf
  If gamSchedule(date)=4 And charFed(char)=gamRivalFed(date)
   v=fedRoster(charFed(gamChar),Rnd(1,fedSize(charFed(gamChar))))
   If its=0 And TitleHolder(char,3) Then v=fedChampTag(charFed(gamChar),1)
   If its=0 And TitleHolder(char,2) Then v=fedChampInter(charFed(gamChar))
   If its=0 And TitleHolder(char,1) Then v=fedChampWorld(charFed(gamChar))
;new belts
If its=0 And TitleHolder(char,5) Then v=fedChampWomens(charFed(gamChar))
If its=0 And TitleHolder(char,6) Then v=fedChampUS(charFed(gamChar))
If its=0 And TitleHolder(char,7) Then v=fedChampTV(charFed(gamChar))
;end
  EndIf
  If screen=54 Then v=fedRoster(7,Rnd(1,fedSize(7)))
  ;favour enemies
  If task=0 And FindEnemy(char,0)>0 And screen<>54
   randy=Rnd(0,2)
   If (randy=0 And gamSchedule(date)=1) Or (randy=<1 And gamSchedule(date)=2)
    Repeat
     v=fedRoster(charFed(char),Rnd(1,fedSize(charFed(char))))
     early=Rnd(0,2)
    Until (charRelationship(char,v)=>-4 And charRelationship(char,v)=<-1) Or (early=0 And charRelationship(char,v)<0)
   EndIf
  EndIf
  ;career logic
  If charFed(char)=charFed(v) And screen<>54
   If char=gamChar
    ;push title shots
    If charPopularity(gamChar)=>60 And gamSchedule(date)=<2 And task=0
     If charPartner(char)=0 And matchTeams=0 And TitleHolder(char,3)=0
      randy=Rnd(0,20)
      If randy=0 And charTitles(char,charFed(char),2)=0 And TitleHolder(char,0)=0 Then v=fedChampInter(charFed(char)) ;inter title shot
;new belts
If randy=5 And charTitles(char,charFed(char),6)=0 And TitleHolder(char,0)=0 Then v=fedChampUS(charFed(char)) ;US title shot
If randy=6 And charTitles(char,charFed(char),7)=0 And TitleHolder(char,0)=0 Then v=fedChampTV(charFed(char)) ;TV title shot
;end      
If randy=1 And charTitles(char,charFed(char),1)=0 And TitleHolder(char,1)=0
       If charPopularity(gamChar)=>charPopularity(fedChampWorld(charFed(char)))-10 Or charPopularity(gamChar)=>fedPopularity(charFed(char))
        v=fedChampWorld(charFed(char)) ;world title shot
       EndIf
      EndIf
     EndIf
     If charPartner(char)>0 And matchTeams>0 And TitleHolder(char,0)=0 And TitleHolder(charPartner(char),0)=0
      randy=Rnd(0,30)
      If randy=<1 Then v=fedChampTag(charFed(char),1) ;tag title shot
     EndIf
    EndIf
    ;link lucrative opponents to success
    result=LastResult()
    randy=Rnd(0,3)
    If randy>0 And result<2 And charPopularity(v)>charPopularity(gamChar) Then satisfied=0 ;not worthy of superior 
    If TitleHolder(v,0)>0 And charRelationship(char,v)=>0 
     If result<3 Then satisfied=0 ;not worthy of title shot
     If TitleHolder(v,1) And charPopularity(gamChar)=<charPopularity(v)-10 Then satisfied=0 ;not worthy of world title shot
    EndIf
    ;prevent tag champs if singles wrestler
    If TitleHolder(v,3) And charPartner(char)=0 Then satisfied=0
    ;prevent champs if campaigning
    If TitleHolder(v,0)>0 And gamPromo(date)=>57 And gamPromo(date)=<59 Then satisfied=0
    ;prevent champs as extras
    If TitleHolder(v,0)>0 And task=1 Then satisfied=0
   Else
    If (TitleHolder(char,1) And charPopularity(v)=<charPopularity(char)-10) Or (TitleHolder(v,1) And charPopularity(char)=<charPopularity(v)-10)
     satisfied=0 ;CPU's not worthy of world title shot
    EndIf 
    If (TitleHolder(char,2) And charPopularity(v)=>charPopularity(char)+10) Or (TitleHolder(v,2) And charPopularity(char)=>charPopularity(v)+10)
     satisfied=0 ;CPU's not ideal for inter title shot
    EndIf
    randy=Rnd(0,1)
    If (randy=0 And TitleHolder(char,3) And charPartner(v)=0) Or (randy=1 And TitleHolder(v,3) And charPartner(char)=0)
     satisfied=0 ;involve tag champions less
    EndIf
    If TitleHolder(char,0)>0 And TitleHolder(v,0)>0 Then satisfied=0 ;prevent champion vs champion
   EndIf
  EndIf
  ;universal logic
  randy=Rnd(0,1)
  If randy>0 And task=0 And charPartner(char)>0 And charPartner(v)=0 Then satisfied=0 ;favour teams vs teams
  randy=Rnd(0,1)
  If randy>0 And task=0 And charHeel(v)=charHeel(char) And TitleHolder(v,0)=0 Then satisfied=0 ;rarely same allegiance in opposition
  randy=Rnd(0,2)
  If randy>0 And screen=54 And charFed(v)<>7 Then satisfied=0 ;favour students for sparring
  randy=Rnd(0,4)
  If (randy>0 And v=fedBooker(charFed(char))) Or (randy>0 And charRole(v)=2) Or charRole(v)=3 
   If charRelationship(char,v)=>0 And TitleHolder(v,0)=0 And charFed(char)<>7 Then satisfied=0 ;avoid non-wrestlers
  EndIf
If charGender(v)<>charGender(char) Then satisfied=0 ;prevent male vs female
  If charVacant(v)>0 Or charFed(v)=>8 Or (charFed(v)=7 And charFed(char)<>7) Then satisfied=0 ;avoid inactive wrestlers
  If TournamentStatus(v)>0 Then satisfied=0 ;avoid pre-occupied wrestlers
  If char=gamChar And gamPromo(date)=>57 And gamPromo(date)=<59 And TitleHolder(v,0)>0 Then satisfied=0 ;avoid champions during push
  If charFed(char)<>charFed(v) And TitleHolder(v,0)>0 And TitleHolder(char,0)<>TitleHolder(v,0) Then satisfied=0 ;avoid champion counterparts
  randy=Rnd(0,4)
  If (randy>0 And charHealth(v)<50) Or (randy>2 And charHealth(v)<75) Or InjuryStatus(v)>0 Then satisfied=0 ;favour healthy
  If charLimb(v,40)=0 And charLimb(v,43)=0 Then satisfied=0 ;avoid cripples!
  If gamSchedule(date)=>4 And charFed(v)=charFed(char) Then satisfied=0 ;avoid colleagues at special events
  If v=0 Or v=char Or v=charPartner(char) Or v=charManager(char) Then satisfied=0 ;avoid suicide
  If v=gamChar Or v=charPartner(gamChar) Or v=charManager(gamChar) Then satisfied=0 ;avoid player for CPU's 
  If char=gamChar And gamPromoVariable(date)>0 And v=gamPromoVariable(date) Then satisfied=0 ;avoid forced partners
  If screen=22 And charFought(v)>0 Then satisfied=0 ;avoid already fought in simulations
  If task=1
   For count=1 To no_plays
    If v=gamCastList(count) Or v=charPartner(gamCastList(count)) Or v=charManager(gamCastList(count)) Then satisfied=0 ;avoid already involved
   Next
  EndIf
  ;get-out clause
  its=its+1
  If its>10000 Then satisfied=1
 Until satisfied=1
 Return v
End Function

;FIND IDEAL TEAM-MATE
Function AssignPartner(char,task) ;-1=permanent, 0=for match, 1=for tournament
 partner=0 : its=0
 Repeat
  ;random by default
  satisfied=1 : its=its+1
  partner=fedRoster(charFed(char),Rnd(1,fedSize(charFed(char))))
  If its>500 Then partner=Rnd(1,no_chars)
  ;force partners
  If its<100
   If charPartner(char)>0 Then partner=charPartner(char)
   If char=fedChampTag(charFed(char),1) Then partner=fedChampTag(charFed(char),2)
   If char=fedChampTag(charFed(char),2) Then partner=fedChampTag(charFed(char),1) 
  EndIf
  ;ideal criteria
  If its<100
   If charPartner(partner)>0 And charPartner(partner)<>char And partner<>charPartner(char) Then satisfied=0 ;avoid those with other partners
   If TitleHolder(partner,3)<>TitleHolder(char,3) Then satisfied=0 ;avoid tag champions
  EndIf
  If its=>100 And its<200 And charRelationship(char,partner)=<0 Then satisfied=0 ;prefer friends
  If its=>200 And its<300 And charHeel(char)<>charHeel(partner) Then satisfied=0 ;prefer same allegiance
  If its=>300 And its<400 And charFed(char)<>charFed(partner) Then satisfied=0 ;prefer same fed 
  ;important exceptions
  If game=1
   randy=Rnd(0,2)
   If (randy=0 And charHealth(partner)<50) Or (randy=<1 And charHealth(partner)<25) Then satisfied=0 ;favour healthy
   If charVacant(partner)>0 Or TournamentStatus(partner)>0 Then satisfied=0 ;avoid unavailable characters
  EndIf 
  randy=Rnd(0,5)
  If randy>0 And charLimb(partner,40)=0 And charLimb(partner,43)=0 Then satisfied=0 ;avoid cripples!
  randy=Rnd(0,4)
  If (randy>0 And charRole(partner)<>1) Or charRole(partner)=3 Or charFed(partner)=>8 Then satisfied=0 ;avoid non-wrestlers 
  If task=0 
   For v=1 To no_plays
    If partner=gamCastList(v) Or (partner=charManager(gamCastList(v)) And partner<>charPartner(gamCastList(v)) And its<100)
     satisfied=0 ;already involved in match
    EndIf
   Next   
  EndIf
  If task=1 And TournamentSelected(partner)>0 Then satisfied=0 ;already involved in tournament
  If game=1 And char<>gamChar
   If partner=gamChar Or partner=charPartner(gamChar) Or partner=charManager(gamChar) Then satisfied=0 ;key to career
  EndIf
  If partner=0 Or char=partner Then satisfied=0 ;avoid suicide
  If its>1000 Then satsified=1
 Until satisfied=1
 ;force player's partner
 If game=1 And char=gamChar And charPartner(gamChar)>0 And FindCasted(charPartner(gamChar))=0 Then partner=charPartner(gamChar)
 Return partner
End Function

;ASSIGN REFEREE
Function AssignReferee()
 char=0 : its=0
 Repeat
  ;random by default
  satisfied=1 : its=its+1
  char=Rnd(1,no_chars)
  If fed>0 And gamSchedule(gamDate)=<3 Then char=fedRoster(fed,Rnd(1,fedSize(fed)))
  ;favour designated 
  randy=Rnd(0,6)
  If randy>0 And fed>0 And gamSchedule(gamDate)=<3
   subIts=0
   Repeat
    char=fedRoster(fed,Rnd(1,fedSize(fed))) : subIts=subIts+1   
   Until charRole(char)=3 Or char=fedBooker(7) Or subIts>1000
  EndIf
  ;career logic
  If game=1 And FindCasted(gamChar)>0 And gamAgreement(15)=0 And gamAgreement(16)=0
   ;risk enemy
   randy=Rnd(0,60)
   If randy=<1
    subIts=0
    Repeat
     char=Rnd(1,no_chars)
     If fed>0 And gamSchedule(gamDate)<4 Then char=fedRoster(fed,Rnd(1,fedSize(fed)))
     subIts=subIts+1
    Until charRelationship(char,gamChar)<0 Or charRelationship(char,gamOpponent(gamDate))<0 Or subIts>100
   EndIf
   ;risk friend
   randy=Rnd(0,60)
   If randy=1
    subIts=0
    Repeat
     char=Rnd(1,no_chars)
     If fed>0 And gamSchedule(gamDate)<4 Then char=fedRoster(fed,Rnd(1,fedSize(fed)))
     subIts=subIts+1
    Until charRelationship(char,gamChar)>0 Or charRelationship(char,gamOpponent(gamDate))>0 Or subIts>100
   EndIf
   ;risk title holder
   If TitleHolder(gamChar,0)=0 And TitleHolder(gamChar,gamOpponent(gamDate))=0
    If randy=2 And charTitles(gamChar,fed,1)=0 
     If charPopularity(gamChar)>charPopularity(fedChampWorld(fed))-5 Or charPopularity(gamChar)=>fedPopularity(fed) Then char=fedChampWorld(fed) ;world champ
    EndIf
    If randy=3 And charTitles(gamChar,fed,2)=0 And charPopularity(gamChar)<charPopularity(fedChampInter(fed)) Then char=fedChampInter(fed) ;inter champ
   EndIf
  EndIf
  ;check suitability
  If charVacant(char)>0 Or TournamentStatus(char)>0 Then satisfied=0 ;avoid unavailable characters
  randy=Rnd(0,2)
  If charRole(char)=3 Then randy=Rnd(0,5)
  If (randy=0 And charHealth(char)<50) Or (randy=<1 And charHealth(char)<25) Or InjuryStatus(char)>0 Or charFed(char)=9 
   satisfied=0 ;favour healthy
  EndIf
  If charLimb(char,40)=0 And charLimb(char,43)=0 Then satisfied=0 ;avoid cripples!
  If gamSchedule(date)=4 And (charFed(char)=charFed(gamChar) Or charFed(char)=gamRivalFed(gamDate)) Then satisfied=0 ;avoid bias 
  For v=1 To no_plays
   If char=gamCastList(v) Or char=charPartner(gamCastList(v)) Or char=charManager(gamCastList(v)) Then satisfied=0 ;avoid already involved
  Next
  If char=0 Then satisfied=0 ;avoid none
 Until satisfied=1 Or its>10000
 ;planned referees
 If game=1 And fed=7 And charExperience(gamChar,fed)=0 Then char=fedBooker(fed)
 Return char
End Function

;FIND CASTED CHARACTER (in career)
Function FindCasted(char)
 value=0
 For v=1 To optPlayLim
  If gamCastList(v)=char Then value=v
 Next
 Return value
End Function

;CONSTRUCT MATCH
Function ConstructMatch()
 ;confirm match size
 If fed>0 And no_wrestlers>fedSize(fed) Then no_wrestlers=fedSize(fed) : no_refs=0
 no_plays=no_wrestlers+no_refs
 If gamCasted=0
  ;reset cast list
  For cyc=1 To optPlayLim
   gamCastList(cyc)=0
  Next
  ;solo assignments
  If matchTeams=<0 
   If matchTeams=-1
    gamCastList(1)=gamChar
    gamCastList(2)=gamOpponent(gamDate)
   Else
    gamCastList(Rnd(1,no_wrestlers))=gamChar
    Repeat
     newbie=Rnd(1,no_wrestlers)
    Until gamCastList(newbie)=0
    gamCastList(newbie)=gamOpponent(gamDate)
   EndIf
   For cyc=1 To no_wrestlers
    If gamCastList(cyc)=0 Then gamCastList(cyc)=AssignOpponent(gamChar,gamDate,1)
   Next
  EndIf
  ;team-mate assignments
  If matchTeams>0 And no_wrestlers=>4 
   For cyc=1 To no_wrestlers
    If cyc=<no_wrestlers/2 Then pTeam(cyc)=1 Else pTeam(cyc)=2
   Next
   If matchPreset=11 Then matchPlayer=1 Else matchPlayer=Rnd(1,no_wrestlers) ;handicap fiddle
   gamCastList(matchPlayer)=gamChar
   If gamPromo(gamDate)=31 Or gamPromo(gamDate)=64 Or gamPromo(gamDate)=98 Or gamPromo(gamDate)=99 ;predetermined team-mates
    matchPlayer=1 : gamCastList(1)=gamChar
    gamCastList(2)=gamPromoVariable(gamDate)
   EndIf
   Repeat
    newbie=Rnd(1,no_wrestlers)
   Until pTeam(newbie)<>pTeam(matchPlayer)
   gamCastList(newbie)=gamOpponent(gamDate)
   For cyc=1 To no_wrestlers
    If gamCastList(cyc)=0 And pTeam(cyc)=pTeam(matchPlayer) Then gamCastList(cyc)=AssignPartner(gamChar,0)
    If gamCastList(cyc)=0 And pTeam(cyc)<>pTeam(matchPlayer) 
     gamCastList(cyc)=AssignPartner(gamOpponent(gamDate),0)
     If gamPromo(gamDate)=90 And FindCasted(gamPromoVariable(gamDate))=0 Then gamCastList(cyc)=gamPromoVariable(gamDate) ;predetermined accomplice
    EndIf
   Next
  EndIf
  ;handicap variation
  If matchTeams>0 And no_wrestlers=3 
   gamCastList(1)=gamChar
   gamCastList(2)=gamOpponent(gamDate)
   gamCastList(3)=AssignPartner(gamOpponent(gamDate),0)
  EndIf
  ;6-way interpromotional
  If gamSchedule(gamDate)=4 And gamRivalFed(gamDate)=0
   For cyc=1 To no_wrestlers
    its=0
    Repeat
     satisfied=1 : its=its+1
     newbie=fedRoster(cyc,Rnd(1,fedSize(cyc))) 
     If charRole(newbie)>1 Or InjuryStatus(newbie)>0 Then satisfied=0
     If TitleHolder(newbie,0)>0 And (TitleHolder(gamChar,0)=0 Or TitleHolder(gamChar,3)) Then satisfied=0
    Until satisfied=1 Or its>1000
    If TitleHolder(gamChar,2) And fedChampInter(cyc)>0 Then newbie=fedChampInter(cyc)
    If TitleHolder(gamChar,1) And fedChampWorld(cyc)>0 Then newbie=fedChampWorld(cyc)
;new belts
If TitleHolder(gamChar,5) And fedChampWomens(cyc)>0 Then newbie=fedChampWomens(cyc)
    If TitleHolder(gamChar,6) And fedChampUS(cyc)>0 Then newbie=fedChampUS(cyc)
If TitleHolder(gamChar,7) And fedChampTV(cyc)>0 Then newbie=fedChampTV(cyc)
   
;end
    If cyc=charFed(gamChar) Then newbie=gamChar
    gamCastList(cyc)=newbie
   Next
  EndIf
  ;add referees
  If no_refs>0
   For cyc=1 To no_refs
    newbie=AssignReferee()
    gamCastList(no_wrestlers+cyc)=newbie
   Next
  EndIf
  gamCasted=1
 EndIf
 ;restore casted characters
 For cyc=1 To optPlayLim
  pChar(cyc)=gamCastList(cyc)
  pControl(cyc)=0
 Next
End Function

;FIND SUITABLE PROMO FOR MATCH
Function AssignPromo()
 matchPromo=gamPromo(gamDate)
 If matchPromo=0
  ;random talking points
  If charRelationship(gamChar,gamOpponent(gamDate))=<0 And charFed(gamChar)=<6
   chance=12
   If charRelationship(gamChar,gamOpponent(gamDate))<0 Then chance=chance/2
   randy=Rnd(0,chance)
   If randy=<1 And (no_wrestlers=2 Or matchTeams>0) Then matchPromo=94 ;stat comparison
   If randy=2 Then matchPromo=85 ;city reference
  EndIf
  ;allegiance appeal
  If charFed(gamChar)=<6
   randy=Rnd(0,10)
   If randy=0 And charHeel(gamChar)=0 And charHeel(gamOpponent(gamDate))=1 Then matchPromo=86 ;try to turn bad 
   If randy=1 And charHeel(gamChar)=1 And charHeel(gamOpponent(gamDate))=0 Then matchPromo=87 ;try to turn good
  EndIf
  ;title shot
  If matchChamps>0 And charFed(gamChar)=<6
   randy=Rnd(0,12)
   If (randy=0 Or promoLocked(21)) And matchTeams=0 And gamOpponent(gamDate)=fedChampInter(charFed(gamChar)) Then matchPromo=21
   If (randy=1 Or promoLocked(20)) And matchTeams=0 And gamOpponent(gamDate)=fedChampWorld(charFed(gamChar)) Then matchPromo=20
;new belts
If (randy=4 Or promoLocked(21)) And matchTeams=0 And gamOpponent(gamDate)=fedChampUS(charFed(gamChar)) Then matchPromo=21
   If (randy=5 Or promoLocked(20)) And matchTeams=0 And gamOpponent(gamDate)=fedChampTV(charFed(gamChar)) Then matchPromo=20


   If (randy=2 Or promoLocked(22)) And matchTeams>0 And TitleHolder(gamOpponent(gamDate),3) Then matchPromo=22
   If randy=3 And matchTeams=0 
    If gamOpponent(gamDate)=fedChampWorld(charFed(gamChar)) Or gamOpponent(gamDate)=fedChampInter(charFed(gamChar)) Or gamOpponent(gamDate)=fedChampUS(charFed(gamChar)) Or gamOpponent(gamDate)=fedChampTV(charFed(gamChar)) Then matchPromo=76
;end   
EndIf
   ;title defence
   randy=Rnd(0,12)
   If (randy=0 Or promoLocked(18)) And matchTeams=0 And gamChar=fedChampInter(charFed(gamChar)) Then matchPromo=18
   If (randy=1 Or promoLocked(17)) And matchTeams=0 And gamChar=fedChampWorld(charFed(gamChar)) Then matchPromo=17
;new belts
If (randy=5 Or promoLocked(17)) And matchTeams=0 And gamChar=fedChampUS(charFed(gamChar)) Then matchPromo=17
If (randy=6 Or promoLocked(17)) And matchTeams=0 And gamChar=fedChampTV(charFed(gamChar)) Then matchPromo=17
;end


   If (randy=2 Or promoLocked(19)) And matchTeams>0 And TitleHolder(gamChar,3) Then matchPromo=19  
  EndIf
  ;heated words with existing rival
  If charRelationship(gamChar,gamOpponent(gamDate))<0 And charFed(gamChar)=<6
   randy=Rnd(0,1)
   If randy=0 And matchRules=2 And matchPreset=>3 And matchPreset=<7 Then matchPromo=34 ;technical reference
   If randy=0 And matchRules=0 Or (gamGimmick(gamDate)=>1 And gamGimmick(gamDate)=<8) Then matchPromo=35 ;hardcore reference
   If randy=0 And matchCage>0 Then matchPromo=36 ;cage reference
   If charPartner(gamChar)=0 And matchTeams>0 And no_wrestlers=4 Then matchPromo=63 ;guest partner
   randy=Rnd(0,10)
   If randy=0 And charSalary(gamChar)>0 Then matchPromo=7 ;accuse of wasting money
   If randy=1 Then matchPromo=8 ;insult costume
   If randy=2 Then matchPromo=9 ;insult name
   If randy=3 Then matchPromo=38 ;insult moves
   If randy=4 And charHistory(gamChar,gamOpponent(gamDate))<>0 Then matchPromo=54 ;bury the hatchet
   If randy=5 Or (randy=0 And promoLocked(37))
    If GetValue(gamChar)<GetValue(gamOpponent(gamDate)) Then matchPromo=37 ;superior opponent
   EndIf
   If randy=6 Or (randy=1 And promoLocked(60))
    If charAge(gamChar)>charAge(gamOpponent(gamDate)) Then matchPromo=60 ;younger opponent
   EndIf
   If randy=7 Or (randy=2 And promoLocked(61))
    If charToughness(gamChar)<charToughness(gamOpponent(gamDate)) Then matchPromo=61 ;tougher opponent
   EndIf
   If randy=8 Or promoLocked(72) 
    If gamOpponent(gamDate)=fedBooker(charFed(gamChar)) And charFed(gamChar)=<6 Then matchPromo=72 ;facing the boss!
   EndIf
   If randy=9 Or promoLocked(84) 
    If charGender(gamChar)=1 And charGender(gamOpponent(gamDate))=0 Then matchPromo=84 ;sexism
   EndIf 
   If randy=10 Or (randy=3 And promoLocked(102))
    If charPopularity(gamChar)>charPopularity(gamOpponent(gamDate)) Then matchPromo=102 ;internet following
   EndIf
  EndIf
  ;facing friend
  If charRelationship(gamChar,gamOpponent(gamDate))>0 ;Or charHeel(gamChar)=charHeel(gamOpponent(gamDate))
   If charHistory(gamChar,gamOpponent(gamDate))=0 And gamAgreement(11)=0 And gamAgreement(12)=0 
    randy=Rnd(0,2)
    If randy=0 Or promoLocked(23) Then matchPromo=23
   EndIf
  EndIf
  ;acknowledge match stipulations
  If matchPreset=11 Then matchPromo=77 ;handicap reference
  If matchChamps=6 Then matchPromo=69 ;hair versus hair
  If matchChamps=7 Then matchPromo=68 ;loser leaves town
  If gamGimmick(gamDate)=11 Then matchPromo=71 ;empty arena
  If gamGimmick(gamDate)=12 Then matchPromo=76 ;race against time  
  If gamGimmick(gamDate)=13 Then matchPromo=95 ;additional referee
  ;injury return
  randy=Rnd(0,1)
  If randy=0 And charFed(gamChar)=<6 And InjuryStatus(gamChar)=0
   If charRelationship(gamChar,gamOpponent(gamDate))<0 Or (charRelationship(gamChar,gamOpponent(gamDate))=<0 And charHeel(gamChar)<>charHeel(gamOpponent(gamDate)))
    comeback=0
    For count=1 To 4
     date=gamDate-count
     If date>0
      If gamSchedule(date)=-1 And gamSchedule(date-1)=-1 Then comeback=1
     EndIf
    Next
    If comeback=1 Then matchPromo=27
   EndIf
  EndIf
  ;farewell
  randy=Rnd(0,1)
  If randy=0 And charFed(gamChar)=<6 And charContract(gamChar)=1 
   If charHeel(gamChar)=1 Then matchPromo=42 Else matchPromo=13
  EndIf
  ;welcome to fed
  randy=Rnd(0,1)
  If randy=0 And charFed(gamChar)=<6 And charMatches(gamChar,charFed(gamChar))=0
   If charHeel(gamOpponent(gamDate))=0 And charRelationship(gamOpponent(gamDate),gamChar)=>0 Then matchPromo=6 Else matchPromo=5
  EndIf
  ;referee issues
  randy=Rnd(0,1)
  If randy=0 And no_refs>0 And charRole(pChar(no_wrestlers+1))<>3 And pChar(no_wrestlers+1)<>fedBooker(7) And charFed(gamChar)=<6
   randy=Rnd(0,1)
   If (randy=0 Or promoLocked(28)) And matchPromo=0 Then matchPromo=28 ;guest referee
   If matchTeams=0 And NextDate()>gamDate And charFed(gamChar)=<6 And TitleHolder(gamChar,0)=0 
    If pChar(no_wrestlers+1)=fedChampInter(charFed(gamChar)) Then matchPromo=41 ;inter contendership
    If pChar(no_wrestlers+1)=fedChampWorld(charFed(gamChar)) Then matchPromo=33 ;world contendership
;new belts
If pChar(no_wrestlers+1)=fedChampUS(charFed(gamChar)) Then matchPromo=41 ;US contendership
    If pChar(no_wrestlers+1)=fedChampTV(charFed(gamChar)) Then matchPromo=33 ;TV contendership
;end
   EndIf
   If charRelationship(gamChar,pChar(no_wrestlers+1))<0 Then matchPromo=15 ;corrupt referee 
  EndIf
 EndIf
 ;special events
 randy=Rnd(0,1)
 If (randy=0 Or matchPromo=0) And matchPromo<>29
  randy=Rnd(0,1)
  If randy=0 And no_refs>0 And gamSchedule(gamDate)=3 And cupFoc(cupSlot)>1 And matchPromo=0 Then matchPromo=92 ;tournament fixture
  If gamSchedule(gamDate)=3 And cupFoc(cupSlot)=1 And gamPromo(gamDate)=0 Then matchPromo=93 ;tournament final
  If no_refs>0 And gamSchedule(gamDate)=4
   If gamRivalFed(gamDate)>0
    If matchTeams>0 Then matchPromo=53 Else matchPromo=12 ;2-way inter-promotional
   Else 
    matchPromo=91 ;6-way inter-promotional 
   EndIf
  EndIf
  If gamSchedule(gamDate)=5 Then matchPromo=11 ;charity
  If gamSchedule(gamDate)=6 Then matchPromo=10 ;tribute 
 EndIf
 ;match summary if nothing else
 If matchPreset=>3 And matchPreset<>12 And (matchPromo=0 Or matchPromo=28)
  randy=Rnd(0,6)
  If randy=<1 And no_refs>0 Then matchPromo=16
  If (randy=2 Or (randy=1 And no_refs=0)) And no_wrestlers=2 Then matchPromo=80
 EndIf
 ;negate promo if used recently
 For count=1 To 4
  date=gamDate-count
  If date>0
   If matchPromo=gamPromo(date) Then matchPromo=0
  EndIf
 Next
End Function

;ASSESS TOTAL INJURY TIME (of character)
Function InjuryStatus(char)
 value=0
 For count=0 To 5
  If charInjured(char,count)>value Then value=charInjured(char,count)
 Next
 Return value
End Function

;ASSESS OLD INJURY STATUS
Function OldInjuryStatus(char)
 value=0
 For count=0 To 5
  If charOldInjured(char,count)>value Then value=charOldInjured(char,count)
 Next
 Return value
End Function

;IDENTIFY INJURY TYPE
Function IdentifyInjury(char)
 value=0
 For count=0 To 5
  If charInjured(char,count)>0 Then value=count
 Next
 Return value
End Function

;INJURED ON DATE?
Function InjuryDate(date)
 value=0
 If gamSchedule(date)=>0 And InjuryStatus(gamChar)>0 And gamAgreement(10)=0
  If date=>gamDate And date=<gamDate+(InjuryStatus(gamChar)-1) Then value=1
 EndIf
 If gamSchedule(date)=-1 Then value=1
 Return value
End Function

;COUNT CAREER EXPERIENCE
Function CountExperience(char,promotion)
 value=0
 If promotion>0
  value=charExperience(char,promotion)
 Else
  For count=1 To 9
   value=value+charExperience(char,count)
  Next
 EndIf
 Return value
End Function

;COUNT MATCHES
Function CountMatches(char,promotion)
 value=0
 If promotion>0
  value=charMatches(char,promotion)
 Else
  For count=1 To 9
   value=value+charMatches(char,count)
  Next
 EndIf
 Return value
End Function

;COUNT WINS
Function CountWins(char,promotion)
 value=0
 If promotion>0
  value=charWins(char,promotion)
 Else
  For count=1 To 9
   value=value+charWins(char,count)
  Next
 EndIf
 Return value
End Function

;CALCULATE WIN RATE
Function GetWinRate(char,promotion)
 If promotion>0
  value=Int(GetPercent#(charWins(char,promotion),charMatches(char,promotion)))
 Else
  value=Int(GetPercent#(CountWins(char,0),CountMatches(char,0)))
 EndIf
 Return value
End Function

;COUNT TITLES
Function CountTitles(char,promotion)
 value=0
 For title=1 To 7 ;new belts orig = 4
  If promotion>0
   value=value+charTitles(char,promotion,title)
  Else
   For count=1 To 9
    value=value+charTitles(char,count,title)
   Next
  EndIf
 Next 
 Return value
End Function

;COUNT FRIENDS
Function CountRelationships(char,task) ;-1=enemies, 1=friends
 value=0
 For v=1 To no_chars
  If char<>v And charFed(v)=<8
   If task>0 And charRelationship(char,v)>0 Then value=value+1
   If task<0 And charRelationship(char,v)<0 Then value=value+1
  EndIf
 Next 
 Return value
End Function

;COUNT FACES/HEELS IN FED
Function AllegianceRatio(cyc,style)
 value=0
 For count=1 To fedSize(cyc)
  char=fedRoster(cyc,count)
  If charHeel(char)=style And charRole(char)<>3 Then value=value+1
 Next 
 Return value
End Function

;CALCULATE FED SALARIES
Function CountSalaries(cyc)
 value=0
 For count=1 To fedSize(cyc)
  char=fedRoster(cyc,count)
  value=value+charSalary(char)
 Next
 Return value
End Function

;HOLDING ANY TITLES?
Function TitleHolder(char,title) ;0=most prestigious, 1=find world, 2=find inter, 3=find tags
 ;return most prestigious
 value=0 
 If title=0
  If fedChampTag(charFed(char),1)=char Or fedChampTag(charFed(char),2)=char Then value=3
  If fedChampInter(charFed(char))=char Then value=2
  If fedChampWorld(charFed(char))=char Then value=1
;new belts
If fedChampWomens(charFed(char))=char Then value=5
  If fedChampUS(charFed(char))=char Then value=6
If fedChampTV(charFed(char))=char Then value=7
 EndIf
 ;find specific title
 If title=1 And fedChampWorld(charFed(char))=char Then value=1
 If title=2 And fedChampInter(charFed(char))=char Then value=1
If title=5 And fedChampWomens(charFed(char))=char Then value=1
If title=6 And fedChampUS(charFed(char))=char Then value=1
If title=7 And fedChampTV(charFed(char))=char Then value=1
;end
 If title=3 
  If fedChampTag(charFed(char),1)=char Or fedChampTag(charFed(char),2)=char Then value=1
 EndIf
 Return value
End Function