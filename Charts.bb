;//////////////////////////////////////////////////////////////////////////////
;-------------------------- WRESTLING MPIRE 2008: CHARTS ----------------------
;//////////////////////////////////////////////////////////////////////////////

;//////////////////////////////////////////////////////////////////////////////
;--------------------------- 24. FINANCIAL REPORT -----------------------------
;//////////////////////////////////////////////////////////////////////////////
Function FinanceReport()
;reset working figures
For count=1 To 7
 gamFinances(count,0)=0
 gamFinances(count,1)=0
Next
;calculate show revenue
gamTicket=0
If gamSchedule(gamDate)=1 Then gamTicket=20
If gamSchedule(gamDate)=>2 Then gamTicket=15
If gamSchedule(gamDate)=3
 If arenaPreset=>11 Then gamTicket=15 Else gamTicket=20
EndIf
If charFed(gamChar)=7
 If gamSchedule(gamDate)>1 Then gamTicket=5 Else gamTicket=0
EndIf
If gamSchedule(gamDate)=<0 Then gamAttendance(gamDate)=0
gamFinances(1,1)=gamAttendance(gamDate)*gamTicket
;calculate earnings
gamFinances(2,1)=0
If gamSchedule(gamDate)>0
 gamFinances(2,1)=charSalary(gamChar)
 If gamResult(gamDate)<>3 And charClause(gamChar,2)=0 Then gamFinances(2,1)=0
 If gamResult(gamDate)<>3 And charClause(gamChar,2)=1 Then gamFinances(2,1)=gamFinances(2,1)/2
 If arenaPreset=>11 Then gamFinances(2,1)=PercentOf#(gamFinances(2,1),125)
 If gamSchedule(gamDate)=5 Or gamSchedule(gamDate)=6 Then gamFinances(2,1)=0
 ;If screenAgenda=14 And pTeam(matchPlayer)=pTeam(matchWinner) Then gamFinances(2,1)=gamFinances(2,1)+negPayOff
 ;If screenAgenda=13 Then gamFinances(2,1)=gamFinances(2,1)+negPayOff
EndIf
;gimmick costs
gamFinances(3,1)=gamGimmickChanges
If charWeapon(gamChar)>0 And gamAgreement(17)=0 Then gamFinances(3,1)=gamFinances(3,1)+weapValue(charWeapon(gamChar))
gamGimmickChanges=0
;management fees
gamFinances(4,1)=0
If charManager(gamChar)>0
 gamFinances(4,1)=PercentOf#(gamFinances(2,1),10)
 If gamFinances(4,1)<100 Then gamFinances(4,1)=100
EndIf
;calculate living costs
gamFinances(5,1)=charBank(gamChar)/20
If gamFinances(5,1)<charSalary(gamChar)/4 Then gamFinances(5,1)=charSalary(gamChar)/4
If gamFinances(5,1)<100 Then gamFinances(5,1)=100
If gamFinances(5,1)>10000 Then gamFinances(5,1)=10000
;If charAge(gamChar)<18 Then gamFinances(5,1)=gamFinances(5,1)/2
;health compensation
If gamSchedule(gamDate)=-1 
 If charClause(gamChar,3)=1 Then gamFinances(2,1)=gamFinances(5,1)
 If charClause(gamChar,3)=2 
  gamFinances(2,1)=charSalary(gamChar)
  If gamFinances(2,1)<gamFinances(5,1) Then gamFinances(2,1)=gamFinances(5,1)
 EndIf
EndIf
;calculate profit
gamFinances(6,1)=gamFinances(2,1)-(gamFinances(3,1)+gamFinances(4,1)+gamFinances(5,1))
oldBank=charBank(gamChar)
gamFinances(7,0)=charBank(gamChar)
gamFinances(7,1)=charBank(gamChar)+gamFinances(6,1)
;frame rating
timer=CreateTimer(30)
;MAIN LOOP
foc=0
go=0 : gotim=-20 : keytim=10
While go=0

 Cls
 frames=WaitTimer(timer)
 For framer=1 To frames
	
	;timers
	keytim=keytim-1
	If keytim<1 Then keytim=0
	
	;PROCESS
    gotim=gotim+1
	;initial subject
	If gotim>20 And foc=0 Then PlaySound sMenuSelect : foc=1
	If gotim>30 And keytim=0
	 ;develop figures
	 For cyc=1 To 7
	  If foc=cyc And keytim=0
	   If gamFinances(cyc,0)<>gamFinances(cyc,1) 
	    ProduceSound(0,sMenuBrowse,0,0.25) : keytim=1
	    gamFinances(cyc,0)=gamFinances(cyc,0)+PursueFigure(gamFinances(cyc,0),gamFinances(cyc,1))
	   EndIf
	   If gamFinances(cyc,0)=gamFinances(cyc,1) 
	    foc=foc+1 : keytim=10
	    If gamFinances(cyc,1)=0 Then PlaySound sMenuSelect Else PlaySound sCash
	    If foc>7 Then charBank(gamChar)=gamFinances(7,1)
	   EndIf
	  EndIf
	 Next
	 ;leave
	 If foc>7 And keytim=0
	  If KeyDown(1) Or KeyDown(28) Or ButtonPressed() Or MouseDown(1) Then go=1
	 EndIf
	EndIf
	;speed up
	If foc=>1 And foc=<7 And keytim>0
	 If KeyDown(1) Or KeyDown(28) Or ButtonPressed() Or MouseDown(1) Then keytim=keytim/2
	EndIf
	;paper thud
	If gotim=0 Then PlaySound sPaper 
	;music
	ManageMusic(-1) 
	
 UpdateWorld
 Next 
 RenderWorld 1

 ;DISPLAY
 DrawImage gBackground,rX#(400),rY#(300)
 DrawImage gFed(charFed(gamChar)),rX#(400),rY#(60)
 ;stat reminder
 If gotim>10 Then DrawProfile(gamChar,-1,-1,0)
 ;REPORT
 If gotim>0
  x=rX#(400) : y=rY#(390)
  DrawImage gReport,x,y
  ;dateline
  SetFont fontNews(1)
  Outline(DescribeDate$(gamDate,gamYear),x+105,y-153,225,220,215,100,100,100)
  ;items
  For count=-2 To 7
   ;positioning
   showX=x+15
   If count=-2 Then showY=y-105
   If count=-1 Then showY=y-85
   If count=0 Then showY=y-65
   If count=1 Then showY=y-45
   If count=2 Then showY=y-5
   If count=3 Then showY=y+15
   If count=4 Then showY=y+35
   If count=5 Then showY=y+55
   If count=6 Then showY=y+95
   If count=7 Then showY=y+115
   ;headers
   If count=-2 
    header$="PROMOTION:"
    If fed>0 Then sum$=fedName$(fed) Else sum$="Various"
   EndIf
   If count=-1 
    header$="EVENT:" : sum$="None"
    If gamSchedule(gamDate)>0 Then sum$=textEvent$(gamSchedule(gamDate))
    If fed=7 And gamSchedule(gamDate)=1 Then sum$="Training Session"
    If fed=7 And gamSchedule(gamDate)=2 Then sum$="Student Showcase"
   EndIf
   If count=0
    header$="ATTENDANCE:" : sum$="N/A"
    If gamSchedule(gamDate)>0 Then sum$=GetFigure$(gamAttendance(gamDate))+" x $"+gamTicket
   EndIf
   If count>0
    sum$="$"+GetFigure$(gamFinances(count,0))
    If count=1 Then header$="REVENUE:"
    If count=2
     If gamSchedule(gamDate)=-1 Then header$="COMPENSATION:" Else header$="EARNINGS:"
    EndIf
    If count=3 Then header$="GIMMICK CHANGES:"
    If count=4 Then header$="MANAGEMENT FEES:"
    If count=5 Then header$="LIFESTYLE COSTS:"
    If count=6 Then header$="PROFIT:"
    If count=7 Then header$="BANK BALANCE:"
   EndIf
   ;highlighter
   SetFont fontNews(2)
   If foc>0 And foc=count
    Color 150,150,150
    Rect (showX-5)-StringWidth(header$),showY-7,(StringWidth(header$)+5)+(StringWidth(sum$)+5),16,0
    Color 0,0,0
    Rect (showX-7)-StringWidth(header$),showY-9,(StringWidth(header$)+5)+(StringWidth(sum$)+9),20,0
   EndIf
   Color 175,175,175
   Line showX-110,y-25,showX+90,y-25
   Line showX-110,y+75,showX+90,y+75
   ;display text
   OutlineStraight(header$,(showX-3)-StringWidth(header$),showY,225,220,215,50,50,50)
   r=50 : g=50 : b=50
   If count>0
    If gamFinances(count,0)>0 Then r=0 : g=150 : b=0
    If gamFinances(count,0)<0 Or (count=>3 And count=<5 And gamFinances(count,0)>0) Then r=150 : g=0 : b=0
    If count=7
     r=50 : g=50 : b=50
     If gamFinances(count,0)>oldBank Then r=0 : g=150 : b=0
     If gamFinances(count,0)<oldBank Then r=150 : g=0 : b=0
    EndIf
   EndIf
   OutlineStraight(sum$,showX+3,showY,225,220,215,r,g,b)
   ;explanations
   If count=2 And foc>count
    explain$="" 
    If gamSchedule(gamDate)>0 And gamResult(gamDate)<>3 And charSalary(gamChar)>0 Then explain$="("+textClause$(2,charClause(gamChar,2))+")"
    If gamFinances(2,1)>charSalary(gamChar) Then explain$="(Bonus!)" 
    If gamSchedule(gamDate)=5 Or gamSchedule(gamDate)=6 Then explain$="(Donated)" 
    If gamSchedule(gamDate)=-1 Then explain$="("+textClause$(3,charClause(gamChar,3))+")"
    stringer=StringWidth(sum$)
    SetFont fontNews(0) 
    OutlineStraight(explain$,(showX+3)+(stringer+5),showY,225,220,215,100,100,100)
   EndIf
  Next
  ;prompt
  If foc>7 And gotim>50
   SetFont font(2)
   Outline(">>> PRESS ANY COMMAND TO PROCEED >>>",x+10,y+190,120,120,120,250,250,250)
  EndIf
 EndIf
 ;cursor
 DrawImage gCursor,MouseX(),MouseY()

 Flip
 ;screenshot (F12)
 If KeyHit(88) Then Screenshot()

Wend
;leave
If go=-1 Then PlaySound sMenuBack Else PlaySound sMenuGo
FreeTimer timer
screen=22
End Function

;----------------------------------------------------------------------
;///////////////////// 25. RETIREMENT SUMMARY /////////////////////////
;----------------------------------------------------------------------
Function Retirement()
;get working values
hiWealth(0)=charBank(gamChar)
hiTitles(0)=CountTitles(gamChar,0)
hiSuccess(0)=GetWinRate(gamChar,0)
hiTalent(0)=AverageStats(gamChar)
hiExperience(0)=CountExperience(gamChar,0)
;newpaper identity
eNewspaper(no_events)=Rnd(1,1)
eBackground(no_events)=Rnd(1,12)
eAdvert(no_events,1)=Rnd(1,9)
Repeat
 eAdvert(no_events,2)=Rnd(1,9)
Until eAdvert(no_events,2)<>eAdvert(no_events,1)
;frame rating
timer=CreateTimer(30)
;MAIN LOOP
go=0 : gotim=-10 : keytim=0
While go=0
 
 Cls
 frames=WaitTimer(timer)
 For framer=1 To frames
	
	;PORTAL
    gotim=gotim+1
	If gotim>100 
	 If KeyDown(1) Or KeyDown(28) Or ButtonPressed() Or MouseDown(1) Then go=1
	EndIf 
	;reaction
	If gotim=0 
	 PlaySound sPaper
	 If hiWealth(0)<1000 Then PlaySound sCrowd(8)
	 If hiWealth(0)=>1000 And hiWealth(0)<10000 Then PlaySound sCrowd(7)
	 If hiWealth(0)=>10000 And hiWealth(0)<50000 Then PlaySound sCrowd(4)
	 If hiWealth(0)=>50000 And hiWealth(0)<100000 Then PlaySound sCrowd(2)
	 If hiWealth(0)=>100000 Then PlaySound sCrowd(6) : PlaySound sCrowd(9)
	EndIf
	;music
	ManageMusic(-1) 
	
 UpdateWorld
 Next 
 RenderWorld 1

 ;DISPLAY
 DrawImage gBackground,rX#(400),rY#(300)  
 DrawImage gLogo(2),rX#(400),rY#(60)  
 ;show profiles
 If gotim>10 Then DrawProfile(gamChar,-1,-1,0)
 ;CONSTRUCT NEWSPAPER
 If gotim>0
  ;images
  x=rX#(400) : y=rY#(370)
  DrawImage gNewspaper,x,y
  DrawImage gNewsIdentity(1),x-158,y-165
  DrawImage gNewsAdvert(1),x+42,y-165
  DrawImage gNewsAdvert(2),x+201,y-165
  DrawImage gNewsScene(1),x+146,y+40 
  DrawImage charPhoto(gamChar),x+145,y+34
  ;small print
  SetFont fontNews(0) : Color 110,110,110
  Text x-268,y-107,"The Nation's #1 Newspaper",0,1
  Text x+40,y-107,DescribeDate$(gamDate,gamYear),1,1
  Text x+235,y-107,"50 Cents",0,1
  ;headline
  SetFont fontNews(10) : Color 0,0,0
  Text x+5,y-63,"GAME OVER",1,1
  ;career summary
  g=charGender(gamChar)
  SetFont fontNews(3) : Color 0,0,0
  If charFed(gamChar)=9
   Text x+5,y+125,"After "+hiExperience(0)+" weeks in the wrestling business, "+charName$(gamChar),1,1
   Text x+5,y+150,"has died at the age of "+charAge(gamChar)+" - leaving a fortune of $"+GetFigure$(hiWealth(0))+".",1,1
   Text x+5,y+175,He$(g)+" won "+hiSuccess(0)+"% of "+Lower$(His$(g))+" matches and held "+hiTitles(0)+" titles...",1,1
  Else
   Text x+5,y+125,"After "+hiExperience(0)+" weeks, "+charName$(gamChar)+" has retired from",1,1
   Text x+5,y+150,"the wrestling business with a fortune of $"+GetFigure$(hiWealth(0))+".",1,1
   Text x+5,y+175,He$(g)+" won "+hiSuccess(0)+"% of "+Lower$(His$(g))+" matches and held "+hiTitles(0)+" titles...",1,1
  EndIf
  ;prompt
  If gotim>100
   SetFont font(2)
   Outline(">>> PRESS ANY COMMAND TO PROCEED >>>",x,y+210,130,130,130,255,255,255)
  EndIf
 EndIf
 ;cursor
 DrawImage gCursor,MouseX(),MouseY()

 Flip
 ;screenshot (F12)
 If KeyHit(88) Then Screenshot()

Wend
;leave
Loader("Please Wait","Saving Career")
PlaySound sMenuGo
FreeTimer timer
;find hall of fame slot
hiInduct=0
For cyc=1 To 10
 If hiChar(cyc)=0 Then hiInduct=cyc
Next
If hiInduct=0
 hi=999999999 : loser=0
 For cyc=1 To 10
  If hiWealth(cyc)<hi Then hi=hiWealth(cyc) : loser=cyc
 Next
 hiInduct=loser
EndIf
;transfer details
If hiInduct>0
 hiChar(hiInduct)=gamChar
 hiName$(hiInduct)=charName$(gamChar)
 hiPhoto(hiInduct)=charPhoto(gamChar)
 hiPhotoHeight#(hiInduct)=PortraitHead#(gamChar)
 hiPhotoR(hiInduct)=charPhotoR(gamChar)
 hiPhotoG(hiInduct)=charPhotoG(gamChar)
 hiPhotoB(hiInduct)=charPhotoB(gamChar)
 MaskImage hiPhoto(hiInduct),hiPhotoR(hiInduct),hiPhotoG(hiInduct),hiPhotoB(hiInduct)
 SaveImage(hiPhoto(hiInduct),"Data/Hall Of Fame/Photo"+Dig$(hiInduct,10)+".bmp")
 hiWealth(hiInduct)=hiWealth(0)
 hiTitles(hiInduct)=hiTitles(0)
 hiSuccess(hiInduct)=hiSuccess(0)
 hiTalent(hiInduct)=hiTalent(0)
 hiExperience(hiInduct)=hiExperience(0)
 hiFed(hiInduct)=charFed(gamChar) 
EndIf
;move to manager ppool
If charFed(gamChar)=<7 Then MoveChar(gamChar,8)
slotActive(slot)=0
hiChar(0)=0
SaveUniverse()
;proceed
screen=26
End Function

;----------------------------------------------------------------------
;/////////////////////// 26. HALL OF FAME /////////////////////////////
;----------------------------------------------------------------------
Function HallOfFame()
;initiate list
If hiInduct>0 Then PlaySound sCrowd(9)
CareerRankings(1)
;frame rating
timer=CreateTimer(30)
;MAIN LOOP
foc=3 : oldfoc=foc : page=1
go=0 : gotim=0 : keytim=10
While go=0

 Cls
 frames=WaitTimer(timer)
 For framer=1 To frames
	
	;counters
	keytim=keytim-1
	If keytim<1 Then keytim=0
	flashTim=flashTim+1
	If flashTim>30 Then flashTim=0 
	
	;PORTAL
    gotim=gotim+1
	If gotim>20 
	 ;quit
	 If KeyDown(1) Then go=-1 
	 ;proceed
	 If KeyDown(28) Or ButtonPressed() Or (MouseDown(1) And foc=3) Then go=1
	EndIf
	;music
	ManageMusic(-1) 
	  
	;CONFIGURE 
	oldPage=page
	If gotim>20 And keytim=0
	 ;change category
	 mousy=0
	 If MouseX()>rX#(100) And MouseX()<rX#(700) And MouseY()>rY#(115) And MouseY()<rY#(525) Then mousy=1
	 If (mousy=1 And MouseDown(2) And foc=0) Or KeyDown(203) Or JoyXDir()=-1 Then page=page-1 : PlaySound sMenuBrowse : keytim=6 
	 If (mousy=1 And MouseDown(1) And foc=0) Or KeyDown(205) Or JoyXDir()=1 Then page=page+1 : PlaySound sMenuBrowse : keytim=6  
	 ;reset entries
	 If KeyDown(29) And KeyDown(19)
	  Loader("Please Wait","Populating List")
	  PlaySound sTrash : keytim=10 
	  For count=1 To 10
       hiChar(count)=Rnd(1,no_chars)
       hiName$(count)=charName$(hiChar(count))
       hiPhoto(count)=charPhoto(hiChar(count))
 	   hiPhotoHeight#(count)=PortraitHead#(hiChar(count))
 	   hiPhotoR(count)=charPhotoR(hiChar(count))
 	   hiPhotoG(count)=charPhotoG(hiChar(count))
 	   hiPhotoB(count)=charPhotoB(hiChar(count))
 	   MaskImage hiPhoto(count),hiPhotoR(count),hiPhotoG(count),hiPhotoB(count)
 	   SaveImage(hiPhoto(count),"Data/Hall Of Fame/Photo"+Dig$(count,10)+".bmp")     
       hiWealth(count)=count*100
       hiTitles(count)=0
       hiSuccess(count)=count     
       hiTalent(count)=count*5
       hiExperience(count)=count
       hiFed(count)=charFed(hiChar(count))
      Next 
      CareerRankings(page)
	 EndIf
	EndIf
	;limits
	If page<1 Then page=5
	If page>5 Then page=1
    ;update list
	If page<>oldPage 
	 CareerRankings(page)
    EndIf
	
 UpdateWorld
 Next
 RenderWorld 1

 ;DISPLAY
 DrawImage gBackground,rX#(400),rY#(300)
 DrawImage gLogo(2),rX#(400),rY#(60)
 ;RANKINGS
 x=165 : y=173
 For count=1 To 10
  cyc=hiRank(count)
  ;photo
  If hiPhoto(cyc)>0
   If count=1
    DrawImage hiPhoto(cyc),rX#(x),rY#(y)-40 
   Else
    reveal=(64-hiPhotoHeight#(cyc))+10
    DrawImageRect hiPhoto(cyc),rX#(x),(rY#(y)+10)+(64-reveal),0,0,76,reveal
   EndIf
  EndIf
  ;position
  SetFont fontNews(4)
  If hiChar(cyc)>0 And count=1 Then SetFont fontNews(8) 
  If hiChar(cyc)>0 And count=10 Then SetFont fontNews(3) 
  r=255 : g=230 : b=110
  If hiInduct>0 And cyc=hiInduct Then r=255 : g=Rnd(150,250) : b=130
  If hiChar(cyc)>0 Then Outline(count+".",rX#(x+35),rY#(y),0,0,0,r,g,b)
  If hiChar(cyc)=0 Then Outline("-",rX#(x+35),rY#(y),0,0,0,r,g,b)
  ;name
  r=255 : g=255 : b=255
  If hiInduct>0 And cyc=hiInduct Then r=255 : g=Rnd(150,250) : b=130
  If hiChar(cyc)>0 Then namer$=hiName$(cyc) Else namer$=""
  SqueezeFont(namer$,150,18)
  OutlineStraight(namer$,rX#(x+55),rY#(y),0,0,0,r,g,b)
  ;promotion
  r=175 : g=175 : b=175
  If hiInduct>0 And cyc=hiInduct Then r=255 : g=Rnd(150,250) : b=130
  If hiChar(cyc)>0 Then namer$=fedName$(hiFed(cyc)) Else namer$=""
  SqueezeFont(namer$,125,18)
  Outline(namer$,rX#(x+300),rY#(y),0,0,0,r,g,b)
  ;value
  SetFont fontNews(3)
  If page=1 Then namer$="$"+GetFigure$(hiWealth(cyc)) : header$="Wealthiest Careers"
  If page=2 Then namer$=hiTitles(cyc)+" Titles" : header$="Most Decorated Careers"
  If page=3 Then namer$=hiSuccess(cyc)+"% Wins" : header$="Most Successful Careers"
  If page=4 Then namer$=hiTalent(cyc)+"% Overall" : header$="Most Talented Careers"
  If page=5 Then namer$=hiExperience(cyc)+" Weeks" : header$="Longest Careers"
  If hiChar(cyc)=0 Then namer$=""
  r=100 : g=200 : b=100 
  If hiInduct>0 And cyc=hiInduct Then r=255 : g=Rnd(150,250) : b=130
  Outline(namer$,rX#(x+440),rY#(y),0,0,0,r,g,b)
  y=y+37
 Next 
 ;header
 SetFont font(4)
 Outline(header$,rX#(400),rY#(130),50,35,20,250,230,110)
 ;options
 foc=0
 DrawOption(3,rX#(400),rY#(560),"<<< EXIT <<<","")
 ;advice
 ;SetFont font(1)
 ;If foc=1 And flashTim>15
  ;Outline("CLICK TO BROWSE",rX#(80),rY#(320)-10,0,0,0,255,255,255)
  ;Outline("CATEGORIES",rX#(80),rY#(320)+10,0,0,0,255,255,255) 
  ;Outline("CLICK TO BROWSE",rX#(720),rY#(320)-10,0,0,0,255,255,255)
  ;Outline("CATEGORIES",rX#(720),rY#(320)+10,0,0,0,255,255,255) 
 ;EndIf
 ;cursor 
 If foc<>oldfoc Then oldfoc=foc : PlaySound sMenuSelect 
 DrawImage gCursor,MouseX(),MouseY()

 Flip
 ;screenshot (F12)
 If KeyHit(88) Then Screenshot()

Wend
;leave
If go=1 Then PlaySound sMenuGo Else PlaySound sMenuBack
FreeTimer timer
screen=10
If hiInduct>0 Then screen=7
hiInduct=0
End Function

;----------------------------------------------------------------------
;/////////////////////// 28. WORLD LEADERS ////////////////////////////
;----------------------------------------------------------------------
Function WorldLeaders()
;initiate list
fed=0 : page=1
GetRankings(page)
;frame rating
timer=CreateTimer(30)
;MAIN LOOP
foc=3 : oldfoc=foc
go=0 : gotim=0 : keytim=10
While go=0

 Cls
 frames=WaitTimer(timer)
 For framer=1 To frames
	
	;counters
	keytim=keytim-1
	If keytim<1 Then keytim=0
	flashTim=flashTim+1
	If flashTim>50 Then flashTim=0 
	
	;PORTAL
    gotim=gotim+1
	If gotim>20 
	 ;quit
	 If KeyDown(1) Then go=-1 
	 ;proceed
	 If KeyDown(28) Or ButtonPressed() Or MouseDown(1)
	  If foc=3 Then go=1
	 EndIf
	EndIf
	;hotspots
	foc=0 
	If MouseX()>rX#(400)-100 And MouseX()<rX#(400)+100 And MouseY()>rY#(60)-45 And MouseY()<rY#(60)+45 Then foc=1
	If MouseX()>rX#(100) And MouseX()<rX#(700) And MouseY()>rY#(115) And MouseY()<rY#(525) Then foc=2 
	;music
	ManageMusic(-1) 
	
	;CONFIGURE 
	oldPage=page : oldFed=fed
	If gotim>20 And keytim=0
	 ;change category
	 If (MouseDown(2) And foc=1) Or KeyDown(200) Or JoyYDir()=-1 Then fed=fed-1 : PlaySound sMenuBrowse : keytim=8 
	 If (MouseDown(1) And foc=1) Or KeyDown(208) Or JoyYDir()=1 Then fed=fed+1 : PlaySound sMenuBrowse : keytim=8   
	 ;change category
	 If (MouseDown(2) And foc=2) Or KeyDown(203) Or JoyXDir()=-1 Then page=page-1 : PlaySound sMenuBrowse : keytim=5
	 If (MouseDown(1) And foc=2) Or KeyDown(205) Or JoyXDir()=1 Then page=page+1 : PlaySound sMenuBrowse : keytim=5  
	EndIf
	;limits
	;new belts
	If page<1 Then page=38 ;orig = 35
	If page>38 Then page=1 ;orig = 35
	If page=>24 And page=<30 Then fed=0
	If page=>31 And page=<38 ;orig = 35
	;end
	 If fed<1 Then fed=6
	 If fed>6 Then fed=1
	Else
	 If fed<0 Then fed=7
	 If fed>7 Then fed=0 
    EndIf
    ;update list
	If fed<>oldFed Or page<>oldPage 
	 GetRankings(page)
    EndIf
	
 UpdateWorld
 Next
 RenderWorld 1

 ;DISPLAY
 DrawImage gBackground,rX#(400),rY#(300)
 If fed>0
  DrawImage gFed(fed),rX#(400),rY#(60)
 Else
  DrawImage gLogo(2),rX#(400),rY#(60)
 EndIf
 ;RANKINGS
 x=165 : y=173
 For count=1 To 10
  char=fedRank(fed,count)
  If page=31 Then char=fedHistChar(fed,0,count)
  If page=32 Then char=fedHistChar(fed,1,count)
  If page=33 Then char=fedHistChar(fed,2,count)
  If page=34 Then char=fedHistChar(fed,3,count)
  If page=35 Then char=fedHistChar(fed,4,count)
;new belts?
If page=36 Then char=fedHistChar(fed,5,count)
If page=37 Then char=fedHistChar(fed,6,count)
If page=38 Then char=fedHistChar(fed,7,count)
;end
  ;photo
  photoChar=char
  If page=>24 And page=<30 Then photoChar=fedBooker(char)
  If photoChar>0
   If count=1
    DrawImage charPhoto(photoChar),rX#(x),rY#(y)-40 
   Else
    reveal=(64-PortraitHead#(photoChar))+10
    DrawImageRect charPhoto(photoChar),rX#(x),(rY#(y)+10)+(64-reveal),0,0,76,reveal
   EndIf
  EndIf
  ;position
  rank=count
;new belts
  If page=>31 And page=<38 ;orig = 35
;end
   rank=fedHistCount(fed,page-31,count)
  EndIf
  SetFont fontNews(4)
  If char>0 And rank=1 Then SetFont fontNews(8) 
  If char>0 And rank=10 Then SetFont fontNews(3) 
  r=255 : g=230 : b=110
  If char=gamChar Then r=255 : g=Rnd(150,250) : b=130
  If rank>0 And char>0 
   Outline(rank+".",rX#(x+35),rY#(y),0,0,0,r,g,b)
  Else
   Outline("-",rX#(x+35),rY#(y),0,0,0,r,g,b)
  EndIf
  ;name
  r=255 : g=255 : b=255
  If char=gamChar Then r=255 : g=Rnd(150,250) : b=130
  If char>0 Then namer$=charName$(char) Else namer$=""
  If char>0 And page=>24 And page=<30 Then namer$=fedName$(char)
  If char>0 And page=34 And fedHistPartner(fed,3,count)>0 Then namer$=charName$(char)+" & "+charName$(fedHistPartner(fed,3,count))
  SqueezeFont(namer$,150,18)
  If page=34 Then SqueezeFont(namer$,300,18)
  OutlineStraight(namer$,rX#(x+55),rY#(y)+1,0,0,0,r,g,b)
  ;promotion
  r=175 : g=175 : b=175
  If char=gamChar Then r=255 : g=Rnd(150,250) : b=130
  If char>0 Then namer$=fedName$(charFed(char)) Else namer$=""
  If (page=>24 And page=<30) Or page=34 Then namer$=""
  SqueezeFont(namer$,125,18)
;new belts
  If (page=>10 And page=<11) Or (page=>19 And page=<38) Then offset=300 Else offset=305 ;orig = 35
;end
  Outline(namer$,rX#(x+offset),rY#(y)+1,0,0,0,r,g,b)
  ;value
  SetFont fontNews(4)
;new belts
  If (page=>10 And page=<11) Or (page=>19 And page=<38) Then SetFont fontNews(3) ;orig = 35
;end
  If page=1 Then namer$=charPopularity(char)+"%" : header$="Most Popular Wrestlers"
  If page=2 Then namer$=charStrength(char)+"%" : header$="Strongest Wrestlers"
  If page=3 Then namer$=charSkill(char)+"%" : header$="Most Skilful Wrestlers"
  If page=4 Then namer$=charAgility(char)+"%" : header$="Most Agile Wrestlers"
  If page=5 Then namer$=charStamina(char)+"%" : header$="Fittest Wrestlers"
  If page=6 Then namer$=charToughness(char)+"%" : header$="Toughest Wrestlers"
  If page=7 Then namer$=charAttitude(char)+"%" : header$="Most Reliable Wrestlers"
  If page=8 Then namer$=charHappiness(char)+"%" : header$="Happiest Wrestlers"
  If page=9 Then namer$=AverageStats(char)+"%" : header$="Best Overall Wrestlers"
  If page=10 
   namer$=CountMatches(char,0)+" Matches" : header$="Most Experienced Wrestlers"
  EndIf
  If page=11
   namer$=GetWinRate(char,0)+"% Wins" : header$="Most Successful Wrestlers"
  EndIf
  If page=12 Then namer$=charHealth(char)+"%" : header$="Healthiest Wrestlers"
  If page=13 Then namer$=GetHeight$(charHeight(char)) : header$="Shortest Wrestlers"
  If page=14 Then namer$=GetHeight$(charHeight(char)) : header$="Tallest Wrestlers"
  If page=15 Then namer$=TranslateWeight(char)+"lbs" : header$="Lightest Wrestlers"
  If page=16 Then namer$=TranslateWeight(char)+"lbs" : header$="Heaviest Wrestlers"
  If page=17 Then namer$=charAge(char)+"yrs" : header$="Youngest Wrestlers"
  If page=18 Then namer$=charAge(char)+"yrs" : header$="Oldest Wrestlers"
  If page=19 Then namer$=charContract(char)+" Weeks" : header$="Most Committed Wrestlers"
  If page=20 Then namer$="$"+GetFigure$(charSalary(char)) : header$="Highest Paid Wrestlers"   
  If page=21 
   namer$=CountTitles(char,0)+" Titles" : header$="Most Decorated Wrestlers" 
  EndIf
  If page=22 
   namer$=CountRelationships(char,1)+" Friends" : header$="Most Sociable Wrestlers"   
  EndIf
  If page=23 
   namer$=CountRelationships(char,-1)+" Enemies" : header$="Most Hostile Wrestlers" 
  EndIf
  If page=24 Then namer$=charMatches(gamChar,char)+" Matches" : header$=charName$(gamChar)+"'s International Experience"
  If page=25 Then namer$=GetWinRate(gamChar,char)+"% Wins" : header$=charName$(gamChar)+"'s International Success"
  If page=26 Then namer$=charTitles(gamChar,char,1)+" Titles" : header$=charName$(gamChar)+"'s World Title Reigns"
  If page=27 Then namer$=charTitles(gamChar,char,2)+" Titles" : header$=charName$(gamChar)+"'s Inter Title Reigns"
  If page=28 Then namer$=charTitles(gamChar,char,3)+" Titles" : header$=charName$(gamChar)+"'s Tag Title Reigns"
  If page=29 Then namer$=charTitles(gamChar,char,4)+" Trophies" : header$=charName$(gamChar)+"'s Trophy Cabinet"
  If page=30 Then namer$=CountTitles(gamChar,char)+" Titles" : header$=charName$(gamChar)+"'s Combined Title Reigns"
  If page=31 Then namer$=Left$(textMonth$(GetMonth(fedHistDate(fed,0,count))),3)+" "+fedHistYear(fed,0,count) : header$="Management History"
  If page=32 Then namer$=Left$(textMonth$(GetMonth(fedHistDate(fed,1,count))),3)+" "+fedHistYear(fed,1,count) : header$="World Title History"
  If page=33 Then namer$=Left$(textMonth$(GetMonth(fedHistDate(fed,2,count))),3)+" "+fedHistYear(fed,2,count) : header$="Inter Title History"
  If page=34 Then namer$=Left$(textMonth$(GetMonth(fedHistDate(fed,3,count))),3)+" "+fedHistYear(fed,3,count) : header$="Tag Title History"
  If page=35 Then namer$=Left$(textMonth$(GetMonth(fedHistDate(fed,4,count))),3)+" "+fedHistYear(fed,4,count) : header$="Trophy History"

;new belts

If page=36 Then namer$=Left$(textMonth$(GetMonth(fedHistDate(fed,5,count))),3)+" "+fedHistYear(fed,1,count) : header$="Womens Title History"
If page=37 Then namer$=Left$(textMonth$(GetMonth(fedHistDate(fed,6,count))),3)+" "+fedHistYear(fed,1,count) : header$="US Title History"
If page=38 Then namer$=Left$(textMonth$(GetMonth(fedHistDate(fed,7,count))),3)+" "+fedHistYear(fed,1,count) : header$="TV Title History"

;end
 
  If char=0 Then namer$=""
  r=100 : g=200 : b=100 
  If char=gamChar Then r=255 : g=Rnd(150,250) : b=130
  Outline(namer$,rX#(x+440),rY#(y),0,0,0,r,g,b)
  y=y+37
 Next 
 ;header
 SetFont fontNews(4)
 Outline(header$+":",rX#(400),rY#(130),50,35,20,250,230,110)
 ;options
 DrawOption(3,rX#(400),rY#(560),"<<< EXIT <<<","")
 ;advice
 ;SetFont font(1)
 ;If foc=1 And flashTim>15
  ;Outline("CLICK TO BROWSE",rX#(400)-160,rY#(60)-10,0,0,0,255,255,255)
  ;Outline("ROSTERS",rX#(400)-160,rY#(60)+10,0,0,0,255,255,255) 
  ;Outline("CLICK TO BROWSE",rX#(400)+160,rY#(60)-10,0,0,0,255,255,255)
  ;Outline("ROSTERS",rX#(400)+160,rY#(60)+10,0,0,0,255,255,255) 
 ;EndIf
 ;If foc=2 And flashTim>15
  ;Outline("CLICK TO BROWSE",rX#(80),rY#(320)-10,0,0,0,255,255,255)
  ;Outline("CATEGORIES",rX#(80),rY#(320)+10,0,0,0,255,255,255) 
  ;Outline("CLICK TO BROWSE",rX#(720),rY#(320)-10,0,0,0,255,255,255)
  ;Outline("CATEGORIES",rX#(720),rY#(320)+10,0,0,0,255,255,255) 
 ;EndIf
 ;cursor 
 If foc<>oldfoc Then oldfoc=foc : PlaySound sMenuSelect 
 DrawImage gCursor,MouseX(),MouseY()

 Flip
 ;screenshot (F12)
 If KeyHit(88) Then Screenshot()

Wend
;leave
If go=1 Then PlaySound sMenuGo Else PlaySound sMenuBack
FreeTimer timer
screen=20
End Function

;/////////////////////////////////////////////////////////////////
;---------------------- RELATED FUNCTIONS ------------------------
;/////////////////////////////////////////////////////////////////

;GET CHARACTER RANKINGS
Function GetRankings(category)
 ;reset records
 For char=1 To no_chars
  charRanked(char)=0
  fedRank(fed,char)=0
 Next
 ;honour roster slots
 If category=0
  If fed=0
   slotter=0
   For promotion=1 To 9
    For count=1 To fedSize(promotion) 
     char=fedRoster(promotion,count)
     If screenAgenda<>1 Or fedLocked(promotion)=0
      slotter=slotter+1
      fedRank(fed,slotter)=char : charRanked(char)=slotter
     EndIf
    Next
   Next
  Else
   For count=1 To fedSize(fed)
    char=fedRoster(fed,count)
    fedRank(fed,count)=char : charRanked(char)=count
   Next
  EndIf
 EndIf
 ;rank according to character stats
 If category=>1 And category=<23
  range=no_chars
  If fed>0 Then range=fedSize(fed)
  If screen=28 Then range=10
  For finder=1 To range
   leader=0
   If category=13 Or category=15 Or category=17 Then hi=9999 Else hi=-1
   For char=1 To no_chars
    excluded=0
    If fed>0 And charFed(char)<>fed Then excluded=1
    If screenAgenda=1 And fedLocked(charFed(char)) Then excluded=1
    If screen=28 And charFed(char)=>8 Then excluded=1
    If excluded=0 And charRanked(char)=0
     If category=1 And charPopularity(char)>hi Then hi=charPopularity(char) : leader=char
     If category=2 And charStrength(char)>hi Then hi=charStrength(char) : leader=char
     If category=3 And charSkill(char)>hi Then hi=charSkill(char) : leader=char
     If category=4 And charAgility(char)>hi Then hi=charAgility(char) : leader=char
     If category=5 And charStamina(char)>hi Then hi=charStamina(char) : leader=char
     If category=6 And charToughness(char)>hi Then hi=charToughness(char) : leader=char
     If category=7 And charAttitude(char)>hi Then hi=charAttitude(char) : leader=char
     If category=8 And charHappiness(char)>hi Then hi=charHappiness(char) : leader=char
     If category=9 And AverageStats(char)>hi Then hi=AverageStats(char) : leader=char 
     If category=10
      value=CountMatches(char,0)
      If value>hi Then hi=value : leader=char
     EndIf
     If category=11
      value=GetWinRate(char,0)
      If value>hi Then hi=value : leader=char
     EndIf
     If category=12 And charHealth(char)>hi Then hi=charHealth(char) : leader=char
     If category=13 And charHeight(char)<hi Then hi=charHeight(char) : leader=char
     If category=14 And charHeight(char)>hi Then hi=charHeight(char) : leader=char
     If category=15 And TranslateWeight(char)<hi Then hi=TranslateWeight(char) : leader=char
     If category=16 And TranslateWeight(char)>hi Then hi=TranslateWeight(char) : leader=char
     If category=17 And charAge(char)<hi Then hi=charAge(char) : leader=char
     If category=18 And charAge(char)>hi Then hi=charAge(char) : leader=char
     If category=19 And charContract(char)>hi Then hi=charContract(char) : leader=char
     If category=20 And charSalary(char)>hi Then hi=charSalary(char) : leader=char
     If category=21
      value=CountTitles(char,0)
      If value>hi Then hi=value : leader=char
     EndIf
     If category=22
      value=CountRelationships(char,1)
      If value>hi Then hi=value : leader=char
     EndIf
     If category=23
      value=CountRelationships(char,-1)
      If value>hi Then hi=value : leader=char
     EndIf
    EndIf
   Next
   fedRank(fed,finder)=leader : charRanked(leader)=finder
  Next
 EndIf
 ;rank according to promotion experience
 If category=>24 And category=<33 ;new belts orig = 30
  For finder=1 To 10
   leader=0 : hi=-1
   For promotion=1 To 7
    If charRanked(promotion)=0
     If category=24 And charMatches(gamChar,promotion)>hi Then hi=charMatches(gamChar,promotion) : leader=promotion
     If category=25 And GetWinRate(gamChar,promotion)>hi Then hi=GetWinRate(gamChar,promotion) : leader=promotion
     If category=26 And charTitles(gamChar,promotion,1)>hi Then hi=charTitles(gamChar,promotion,1) : leader=promotion
     If category=27 And charTitles(gamChar,promotion,2)>hi Then hi=charTitles(gamChar,promotion,2) : leader=promotion
     If category=28 And charTitles(gamChar,promotion,3)>hi Then hi=charTitles(gamChar,promotion,3) : leader=promotion
     If category=29 And charTitles(gamChar,promotion,4)>hi Then hi=charTitles(gamChar,promotion,4) : leader=promotion 
     If category=33 And CountTitles(gamChar,promotion)>hi Then hi=CountTitles(gamChar,promotion) : leader=promotion
;new belts
If category=30 And charTitles(gamChar,promotion,5)>hi Then hi=charTitles(gamChar,promotion,5) : leader=promotion
If category=31 And charTitles(gamChar,promotion,6)>hi Then hi=charTitles(gamChar,promotion,6) : leader=promotion
If category=32 And charTitles(gamChar,promotion,7)>hi Then hi=charTitles(gamChar,promotion,7) : leader=promotion
;end
    EndIf
   Next
   fedRank(fed,finder)=leader : charRanked(leader)=finder
  Next
 EndIf
End Function

;GET CAREER RANKINGS
Function CareerRankings(category)
 ;reset checkers
 For count=1 To 10
  hiRank(count)=0
 Next
 For count=1 To 10
  charRanked(count)=0
 Next
 ;find top 10
 For finder=1 To 10
  hi=-1 : leader=0
  For cyc=1 To 10
   If hiChar(cyc)>0 And charRanked(cyc)=0
    If category=1 And hiWealth(cyc)>hi Then hi=hiWealth(cyc) : leader=cyc
    If category=2 And hiTitles(cyc)>hi Then hi=hiTitles(cyc) : leader=cyc 
    If category=3 And hiSuccess(cyc)>hi Then hi=hiSuccess(cyc) : leader=cyc
    If category=4 And hiTalent(cyc)>hi Then hi=hiTalent(cyc) : leader=cyc
    If category=5 And hiExperience(cyc)>hi Then hi=hiExperience(cyc) : leader=cyc
   EndIf
  Next
  hiRank(finder)=leader : charRanked(leader)=finder
 Next
End Function

;RANK PROMOTIONS
Function RankPromotions()
 ;reset and clock previous
 For promotion=1 To 6
  fedOldList(promotion)=fedList(promotion)
  fedList(promotion)=0
  fedOldRanked(promotion)=fedRanked(promotion)
  fedRanked(promotion)=0
 Next
 ;update list
 For finder=1 To 6
  hi=0 : leader=0
  For promotion=1 To 6
   If fedPopularity(promotion)>hi And fedRanked(promotion)=0 Then leader=promotion : hi=fedPopularity(promotion)
  Next
  fedList(finder)=leader : fedRanked(leader)=finder
 Next
End Function

;CLOCK NEW CHAMPIONS
Function WriteHistory(promotion,title) ;0=booker, 1=world, 2=inter, 3=tags, 4=trophy
 If game=1 And slot>0
  ;find changes
  change=0
  If title=0 And fedBooker(promotion)<>fedHistChar(promotion,title,1) Then change=1
  If title=1 And fedChampWorld(promotion)<>fedHistChar(promotion,title,1) Then change=1
  If title=2 And fedChampInter(promotion)<>fedHistChar(promotion,title,1) Then change=1
  If title=3
   If fedChampTag(promotion,1)<>fedHistChar(promotion,title,1) And fedChampTag(promotion,2)<>fedHistChar(promotion,title,1) Then change=1
   If fedChampTag(promotion,1)<>fedHistPartner(promotion,title,1) And fedChampTag(promotion,2)<>fedHistPartner(promotion,title,1) Then change=1
  EndIf
  If title=4 And gamDate<>fedHistDate(promotion,title,1) Then change=1
;new belts
If title=5 And fedChampWomens(promotion)<>fedHistChar(promotion,title,1) Then change=1
If title=6 And fedChampUS(promotion)<>fedHistChar(promotion,title,1) Then change=1
If title=7 And fedChampTV(promotion)<>fedHistChar(promotion,title,1) Then change=1

;end
 
  If change=1
   ;get working character
   If title=0 Then char=fedBooker(promotion)
   If title=1 Then char=fedChampWorld(promotion)
   If title=2 Then char=fedChampInter(promotion)
   If title=3 Then char=fedChampTag(promotion,1)
   If title=4 Then char=fedCupHolder(promotion)
;new belts
If title=5 Then char=fedChampWomens(promotion)
If title=6 Then char=fedChampUS(promotion)
If title=7 Then char=fedChampTV(promotion)

;end
   If char>0
    ;move down priors
    For count=1 To 9
     source=10-count : target=11-count 
     fedHistCount(promotion,title,target)=fedHistCount(promotion,title,source)
     fedHistChar(promotion,title,target)=fedHistChar(promotion,title,source)
     fedHistPartner(promotion,title,target)=fedHistPartner(promotion,title,source)
     fedHistDate(promotion,title,target)=fedHistDate(promotion,title,source)
     fedHistYear(promotion,title,target)=fedHistYear(promotion,title,source)
    Next
    ;new entry 
    If fedHistChar(promotion,title,2)>0 Then fedHistCount(promotion,title,1)=fedHistCount(promotion,title,2)+1 Else fedHistCount(promotion,title,1)=1
    fedHistChar(promotion,title,1)=char
    fedHistPartner(promotion,title,1)=0
    If title=3 Then fedHistPartner(promotion,title,1)=fedChampTag(promotion,2)
    If game=0 Or slot=0 Then GetRealDate()
    fedHistDate(promotion,title,1)=gamDate
    fedHistYear(promotion,title,1)=gamYear
   EndIf
  EndIf
 EndIf
End Function