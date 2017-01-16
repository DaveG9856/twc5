;//////////////////////////////////////////////////////////////////////////////
;------------------------ WRESTLING MPIRE 2008: NEGOTIATIONS ------------------
;//////////////////////////////////////////////////////////////////////////////

;----------------------------------------------------------------
;//////////////////// RISK CONTRACT OFFERS //////////////////////
;----------------------------------------------------------------
Function RiskContractOffers()
 ;renewal
 fed=0 
 If charContract(gamChar)=0 And charFed(gamChar)=<6 And fedSize(7)<optRosterLim Then fed=charFed(gamChar)
 ;new offer
 If fed=0
  chance=(100-GetValue(gamChar))/3
  If charContract(gamChar)>0 And chance<charContract(gamChar)*10 Then chance=charContract(gamChar)*10
  If LastResult()<3 Then chance=chance*2
  If charFed(gamChar)=7 And gamResult(gamDate-1)=3 And charFed(gamOpponent(gamDate-1))=<6 Then chance=chance/2
  If InjuryStatus(gamChar)>0 Then chance=chance*2
  If chance<5 Then chance=5
  randy=Rnd(0,chance)
  If randy=<1 
   its=0
   Repeat
    fed=Rnd(1,6) : satisfied=1
    randy=Rnd(0,1)
    If randy=0 And charPopularity(gamChar)<fedPopularity(fed)-10 Then satisfied=0
    randy=Rnd(0,1)
    If randy=0 And charAttitude(gamChar)<fedReputation(fed)-10 Then satisfied=0
    randy=Rnd(0,1)
    If randy=0 And CountExperience(gamChar,fed)>0 Then satisfied=0
    randy=Rnd(0,1)
    If (randy=0 And fedSize(fed)>32) Or fedSize(fed)=>optRosterLim Then satisfied=0
    randy=Rnd(0,1)
    If randy=0 And charRelationship(fedBooker(fed),gamChar)<0 Then satisfied=0 
    randy=Rnd(0,1)
    If randy=0 And charContract(gamChar)>0 And fedBank(fed)/2<charSalary(gamChar)*charContract(gamChar) Then satisfied=0 
    If fed=charFed(gamChar) Then satisfied=0 
    its=its+1
    If its>100 Then fed=0 : satisfied=1
   Until satisfied=1
  EndIf
  ;promised offer
  If charFed(gamChar)=7 And gamResult(gamDate-1)=3 And charFed(gamOpponent(gamDate-1))=<6 Then fed=charFed(gamOpponent(gamDate-1))
  If gamAgreement(21)>0 And gamAgreement(21)<>charFed(gamChar) And LastResult()=3 Then fed=gamAgreement(21)
 EndIf
 ;execute
 If fed>0 And fedBooker(fed)>0 Then negChar=fedBooker(fed) : screen=52 : Negotiations()
 gamAgreement(21)=0
End Function

;----------------------------------------------------------------
;///////////////////// RISK MATCH OFFERS ////////////////////////
;----------------------------------------------------------------
Function RiskMatchOffers()
 negTopic=2 : negChar=0 : fed=charFed(gamChar)
 ;find prospects
 date=NextDate() 
 If date>gamDate+4 Then date=0
 If date>0 And charClause(gamChar,1)>0 And InjuryDate(date)=0 And CountMatches(gamChar,fed)>0
  stopper=Rnd(0,1)
  For count=1 To fedSize(fed)
   char=fedRoster(fed,count)
   If negChar=0 Or stopper=0
    If char<>gamChar And char<>charPartner(gamChar) And char<>charManager(gamChar) And charRole(char)=1 And InjuryStatus(char)=0 And TournamentStatus(char)=0 And OpponentBooked(char)=0
     chance=(GetValue(char)-GetValue(gamChar))*20
     If chance<40 Then chance=40
     If charHeel(char)=charHeel(gamChar) Then chance=chance*2
     If TitleHolder(gamChar,0) And TitleHolder(char,0)=0 Then chance=chance/2 
     If TitleHolder(char,0) And TitleHolder(gamChar,0)=0 Then chance=chance*2
     randy=Rnd(0,chance)
     If TitleHolder(char,3) And charPartner(gamChar)=0 Then randy=99
     If TitleHolder(gamChar,3) And TitleHolder(char,0)>0 Then randy=99
     If randy=<1 Then negChar=char
    EndIf
   EndIf
  Next
 EndIf
 ;execute
 If negTopic>0 And negChar>0 Then screen=56 : ArrangeMatch() 
End Function

;------------------------------------------------------------------------------
;////////////////////// 52. CONTRACT NEGOTIATIONS /////////////////////////////
;------------------------------------------------------------------------------
Function Negotiations()
;get setting
Loader("Please Wait","Meeting "+charName$(negChar))
;ChannelVolume chTheme,0.5
fed=charFed(negChar)
negSetting=1 : camFoc=2
PrepareMeeting()
;initial contract
negChances=Rnd(1,charAttitude(negChar)/9)
negContract=Rnd(12,36)
negInitialContract=negContract
For count=1 To 3
 negClause(count)=Rnd(0,ClauseEntitled(char,fed,count))
 randy=Rnd(0,2)
 If randy=0 And negClause(count)<2 Then negClause(count)=negClause(count)+1
 negInitialClause(count)=negClause(count)
Next
negWorth=CalculateWorth(gamChar,fed)
negWorth=PercentOf#(negWorth,Rnd(75,125))
negSalary=ContractFilter(negWorth,negContract,fed)
negSalary=ClauseFilter(negWorth,negClause(1),negClause(2),negClause(3),fed)
negSalary=Int(PercentOf#(negSalary,charAttitude(negChar)))
negPayOff=0 : randy=Rnd(-15,5)
If randy>0 Then cut=PercentOf#(negSalary,randy*20) Else cut=0
If cut>0 And negSalary>100 And fedBank(fed)/2>cut*negContract
 negPayOff=cut*negContract
 negSalary=negSalary-cut
EndIf
negPayOff=RoundOff(negPayOff,100)
If negPayOff>10000 Then negPayOff=RoundOff(negPayOff,1000)
negInitialPayOff=negPayOff
negSalary=RoundOff(negSalary,10)
If negSalary>1000 Then negSalary=RoundOff(negSalary,100)
negInitialSalary=negSalary
;reset progress
negTim=0 : negStage=0
;frame rating
timer=CreateTimer(30)
;MAIN LOOP
foc=1 : oldfoc=foc : charged=0
go=0 : gotim=-20 : keytim=20
While go=0

 Cls
 frames=WaitTimer(timer)
 For framer=1 To frames
	
	;timers
	keytim=keytim-1
	If keytim<1 Then keytim=0  
	
	;PORTAL
    gotim=gotim+1
    If gotim>25 Then negTim=negTim+1 
    ;speed-ups
    If gotim>0 And negStage<>1 And negStage<>3 And keytim=0
	 If KeyDown(1) Or KeyDown(28) Or ButtonPressed() Or MouseDown(1) Then negTim=negTim+50 : keytim=5 ;: PlaySound sMenuBrowse
	EndIf
	
	;CONFIGURATION
	If gotim>20 And keytim=0 And (negStage=1 Or negStage=3)
	 ;highlight option
	 If KeyDown(200) Or JoyYDir()=-1 Then foc=foc-1 : keytim=5
	 If KeyDown(208) Or JoyYDir()=1 Then foc=foc+1 : keytim=5
	 If foc>7 Then foc=0
	 If foc<0 Then foc=7 
	 ;adjust contract
	 If negStage=1 And foc=>1 And foc=<6
	  ;browse left
	  If KeyDown(203) Or JoyXDir()=-1 Or MouseDown(2)
	   range=FigureRange(negPayOff)
	   If range<100 Then range=100
	   If foc=1 Then negPayOff=negPayOff-range : PlaySound sMenuBrowse : keytim=3
	   range=FigureRange(negSalary)
	   If foc=2 Then negSalary=negSalary-range : PlaySound sMenuBrowse : keytim=3
	   If foc=3 Then negContract=negContract-1 : PlaySound sMenuBrowse : keytim=3
	   For count=1 To 3
	    If foc=3+count Then negClause(count)=negClause(count)-1 : PlaySound sMenuBrowse : keytim=5
	   Next
	  EndIf
	  ;browse right
	  If KeyDown(205) Or JoyXDir()=1 Or MouseDown(1)
	   range=FigureRange(negPayOff)
	   If range<100 Then range=100
	   If foc=1 Then negPayOff=negPayOff+range : PlaySound sMenuBrowse : keytim=3
	   range=FigureRange(negSalary)
	   If foc=2 Then negSalary=negSalary+range : PlaySound sMenuBrowse : keytim=3
	   If foc=3 Then negContract=negContract+1 : PlaySound sMenuBrowse : keytim=3
	   For count=1 To 3
	    If foc=3+count Then negClause(count)=negClause(count)+1 : PlaySound sMenuBrowse : keytim=5
	   Next
	  EndIf
	  ;reset
	  If KeyDown(14)
	   PlaySound sMenuBrowse : keytim=10
	   If foc=1 Then negPayOff=negInitialPayOff
	   If foc=2 Then negSalary=negInitialSalary
	   If foc=3 Then negContract=negInitialContract
	   For count=1 To 3
	    If foc=3+count Then negClause(count)=negInitialClause(count)
	   Next
	  EndIf
	 EndIf
	 ;submit proposal
	 If foc=7 Or KeyDown(28)
	  If KeyDown(28) Or ButtonPressed() Or MouseDown(1) 
	   PlaySound sMenuGo : keytim=15
	   If negStage=1 
	    negStage=2 : negTim=0 : foc=7
	    If DealChanged() Then negVerdict=GetContractVerdict() Else negVerdict=0
	   EndIf
	   If negStage=3 Or DealChanged()=0 Then negStage=4 : negTim=0
	  EndIf
	 EndIf
	 ;cancel
	 If foc=0 Or KeyDown(1)
	  If KeyDown(1) Or KeyDown(28) Or ButtonPressed() Or MouseDown(1)
	   PlaySound sMenuBack : keytim=15
	   negStage=5 : negTim=0
	  EndIf
	 EndIf
	EndIf
	;check limits
	If negPayOff<0 Then negPayOff=0
	If negPayOff>1000000 Then negPayOff=1000000  
	If negSalary<0 Then negSalary=0
	If negSalary>50000 Then negSalary=50000  
	If negContract<0 Then negContract=0
	If negContract>48 Then negContract=48
	For count=1 To 3
	 If negClause(count)<0 Then negClause(count)=0
	 If negClause(count)>2 Then negClause(count)=2 
	Next  
	
	;PLAYER CYCLE
	For cyc=1 To no_plays
	 If pChar(cyc)>0
	  ;facial expressions
	  RandomizeAnimation(cyc)
	  FacialExpressions(cyc)
	  ManageEyes(cyc)
	  ;shadows
	  For limb=1 To 50
       If pShadow(cyc,limb)>0
        RotateEntity pShadow(cyc,limb),90,EntityYaw(pLimb(cyc,limb),1),0
        PositionEntity pShadow(cyc,limb),EntityX(pLimb(cyc,limb),1),pY#(cyc)+0.4,EntityZ(pLimb(cyc,limb),1)
       EndIf
      Next
     EndIf   
	Next
	
	;CAMERA
	Camera()
	;music
	ManageMusic(-1)
	
 UpdateWorld
 Next  

 ;ANIMATION OVERRIDE
 pFoc(1)=2 :  pFoc(2)=1
 If pChar(3)>0 
  If pSpeaking(3)>0 Or (negStage=0 And negTim=>350 And fed<>charFed(gamChar) And charContract(gamChar)>0) Then pFoc(1)=3 : pFoc(2)=3
  If negStage=1 Or negStage=3 Then pFoc(3)=1 Else pFoc(3)=2
 EndIf
 For cyc=1 To 3
 If pChar(cyc)>0
  If pFoc(cyc)>0
   If cyc=3 Or pFoc(cyc)=3 Then PointHead(cyc,pLimb(pFoc(cyc),1))
   LookAtPerson(cyc,pFoc(cyc))
  Else
   RotateEntity pLimb(cyc,45),0,0,0
   RotateEntity pLimb(cyc,46),0,0,0
  EndIf
  If charEyeShape(pChar(cyc))=112 Then LookAtPerson(cyc,cyc)
 EndIf
 Next
 
 RenderWorld 1

 ;DISPLAY
 DrawImage gLogo(2),rX#(400),rY#(65)
 ;reset speech
 For cyc=1 To no_plays
  pSpeaking(cyc)=0
 Next
 ;reset subtitles
 lineA$="" : lineB$=""
 redLineA$="" : redLineB$=""
 greenLineA$="" : greenLineB$=""
 ;------------- INTRODUCTIONS -----------------
 ;approached by new promotion (without intervention)
 If negStage=0 And fed<>charFed(gamChar) And charContract(gamChar)=0
  If negTim>25 And negTim<325 And charExperience(gamChar,fed)=0 And gamAgreement(21)<>fed
   Speak(2,0,3)
   lineA$="Nice to meet you, "+charName$(gamChar)+". We invited you here"
   lineB$="today to discuss working for "+fedName$(fed)+"..."
  EndIf
  If negTim>25 And negTim<325 And charExperience(gamChar,fed)>0 And gamAgreement(21)<>fed
   Speak(2,0,3)
   lineA$="Hi, "+charName$(gamChar)+". We got back in touch to talk"
   lineB$="about bringing you back to "+fedName$(fed)+"!"
  EndIf
  If negTim>25 And negTim<325 And gamAgreement(21)=fed
   Speak(2,0,3)
   lineA$="Congratulations on your performance, "+charName$(gamChar)+"!"
   lineB$="As promised, we'd like to talk about employing you..."
  EndIf
  If negTim>350 And negTim<650 And negWorth=<charWorth(gamChar)
   Speak(2,0,1)
   lineA$="You're not exactly a priority for us right now, but"
   lineB$="we'd consider bringing you onboard with this deal:"
  EndIf
  If negTim>350 And negTim<650 And negWorth>charWorth(gamChar)
   Speak(2,0,3)
   lineA$="We feel you'd make a great addition to the roster,"
   lineB$="and we're prepared to make it happen with this deal:"
  EndIf
  If negTim>650 Then camFoc=1
  If negTim>675 Then negStage=1 : negTim=0 : keytim=20
 EndIf
 ;approached by new promotion (with intervention)
 If negStage=0 And fed<>charFed(gamChar) And charContract(gamChar)>0
  If negTim>25 And negTim<325 And charExperience(gamChar,fed)=0
   Speak(2,0,3)
   lineA$="Nice to meet you, "+charName$(gamChar)+". We invited you here"
   lineB$="today to discuss working for "+fedName$(fed)+"..."
  EndIf
  If negTim>25 And negTim<325 And charExperience(gamChar,fed)>0
   Speak(2,0,3)
   lineA$="Hi, "+charName$(gamChar)+". We got back in touch to talk"
   lineB$="about bringing you back to "+fedName$(fed)+"!"
  EndIf 
  If negTim>350 And negTim<650
   Speak(3,0,1)
   lineA$=charName$(gamChar)+" is tied to "+fedName$(charFed(gamChar))
   lineB$="with a "+charContract(gamChar)+" week contract worth $"+GetFigure$(charSalary(gamChar))+" per week!"
  EndIf 
  If negTim>675 And negTim<975
   Speak(3,0,1)
   lineA$="It'll cost at least $"+GetFigure$(charSalary(gamChar)*charContract(gamChar))+" to buy "+Him$(charGender(gamChar))
   lineB$="out of that contract, "+charName$(negChar)+"..."
  EndIf
  If negTim>975 Then camFoc=2
  If negTim>1000 And negTim<1300
   Speak(2,0,2)
   lineA$="OK, "+charName$(pChar(3))+" - I understand the situation!"
   lineB$="With that in mind, we're prepared to offer this deal:"
  EndIf
  If negTim>1300 Then camFoc=1
  If negTim>1325 Then negStage=1 : negTim=0 : keytim=20
 EndIf
 ;renew with existing promotion
 If negStage=0 And fed=charFed(gamChar)
  If negTim>25 And negTim<325
   Speak(2,0,3)
   lineA$="Hi, "+charName$(gamChar)+". Your contract with us has"
   lineB$="expired, so we need to talk about your future..."
  EndIf
  If negTim>350 And negTim<650 And negWorth<charWorth(gamChar)
   Speak(2,0,1)
   lineA$="To be honest, things aren't working out too well"
   lineB$="and we'd like to reflect that with these changes:"
  EndIf
  If negTim>350 And negTim<650 And negWorth=>charWorth(gamChar)
   Speak(2,0,3)
   lineA$="I must admit that we're pleased with your progress,"
   lineB$="so we'd be happy to keep you on with this deal:"
  EndIf
  If negTim>650 Then camFoc=1
  If negTim>675 Then negStage=1 : negTim=0 : keytim=20
 EndIf
 ;--------------- CONTRACT SETUP --------------------
 If negStage=1 Or negStage=3
  ;facial expression
  mood=2
  If foc=0 Then mood=1
  If foc=7 Then mood=3
  Speak(1,0,mood)
  ;options
  y=475
  Color 0,0,0 : Rect rX#(0),rY#(y)-30,rX#(800),rY#(300),1
  DrawOption(0,rX#(104),rY#(515),"<<< WITHDRAW <<<","")
  DrawOption(1,rX#(400),rY#(y-1),"Pay-Off","$"+GetFigure$(negPayOff))
  DrawOption(2,rX#(400),rY#(y+31),"Salary","$"+GetFigure$(negSalary)+" per week")
  DrawOption(3,rX#(400),rY#(y+63),"Contract",negContract+" weeks") 
  namer$=">>> PROPOSE >>>"
  If DealChanged()=0 Then namer$=">>> ACCEPT >>>"
  If negStage=3 Then namer$=">>> CONFIRM >>>"
  DrawOption(7,rX#(696),rY#(515),namer$,"")
  ;smallprint hotspots
  y=y+85
  If MouseX()=>rX#(400)-90 And MouseX()=<rX#(400)+110
   For count=1 To 3
    If MouseY()=>rY#(y+((count-1)*12))-3 And MouseY()=<rY#(y+((count-1)*12))+6 Then foc=count+3
   Next
  EndIf
  ;smallprint options
  SetFont font(1)
  r=150 : g=80 : b=80
  If foc=4 Then DrawImage gSmallPrint,rX#(400)-111,rY#(y)+5 : r=230 : g=0 : b=0
  OutlineStraight("Creative Control:",rX#(400)-101,rY#(y+2),0,0,0,r,g,b)
  r=150 : g=80 : b=80
  If foc=5 Then DrawImage gSmallPrint,rX#(400)-133,rY#(y+14)+5 : r=230 : g=0 : b=0
  OutlineStraight("Performance Clause:",rX#(400)-123,rY#(y+14),0,0,0,r,g,b)
  r=150 : g=80 : b=80
  If foc=6 Then DrawImage gSmallPrint,rX#(400)-96,rY#(y+26)+5 : r=230 : g=0 : b=0
  OutlineStraight("Health Policy:",rX#(400)-85,rY#(y+26),0,0,0,r,g,b)
  SetFont fontStat(0)
  For count=1 To 3
   GetStatColour(negClause(count),1)
   r=ColorRed() : g=ColorGreen() : b=ColorBlue()
   If foc<>3+count Then r=r-(r/4) : g=g-(g/4) : b=b-(b/4)
   OutlineStraight(textClause$(count,negClause(count)),rX#(400)+4,rY#(y+((count-1)*12)),0,0,0,r,g,b)
  Next
  ;stat reminder
  DrawProfile(gamChar,-1,-1,0)
 Else
  DrawFedProfile(fed,-1,-1)
 EndIf
 ;------------------ VERDICTS -----------------------------
 If negStage=2 And negTim>10 Then camFoc=2
 ;acceptances
 If negStage=2 And negVerdict=0
  If negTim>25 And negTim<325
   Speak(2,0,3)
   greenLineA$="OK, I'm happy to draw up that contract for you!"
   greenLineB$="Just confirm the details and it's a done deal..."
  EndIf
  If negTim>350 Then negStage=3 : negTim=0 : keytim=15
 EndIf
 ;contract objections
 If negStage=2 And negVerdict=1
  If negTim>25 And negTim<325
   Speak(2,0,3) 
   redLineA$="Sorry, but we can't afford to pay out $"+GetFigure$(negPayOff)+"."
   redLineB$="You'll have to negotiate that as a weekly salary..."
  EndIf
  If negTim>350 Then PushLuck(1) : negTim=10 : keytim=15
 EndIf
 If negStage=2 And negVerdict=2
  If negTim>25 And negTim<325
   Speak(2,0,1) 
   redLineA$="What's the point in signing up for just "+negContract+" weeks?!"
   redLineB$="We can't invest in someone that's not committed..."
  EndIf
  If negTim>350 Then PushLuck(1) : negTim=10 : keytim=15
 EndIf
 If negStage=2 And negVerdict=3
  If negTim>25 And negTim<325
   Speak(2,0,1) 
   redLineA$="You're not getting $"+GetFigure$(negPayOff)+" now AND $"+GetFigure$(negSalary)+" per week!"
   redLineB$="At least one of those figures will have to come down..."
  EndIf
  If negTim>350 Then PushLuck(1) : negTim=10 : keytim=15
 EndIf
 If negStage=2 And negVerdict=4
  If negTim>25 And negTim<325
   Speak(2,0,1) 
   redLineA$="No, you're not worth $"+GetFigure$(negPayOff)+" to "+fedName$(fed)+"!"
   redLineB$="The figure we offered you was perfectly reasonable..."
  EndIf
  If negTim>350 Then PushLuck(1) : negTim=10 : keytim=15
 EndIf
 If negStage=2 And negVerdict=5
  If negTim>25 And negTim<325
   Speak(2,0,1) 
   redLineA$="No, I don't see you earning $"+GetFigure$(negSalary)+" per week!"
   redLineB$="The figure we offered you was perfectly reasonable..."
  EndIf
  If negTim>350 Then PushLuck(1) : negTim=10 : keytim=15
 EndIf
 If negStage=2 And negVerdict=6
  If negTim>25 And negTim<325
   Speak(2,0,1) 
   redLineA$="No, even a "+negContract+"-week contract isn't worth that much!"
   redLineB$="You'll have to come up with a more realistic figure..."
  EndIf
  If negTim>350 Then PushLuck(1) : negTim=10 : keytim=15
 EndIf
 If negStage=2 And negVerdict=7
  If negTim>25 And negTim<325
   Speak(2,0,1) 
   redLineA$="No, we can't invest that much for just "+negContract+" weeks!"
   redLineB$="That kind of money requires a bigger commitment..."
  EndIf
  If negTim>350 Then PushLuck(1) : negTim=10 : keytim=15
 EndIf
 If negStage=2 And negVerdict=8
  If negTim>25 And negTim<325
   Speak(2,0,1) 
   redLineA$="I'm insulted that you would even dare demand that!"
   redLineB$="You're obviously not taking this very seriously..."
  EndIf
  If negTim>350 Then PushLuck(1) : negTim=10 : keytim=15
 EndIf
 ;clause objections
 If negStage=2 And negVerdict=11
  If negTim>25 And negTim<325 And negClause(1)=1
   Speak(2,0,1) 
   redLineA$="Sorry, but we can't allow you to interfere with the"
   redLineB$="creative process. Just concentrate on wrestling..."
  EndIf
  If negTim>25 And negTim<325 And negClause(1)=2
   Speak(2,0,1) 
   redLineA$="Sorry, but you're not ready to take full control of"
   redLineB$="your career yet. That's a major responsibility..."
  EndIf
  If negTim>350 Then PushLuck(1) : negTim=10 : keytim=15
 EndIf 
 If negStage=2 And negVerdict=12
  If negTim>25 And negTim<325 And negClause(1)=1
   Speak(2,0,1) 
   redLineA$="No, we can't trust YOU with any creative control!"
   redLineB$="Your attitude doesn't exactly inspire confidence..."
  EndIf
  If negTim>25 And negTim<325 And negClause(1)=2
   Speak(2,0,1) 
   redLineA$="No, we can't trust YOU with full creative control!"
   redLineB$="God knows what a "+Guy$(charGender(gamChar))+" like you would do with it..."
  EndIf
  If negTim>350 Then PushLuck(1) : negTim=10 : keytim=15
 EndIf
 If negStage=2 And negVerdict=13
  If negTim>25 And negTim<325 And negClause(2)=1
   Speak(2,0,1) 
   redLineA$="No, we think it's important that you have an incentive"
   redLineB$="to perform until you've proven yourself in the ring..."
  EndIf
  If negTim>25 And negTim<325 And negClause(2)=2
   Speak(2,0,1) 
   redLineA$="No, you're not good enough to be paid no matter what!"
   redLineB$="Very few people are THAT important to the company..."
  EndIf
  If negTim>350 Then PushLuck(1) : negTim=10 : keytim=15
 EndIf
 If negStage=2 And negVerdict=14
  If negTim>25 And negTim<325 And negClause(2)=1
   Speak(2,0,1) 
   redLineA$="No, we can't contemplate paying you to LOSE!"
   redLineB$="Your attitude doesn't exactly inspire confidence..."
  EndIf
  If negTim>25 And negTim<325 And negClause(2)=2
   Speak(2,0,1) 
   redLineA$="No, we can't trust you with unconditional pay!"
   redLineB$="Your attitude doesn't exactly inspire confidence..."
  EndIf
  If negTim>350 Then PushLuck(1) : negTim=10 : keytim=15
 EndIf
 If negStage=2 And negVerdict=15
  If negTim>25 And negTim<325 And negClause(3)=1
   Speak(2,0,1) 
   redLineA$="Sorry, but you're not an ideal candidate for health"
   redLineB$="cover. You need an incentive to look after yourself!"
  EndIf
  If negTim>25 And negTim<325 And negClause(3)=2
   Speak(2,0,1) 
   redLineA$="No, we can't pay for ALL of your health care!
   redLineB$="Very few people are THAT important to the company..."
  EndIf
  If negTim>350 Then PushLuck(1) : negTim=10 : keytim=15
 EndIf  
 If negStage=2 And negVerdict=16
  If negTim>25 And negTim<325 And negClause(3)=1
   Speak(2,0,1) 
   redLineA$="No, we can't trust you with any health cover!"
   redLineB$="Your attitude doesn't exactly inspire confidence..."
  EndIf
  If negTim>25 And negTim<325 And negClause(3)=2
   Speak(2,0,1) 
   redLineA$="No, we can't trust you with full health cover!"
   redLineB$="Your attitude doesn't exactly inspire confidence..."
  EndIf
  If negTim>350 Then PushLuck(1) : negTim=10 : keytim=15
 EndIf  
 If negStage=2 And negVerdict=17
  If negTim>25 And negTim<325
   Speak(2,0,1) 
   redLineA$="You want big money AND a cushy contract?! We"
   redLineB$="gave you those sweeteners to cut down the price..."
  EndIf
  If negTim>350 Then PushLuck(1) : negTim=10 : keytim=15
 EndIf 
 If negStage=2 And negVerdict=18
  If negTim>25 And negTim<325
   Speak(2,0,1) 
   redLineA$="You can't have a cushier contract for the same money!"
   redLineB$="Either your expectations come down or your price does..."
  EndIf
  If negTim>350 Then PushLuck(1) : negTim=10 : keytim=15
 EndIf 
 If negStage=2 And negVerdict=19
  If negTim>25 And negTim<325
   Speak(2,0,1) 
   redLineA$="Sorry, but even those sacrifices don't justify what"
   redLineB$="you're asking for. The figures simply don't add up..."
  EndIf
  If negTim>350 Then PushLuck(1) : negTim=10 : keytim=15
 EndIf  
 ;----------------- ENDINGS -----------------------
 If negStage=>4 And negTim>10 Then camFoc=2
 ;successful deal
 If negStage=4 And fed<>charFed(gamChar)
  If negTim>25 And negTim<325 And charExperience(gamChar,fed)=0
   Speak(2,0,3)
   If charged=0 Then fedBank(fed)=fedBank(fed)-negPayOff : PlaySound sCash : charged=1
   lineA$="Great, welcome to "+fedName$(fed)+"!"
   lineB$="I'm sure you'll be an asset to the show..."
  EndIf
  If negTim>25 And negTim<325 And charExperience(gamChar,fed)>0
   Speak(2,0,3)
   If charged=0 Then fedBank(fed)=fedBank(fed)-negPayOff : PlaySound sCash : charged=1
   lineA$="Great, welcome back to "+fedName$(fed)+"!"
   lineB$="We can't wait to have you back on the show..."
  EndIf
  If negTim>375 Then go=1
 EndIf
 ;successful renewl
 If negStage=4 And fed=charFed(gamChar)
  If negTim>25 And negTim<325
   Speak(2,0,3)
   If charged=0 Then fedBank(fed)=fedBank(fed)-negPayOff : PlaySound sCash : charged=1
   lineA$="Good, I'm glad we could come to an arrangement!"
   lineB$="Let's make the next "+negContract+" weeks your best yet..."
  EndIf
  If negTim>375 Then go=1
 EndIf
 ;deal breakdown
 If negStage=5 And fed<>charFed(gamChar)
  If negTim>25 And negTim<325
   Speak(2,0,1)
   redLineA$="Alright, this meeting isn't going anywhere."
   redLineB$="Let's just forget about it for the time being..."
  EndIf
  If negTim>375 Then go=1
 EndIf
 ;renewal breakdown
 If negStage=5 And fed=charFed(gamChar)
  If negTim>25 And negTim<325
   Speak(2,0,2)
   redLineA$="Well, we're obviously not on the same page anymore"
   redLineB$="so we wish you luck finding employment elsewhere..."
  EndIf
  If negTim>375 Then go=1
 EndIf
 ;---------- DISPLAY SUBTITLES ----------
 DisplaySubtitles()
 ;diagnostics
 ;negVerdict=GetContractVerdict()
 ;SetFont fontStat(1)
 ;Outline("negPaymentTotal: "+negPaymentTotal,100,300,0,0,0,255,255,255)
 ;Outline("negPaymentLimit: "+negPaymentLimit,100,315,0,0,0,255,255,255)
 ;Outline("ClauseEntitled(1): "+ClauseEntitled(gamChar,fed,1),100,335,0,0,0,255,255,255)
 ;Outline("ClauseEntitled(2): "+ClauseEntitled(gamChar,fed,2),100,350,0,0,0,255,255,255)
 ;Outline("ClauseEntitled(3): "+ClauseEntitled(gamChar,fed,3),100,365,0,0,0,255,255,255)
 ;Outline("Verdict: "+negVerdict,100,385,0,0,0,255,255,255)
 ;cursor
 If foc<>oldfoc Then oldfoc=foc : PlaySound sMenuSelect 
 DrawImage gCursor,MouseX(),MouseY()
 ;mask shaky start
 If gotim=<0 Then Loader("Please Wait","Meeting "+charName$(negChar))

 Flip
 ;screenshot (F12)
 If KeyHit(88) Then Screenshot()

Wend
;restore sound
;ChannelVolume chTheme,1.0	
FreeTimer timer
;free entities
FreeEntity world
FreeEntity cam 
FreeEntity camPivot
FreeEntity dummy
FreeEntity light(1)
FreeEntity lightPivot
;remove characters
For cyc=1 To no_plays
 If pChar(cyc)>0
  FreeEntity p(cyc)
  For limb=1 To 50
   If pShadow(cyc,limb)>0
    FreeEntity pShadow(cyc,limb)
   EndIf
  Next
 EndIf
Next
;update status
If negStage=4
 If fed<>charFed(gamChar)
  If charContract(gamChar)>0
   fedBank(fed)=fedBank(fed)-(charSalary(gamChar)*charContract(gamChar))
   fedBank(charFed(gamChar))=fedBank(charFed(gamChar))+(charSalary(gamChar)*charContract(gamChar))
  EndIf
  MoveChar(gamChar,fed)
  If fed=<6
   If gamSchedule(gamDate)=>1 And gamSchedule(gamDate)=<2 Then date=gamDate Else date=NextDate()
   For count=1 To fedSize(fed)
    v=fedRoster(fed,count)
    If v<>gamChar And charRelationship(gamChar,v)>0 Then gamMatch(date)=12 : gamPromo(date)=98 : gamPromoVariable(date)=v
   Next
   If charExperience(gamChar,fed)>0 Then gamPromo(date)=65
  EndIf
 EndIf
 charWorth(gamChar)=negWorth
 charBank(gamChar)=charBank(gamChar)+negPayOff
 charSalary(gamChar)=negSalary
 charContract(gamChar)=negContract
 For count=1 To 3
  charClause(gamChar,count)=negClause(count)
 Next
Else
 If fed=charFed(gamChar)
  MoveChar(gamChar,7)
  charPopularity(gamChar)=charPopularity(gamChar)-1
  charAttitude(gamChar)=charAttitude(gamChar)-1
  charHappiness(gamChar)=charHappiness(gamChar)-1
 EndIf
EndIf
;proceed
screen=20
End Function

;------------------------------------------------------------------------------
;///////////////////////// 56. MATCH ARRANGEMENT //////////////////////////////
;------------------------------------------------------------------------------
Function ArrangeMatch()
;get setting
Loader("Please Wait","Meeting "+charName$(negChar))
;ChannelVolume chTheme,0.5
negSetting=Rnd(2,13)
If charRole(negChar)>1 Then negSetting=Rnd(2,5)
If negChar=fedBooker(charFed(negChar)) Then negSetting=1 
If gamSchedule(gamDate)=<0 And negSetting>1 Then negSetting=Rnd(2,5)
If negTopic=1 Then camFoc=1 Else camFoc=2
PrepareMeeting()
;simulate CPU preferences
negInitialDate=-1
negInitialMatch=-1
negInitialGimmick=-1
negInitialVenue=-1 
For count=1 To 48
 randy=Rnd(0,4)
 If randy=0 Then negDatePrefer(count)=0 Else negDatePrefer(count)=1
Next
For count=0 To no_matches
 randy=Rnd(0,2)
 If randy=0 Then negMatchPrefer(count)=0 Else negMatchPrefer(count)=1
Next
negMatchPrefer(Rnd(1,no_matches))=1
For count=0 To no_gimmicks
 randy=Rnd(0,2)
 If count=0 Then randy=Rnd(0,4) 
 If randy=0 Then negGimmickPrefer(count)=0 Else negGimmickPrefer(count)=1
Next
For count=1 To no_arenas
 randy=Rnd(0,4)
 If randy=0 Then negVenuePrefer(count)=0 Else negVenuePrefer(count)=1
Next
negVenuePrefer(Rnd(1,10))=1
negVenuePrefer(Rnd(11,no_arenas))=1
;initial player arrangement
If negTopic=1
 negDate=0
 For count=gamDate To 48
  If negDate=0 And gamSchedule(count)=>1 And gamSchedule(count)=<2 And gamOpponent(count)=0 And InjuryDate(count)=0 Then negDate=count
 Next
 negMatch=2 : negGimmick=0
 If gamSchedule(negDate)=1 Then negVenue=Rnd(1,10) Else negVenue=Rnd(11,no_arenas)
 negInterest=Rnd(-1,1) : negVerdict=0
 If GetValue(gamChar)>GetValue(negChar)-(GetValue(negChar)/8) Then negInterest=negInterest+1
 If GetValue(gamChar)>GetValue(negChar) Then negInterest=negInterest+1
 If GetValue(gamChar)>GetValue(negChar)+(GetValue(negChar)/8) Then negInterest=negInterest+1 
 If AverageStats(gamChar)>AverageStats(negChar) Then negInterest=negInterest-1 : negVerdict=10 ;scared of losing
 If AverageStats(gamChar)>PercentOf#(AverageStats(negChar),125) Then negInterest=negInterest-1
 If charPopularity(gamChar)=<charPopularity(negChar)-10 And TitleHolder(gamChar,0)=0 Then negInterest=negInterest-1 : negVerdict=11 ;nothing to gain
 If (TitleHolder(gamChar,0)>0 And TitleHolder(negChar,0)=0) Or TitleHolder(gamChar,1) Then negInterest=negInterest+1
 If (TitleHolder(negChar,0)>0 And TitleHolder(gamChar,0)=0) Or TitleHolder(negChar,1) Then negInterest=negInterest-1 : negVerdict=12 ;not worthy of shot
 If charHeel(negChar)=charHeel(gamChar) Then negInterest=negInterest-1 : negVerdict=16 ;wrong allegiance 
 priorMatch=0 : futureMatch=0
 For count=1 To 4
  If gamDate-count>0
   If gamSchedule(gamDate-count)>0 And gamOpponent(gamDate-count)=negChar Then priorMatch=1
  EndIf
  If gamSchedule((gamDate+count)-1)>0 And gamOpponent((gamDate+count)-1)=negChar Then futureMatch=1
 Next
 If priorMatch=1 Then negInterest=negInterest-1 : negVerdict=5 ;fought recently
 If futureMatch=1 Then negInterest=negInterest-1 : negVerdict=6 ;fighting soon
 If charRelationship(negChar,gamChar)>0 Then negInterest=negInterest-1 : negVerdict=13 ;friend turned off
 If charRelationship(negChar,gamChar)<0 Then negInterest=negInterest+1
 If charPartner(gamChar)>0 And charPartner(negChar)=0 And TitleHolder(negChar,3)=0 Then negInterest=negInterest-1 : negVerdict=14 ;not a team wrestler
 If charPartner(gamChar)=0 And charPartner(negChar)>0 And TitleHolder(negChar,0)=0 Then negInterest=negInterest-1 : negVerdict=15 ;not a solo wrestler 
 If charRole(negChar)>1 Or negChar=fedBooker(fed) Then negInterest=negInterest-1 : negVerdict=4 ;not a wrestler
 If negInterest<0 Then negInterest=0
 If negInterest>3 Then negInterest=3
 negChances=Rnd(1,3) 
 negChances=negChances*negInterest 
 If negChances=0 And negVerdict=0 Then negVerdict=1 ;not interested
;new belts
 If charPartner(gamChar)>0 And (TitleHolder(negChar,1) Or TitleHolder(negChar,2) Or TitleHolder(negChar,5) Or TitleHolder(negChar,6) Or TitleHolder(negChar,7)) Then negVerdict=14 ;solo champs avoid teams
;end 
If charPartner(gamChar)=0 And TitleHolder(negChar,3) Then negVerdict=15 ;tag champs avoid solo 
 If CountMatches(gamChar,0)=0 Then negChances=0 : negVerdict=9 ;too green
 If InjuryStatus(negChar)>0 Then negChances=0 : negVerdict=2 ;injured
 If TournamentStatus(negChar)>0 Then negChances=0 : negVerdict=7 ;focusing on tournament 
 If charFed(negChar)=>8 Then negChances=0 : negVerdict=4 ;not a wrestler
 If negChar=charPartner(gamChar) Then negVerdict=13 ;can't fight partner!
 If negDate=0 Then negChances=0 : negVerdict=3 ;no dates
 If gamNegotiated(negChar,3)>0 Then negChances=0 : negVerdict=8 ;already tried
EndIf
;initial CPU arrangement
If negTopic=2
 superIts=0
 Repeat
  negChances=Rnd(1,5)
  its=0
  Repeat
   negDate=Rnd(1,48) : its=its+1
  Until (negDate=>gamDate And negDate=<gamDate+7 And gamSchedule(negDate)=>1 And gamSchedule(negDate)=<2 And gamOpponent(negDate)=0 And InjuryDate(negDate)=0) Or its>1000
  If negDate<gamDate Then negDate=gamDate
  negDatePrefer(negDate)=1
  Repeat
   negMatch=Rnd(1,no_matches)
   randy=Rnd(0,3)
   If randy=<1 Then negMatch=Rnd(2,9)
   If randy=2 Then negMatch=2
  Until negMatchPrefer(negMatch)=1
  Repeat
   negGimmick=Rnd(0,no_gimmicks)
   randy=Rnd(0,2)
   If randy=0 Then negGimmick=0
  Until negGimmickPrefer(negGimmick)=1
  Repeat
   negVenue=Rnd(1,no_arenas)
   If gamSchedule(negDate)=1 Then negVenue=Rnd(1,10)
   If gamSchedule(negDate)=2 
    randy=Rnd(0,1)
    If randy=0 Then negVenue=Rnd(21,no_arenas) Else negVenue=Rnd(11,no_arenas)
   EndIf
  Until negVenuePrefer(negVenue)=1
  superIts=superIts+1
 Until GetMatchVerdict()=0 Or superIts>1000
EndIf
;force gym at school
If fed=7
 If gamSchedule(negDate)=1 Then negVenue=1 Else negVenue=11
 negVenuePrefer(1)=1 : negVenuePrefer(11)=1
EndIf 
;reset status 
negInitialDate=negDate
negInitialMatch=negMatch
negInitialGimmick=negGimmick
negInitialVenue=negVenue 
negTim=0 : negStage=0
;frame rating
timer=CreateTimer(30)
;MAIN LOOP
foc=1 : oldfoc=foc : charged=0
go=0 : gotim=-20 : keytim=20
While go=0

 Cls
 frames=WaitTimer(timer)
 For framer=1 To frames
	
	;timers
	keytim=keytim-1
	If keytim<1 Then keytim=0    
	
	;PORTAL
    gotim=gotim+1
    If gotim>25 Then negTim=negTim+1 
    ;speed-ups
    If gotim>0 And negStage<>1 And negStage<>3 And keytim=0
	 If KeyDown(1) Or KeyDown(28) Or ButtonPressed() Or MouseDown(1) Then negTim=negTim+50 : keytim=5 ;: PlaySound sMenuBrowse
	EndIf
	
	;CONFIGURATION
	If gotim>20 And keytim=0 And (negStage=1 Or negStage=3)
	 ;highlight option
	 If KeyDown(200) Or JoyYDir()=-1 Then foc=foc-1 : keytim=5
	 If KeyDown(208) Or JoyYDir()=1 Then foc=foc+1 : keytim=5
	 If foc>5 Then foc=0
	 If foc<0 Then foc=5 
	 ;adjust contract
	 If negStage=1 And foc=>1 And foc=<6
	  ;browse left
	  If KeyDown(203) Or JoyXDir()=-1 Or MouseDown(2)
	   PlaySound sMenuBrowse : keytim=5
	   If foc=1 Then negDate=negDate-1
	   If foc=2 Then negMatch=negMatch-1  
       If foc=3 Then negGimmick=negGimmick-1
	   If foc=4 Then negVenue=negVenue-1
	  EndIf
	  ;browse right
	  If KeyDown(205) Or JoyXDir()=1 Or MouseDown(1)
	   PlaySound sMenuBrowse : keytim=5
	   If foc=1 Then negDate=negDate+1
	   If foc=2 Then negMatch=negMatch+1  
       If foc=3 Then negGimmick=negGimmick+1
	   If foc=4 Then negVenue=negVenue+1
	  EndIf
	  ;reset
	  If KeyDown(14)
	   PlaySound sMenuBrowse : keytim=10
	   If foc=1 Then negDate=negInitialDate
	   If foc=2 Then negMatch=negInitialMatch 
       If foc=3 Then negGimmick=negInitialGimmick
	   If foc=4 Then negVenue=negInitialVenue
	  EndIf
	 EndIf
	 ;submit proposal
	 If foc=5 Or KeyDown(28)
	  If KeyDown(28) Or ButtonPressed() Or MouseDown(1) 
	   PlaySound sMenuGo : keytim=20
	   If negStage=1 
	    negStage=2 : negTim=0 : foc=6
	    negVerdict=GetMatchVerdict()
	   EndIf
	   If negStage=3 Or (negTopic=2 And DealChanged()=0) Then negStage=4 : negTim=0
	  EndIf
	 EndIf
	 ;cancel
	 If foc=0 Or KeyDown(1)
	  If KeyDown(1) Or KeyDown(28) Or ButtonPressed() Or MouseDown(1)
	   PlaySound sMenuBack : keytim=20
	   negStage=5 : negTim=0
	  EndIf
	 EndIf
	EndIf
	;check limits
	If negDate<gamDate Then negDate=gamDate
	If negDate>48 Then negDate=48
	If negMatch<1 Then negMatch=no_matches
	If negMatch>no_matches Then negMatch=1
	If negGimmick<0 Then negGimmick=no_gimmicks
	If negGimmick>no_gimmicks Then negGimmick=0
	If negVenue<1 Then negVenue=no_arenas
	If negVenue>no_arenas Then negVenue=1
	
	;PLAYER CYCLE
	For cyc=1 To no_plays
	 If pChar(cyc)>0
	  ;facial expressions
	  RandomizeAnimation(cyc)
	  FacialExpressions(cyc)
	  ManageEyes(cyc) 
	  ;shadows
	  For limb=1 To 50
       If pShadow(cyc,limb)>0
        RotateEntity pShadow(cyc,limb),90,EntityYaw(pLimb(cyc,limb),1),0
        PositionEntity pShadow(cyc,limb),EntityX(pLimb(cyc,limb),1),pY#(cyc)+0.4,EntityZ(pLimb(cyc,limb),1)
       EndIf
      Next
     EndIf   
	Next
	
	;CAMERA
	Camera()
	;music
	ManageMusic(-1)
	
 UpdateWorld
 Next 
 
 ;ANIMATION OVERRIDE
 pFoc(1)=2 :  pFoc(2)=1
 For cyc=1 To 2
  If pFoc(cyc)>0
   If negSetting=>2 Then PointHead(cyc,pLimb(pFoc(cyc),1))
   LookAtPerson(cyc,pFoc(cyc))
  Else
   RotateEntity pLimb(cyc,45),0,0,0
   RotateEntity pLimb(cyc,46),0,0,0
  EndIf
  If charEyeShape(pChar(cyc))=112 Then LookAtPerson(cyc,cyc)
 Next
 
 RenderWorld 1

 ;DISPLAY
 DrawImage gLogo(2),rX#(400),rY#(65)
 ;reset speech
 For cyc=1 To no_plays
  pSpeaking(cyc)=0
 Next
 ;reset subtitles
 lineA$="" : lineB$=""
 redLineA$="" : redLineB$=""
 greenLineA$="" : greenLineB$=""
 ;------------- INTRODUCTIONS -----------------
 ;1. player initiates match
 If negStage=0 And negTopic=1
  If negTim>25 And negTim<325
   Speak(1,0,2)
   lineA$="Hey, "+charName$(negChar)+", I'd like to talk to you about"
   lineB$="stepping into the ring with me some time?"
   If charPartner(gamChar)>0 Then lineB$="taking my team on in a match some time?"
   If charPartner(gamChar)>0 And TitleHolder(gamChar,3) Then lineB$="giving your team a shot at our Tag titles?"
   If charPartner(gamChar)>0 And TitleHolder(negChar,3) Then lineB$="giving my team a shot at your Tag titles?"
   If TitleHolder(gamChar,2) Then lineB$="giving you a shot at my Inter title some time?"
   If TitleHolder(negChar,2) Then lineB$="getting a shot at your Inter title some time?"
   If TitleHolder(gamChar,1) Then lineB$="giving you a shot at my World title some time?"
   If TitleHolder(negChar,1) Then lineB$="getting a shot at your World title some time?"
;new belts
If TitleHolder(gamChar,5) Then lineB$="giving you a shot at my Womens title some time?"
   If TitleHolder(negChar,5) Then lineB$="getting a shot at your Womens title some time?"
   If TitleHolder(gamChar,6) Then lineB$="giving you a shot at my US title some time?"
   If TitleHolder(negChar,6) Then lineB$="getting a shot at your US title some time?"
  If TitleHolder(gamChar,7) Then lineB$="giving you a shot at my TV title some time?"
   If TitleHolder(negChar,7) Then lineB$="getting a shot at your TV title some time?"
;end
  EndIf
  If negTim>325 Then camFoc=2
  If negTim>350 And negTim<650 And negChances>0 And negInterest=1 And charRelationship(negChar,gamChar)=>0
   Speak(2,0,1)
   lineA$="I'm not thrilled with the idea of working with you,"
   lineB$="but tell me what you're thinking and we'll see..."
  EndIf
  If negTim>350 And negTim<650 And negChances>0 And negInterest=2 And charRelationship(negChar,gamChar)=>0
   Speak(2,0,2)
   lineA$="Yeah, I suppose we could put on a decent match."
   lineB$="What kind of arrangement did you have in mind?"
  EndIf
  If negTim>350 And negTim<650 And negChances>0 And negInterest=3 And charRelationship(negChar,gamChar)=>0
   Speak(2,0,3)
   lineA$="Sure, I'd love to step into the ring against you!"
   lineB$="What kind of arrangement did you have in mind?"
  EndIf
  If negTim>350 And negTim<650 And negChances>0 And charRelationship(negChar,gamChar)<0
   Speak(2,0,1)
   lineA$="Haha, I'd love an excuse to get my hands on you!"
   lineB$="It's your funeral. What do you have in mind?"
  EndIf
  If negTim>350 And negTim<650 And negChances=0 And negVerdict=1 And charRelationship(negChar,gamChar)=>0
   Speak(2,0,1)
   redLineA$="No, I'm not interested in wrestling you."
   redLineB$="I don't think that would help my career..."
  EndIf
  If negTim>350 And negTim<650 And negChances=0 And negVerdict=1 And charRelationship(negChar,gamChar)<0
   Speak(2,0,1)
   redLineA$="As much as I'd love to kick your pathetic ass,"
   redLineB$="I'm afraid you're simply not worth my time..."
  EndIf
  If negTim>350 And negTim<650 And negChances=0 And negVerdict=2
   Speak(2,0,3)
   redLineA$="Sorry, but I'm still recovering from an injury."
   redLineB$="I'd rather not make any plans until I'm fit..."
  EndIf
  If negTim>350 And negTim<650 And negChances=0 And negVerdict=3
   Speak(2,0,3)
   redLineA$="Sorry, but I'm already booked up for the rest of"
   redLineB$="the year. Let's talk about this some other time..."
  EndIf
  If negTim>350 And negTim<650 And negChances=0 And negVerdict=4
   Speak(2,0,3)
   redLineA$="Sorry, but I don't consider myself to be a wrestler!"
   redLineB$="I've got more important things to do with my time..."
  EndIf
  If negTim>350 And negTim<650 And negChances=0 And negVerdict=5
   Speak(2,0,1)
   redLineA$="I've already fought you recently! The beating"
   redLineB$="I gave you must have screwed up your memory..."
  EndIf 
  If negTim>350 And negTim<650 And negChances=0 And negVerdict=6
   Speak(2,0,1)
   redLineA$="I'm already booked to fight against you!"
   redLineB$="Trust me, one ass-kicking will be enough..."
  EndIf
  If negTim>350 And negTim<650 And negChances=0 And negVerdict=7
   Speak(2,0,3)
   redLineA$="Sorry, but I've got a tournament to concentrate on."
   redLineB$="I won't be able to make any plans until it's over..."
  EndIf
  If negTim>350 And negTim<650 And negChances=0 And negVerdict=8
   Speak(2,0,1)
   redLineA$="We've already talked about this enough for one day!"
   redLineB$="Come back when you've given it some more thought..."
  EndIf
  If negTim>350 And negTim<650 And negChances=0 And negVerdict=9
   Speak(2,0,1)
   redLineA$="Who the hell are you?! I'm not wasting my time"
   redLineB$="on some amateur who's still learning the ropes..."
  EndIf
  If negTim>350 And negTim<650 And negChances=0 And negVerdict=10
   Speak(2,0,1)
   redLineA$="Sorry, but I don't think we'd be a good match."
   redLineB$="You're not the kind of opponent I excel against..."
  EndIf
  If negTim>350 And negTim<650 And negChances=0 And negVerdict=11
   Speak(2,0,1)
   redLineA$="Why would I put my reputation on the line against YOU?!"
   redLineB$="I'd have everything to lose and nothing to gain..."
  EndIf
  If negTim>350 And negTim<650 And negChances=0 And negVerdict=12
   Speak(2,0,1)
   redLineA$="Sorry, but you're not worthy of a title shot!"
   redLineB$="I've got too many other offers to consider..."
  EndIf
  If negTim>350 And negTim<650 And negChances=0 And negVerdict=13
   Speak(2,0,3)
   redLineA$="We're supposed to be friends, "+charName$(gamChar)+"!"
   redLineB$="Let's not jeopardize that unless we have to..."
  EndIf
  If negTim>350 And negTim<650 And negChances=0 And negVerdict=14
   Speak(2,0,3)
   redLineA$="Sorry, but I don't think of myself as a team wrestler."
   redLineB$="Perhaps you should be talking to a dedicated duo..."
  EndIf
  If negTim>350 And negTim<650 And negChances=0 And negVerdict=15
   Speak(2,0,3)
   redLineA$="Sorry, but I consider myself to be a team wrestler."
   redLineB$="Perhaps you should be talking to somebody else..."
  EndIf
  If negTim>350 And negTim<650 And negChances=0 And negVerdict=16 And charHeel(negChar)=0
   Speak(2,0,3)
   redLineA$="I'm not sure a match between two 'Faces' would work!"
   redLineB$="Every story needs a villain to spice it up a little..."
  EndIf
  If negTim>350 And negTim<650 And negChances=0 And negVerdict=16 And charHeel(negChar)=1
   Speak(2,0,1)
   redLineA$="I'm not sure a match between two 'Heels' would work!"
   redLineB$="Every story needs a hero to make it mean something..."
  EndIf
  If negTim>650 And negChances>0 Then camFoc=1
  If negTim>675 And negChances>0 Then negStage=1 : negTim=0 : keytim=20
  If negTim>700 And negChances=0 Then go=1
 EndIf
 ;2. benign opponent initiates match
 If negStage=0 And negTopic=2 And charRelationship(negChar,gamChar)=>0
  If negTim>25 And negTim<325
   Speak(2,0,2)
   lineA$="Hey, "+charName$(gamChar)+", how would you feel about"
   lineB$="stepping into the ring with me for a match?"
   If charPartner(gamChar)>0 Then lineB$="my team taking on your team in a match?"
   If charPartner(gamChar)>0 And TitleHolder(gamChar,3) Then lineB$="giving my team a shot at those Tag titles?"
   If charPartner(gamChar)>0 And TitleHolder(negChar,3) Then lineB$="taking my team on for the Tag Championships?"
   If TitleHolder(gamChar,2) Then lineB$="giving me a shot at the Inter Championship?"
   If TitleHolder(negChar,2) Then lineB$="taking me on for the Inter Championship?"
   If TitleHolder(gamChar,1) Then lineB$="giving me a shot at the World Championship?"
   If TitleHolder(negChar,1) Then lineB$="taking me on for the World Championship?"
;new belts
If TitleHolder(gamChar,5) Then lineB$="giving me a shot at the Womens Championship?"
   If TitleHolder(negChar,5) Then lineB$="taking me on for the Womens Championship?"
If TitleHolder(gamChar,6) Then lineB$="giving me a shot at the US Championship?"
   If TitleHolder(negChar,6) Then lineB$="taking me on for the US Championship?"
If TitleHolder(gamChar,7) Then lineB$="giving me a shot at the TV Championship?"
   If TitleHolder(negChar,7) Then lineB$="taking me on for the TV Championship?"

;end  
EndIf
  If negTim>350 And negTim<650
   Speak(2,0,3)
   lineA$="I've got a window on the "+DescribeDate$(negDate,0)+","
   lineB$="so this is what I'd like to propose:"
  EndIf
  If negTim>650 Then camFoc=1
  If negTim>675 Then negStage=1 : negTim=0 : keytim=20
 EndIf
 ;2. enemy initiates match
 If negStage=0 And negTopic=2 And charRelationship(negChar,gamChar)<0
  If negTim>25 And negTim<325
   Speak(2,0,1)
   lineA$="Hey, "+charName$(gamChar)+", we've got a score to settle"
   lineB$="so why don't you take me on in the ring?"
  EndIf
  If negTim>350 And negTim<650
   Speak(2,0,1)
   lineA$="I've got a window on the "+DescribeDate$(negDate,0)
   lineB$="and I'd like to spend it kicking your ass!"
  EndIf
  If negTim>650 Then camFoc=1
  If negTim>675 Then negStage=1 : negTim=0 : keytim=20
 EndIf
 ;--------------- ARRANGEMENT SETUP --------------------
 If negStage=1 Or negStage=3
  ;facial expression
  mood=2
  If foc=0 Then mood=1
  If foc=5 Then mood=3
  Speak(1,0,mood)
  ;options
  y=475
  Color 0,0,0 : Rect rX#(0),rY#(y)-30,rX#(800),rY#(300),1
  DrawOption(0,rX#(104),rY#(515),"<<< WITHDRAW <<<","") 
  DrawOption(1,rX#(400),rY#(y),"Date",DescribeDate$(negDate,0)) : y=y+32
  DrawOption(2,rX#(400),rY#(y),"Match",textMatch$(negMatch)) : y=y+32
  DrawOption(3,rX#(400),rY#(y),"Gimmick",textGimmick$(negGimmick)) : y=y+32
  DrawOption(4,rX#(400),rY#(y),"Venue",textArena$(negVenue)) 
  namer$=">>> PROPOSE >>>"
  If negTopic=2 And DealChanged()=0 Then namer$=">>> ACCEPT >>>"
  If negStage=3 Then namer$=">>> CONFIRM >>>"
  DrawOption(5,rX#(696),rY#(515),namer$,"")
  ;arena preview
  If foc=4 And gArena(negVenue)>0
   DrawImage gArena(negVenue),rX#(400),rY#(445)-50
   Color 0,0,0 : Rect rX#(400)-100,rY#(445)-100,200,100,0
  EndIf  
  ;stat reminder
  DrawProfile(negChar,-1,-1,0)
 EndIf
 ;------------------ VERDICTS -----------------------------
 If negStage=2 And negTim>10 And negVerdict=>0 Then camFoc=2
 ;acceptance
 If negStage=2 And negVerdict=0
  If negTim>25 And negTim<325
   If charRelationship(negChar,gamChar)<0 Then mood=1 Else mood=3
   Speak(2,0,mood)
   greenLineA$="OK, I'm happy to sign up for that match!"
   greenLineB$="Just confirm the details and it's a done deal..."
  EndIf
  If negTim>350 Then negStage=3 : negTim=0 : keytim=20
 EndIf
 ;date objections
 If negStage=2 And negVerdict=1
  If negTim>25 And negTim<325
   Speak(2,0,1) 
   redLineA$="The "+DescribeDate$(negDate,0)+" is "+(negDate-gamDate)+" weeks away!"
   redLineB$="Anything could happen between now and then..."
  EndIf
  If negTim>350 Then PushLuck(1) : negTim=10 : keytim=20
 EndIf
 If negStage=2 And negVerdict=2
  If negTim>25 And negTim<325
   Speak(2,0,1) 
   redLineA$="You won't have recovered by the "+DescribeDate$(negDate,0)+"!"
   redLineB$="I'm not getting into the ring with a cripple..."
  EndIf
  If negTim>350 Then PushLuck(1) : negTim=10 : keytim=20
 EndIf
 If negStage=2 And negVerdict=3
  If negTim>25 And negTim<325
   Speak(2,0,1) 
   If gamSchedule(negDate)=-2 Then redLineA$="You're meant to be in court on the"
   If gamSchedule(negDate)=0 Then redLineA$="There isn't even a show on the"
   If gamSchedule(negDate)=3 Then redLineA$="We've got a tournament on the"
   If gamSchedule(negDate)=4 Then redLineA$="We've got an inter-promotional on the"
   If gamSchedule(negDate)=5 Then redLineA$="We've got a charity event on the"
   If gamSchedule(negDate)=6 Then redLineA$="We've got a memorial show on the"
   redLineB$=DescribeDate$(negDate,0)+"! Check your diary..."
  EndIf
  If negTim>350 Then PushLuck(1) : negTim=10 : keytim=20
 EndIf
 If negStage=2 And negVerdict=4
  If negTim>25 And negTim<325
   Speak(2,0,1) 
   If charPartner(gamChar)>0 Then namer$=charTeamName$(gamOpponent(negDate)) Else namer$=charName$(gamOpponent(negDate))
   redLineA$="You're already booked to face "+namer$
   redLineB$="on the "+DescribeDate$(negDate,0)+"! Pay attention..."
  EndIf
  If negTim>350 Then PushLuck(1) : negTim=10 : keytim=20
 EndIf
 If negStage=2 And negVerdict=5
  If negTim>25 And negTim<325
   Speak(2,0,1) 
   redLineA$="Sorry, but I'm too tired to work anytime soon."
   redLineB$="I'll need at least a couple of weeks to rest up..."
  EndIf
  If negTim>350 Then PushLuck(1) : negTim=10 : keytim=20
 EndIf
 If negStage=2 And negVerdict=6
  If negTim>25 And negTim<325
   Speak(2,0,1) 
   redLineA$="No, I'm not diving into the ring right now!"
   redLineB$="I'll need at least a week to get ready..."
  EndIf
  If negTim>350 Then PushLuck(1) : negTim=10 : keytim=20
 EndIf
 If negStage=2 And negVerdict=7
  If negTim>25 And negTim<325
   Speak(2,0,1) 
   redLineA$="No, I've got plans for the "+DescribeDate$(negDate,0)+"."
   redLineB$="It'll have to take place on another date..."
  EndIf
  If negTim>350 Then PushLuck(1) : negTim=10 : keytim=20
 EndIf
 ;match objections
 If negStage=2 And negVerdict=10
  If negTim>25 And negTim<325
   Speak(2,0,1) 
   redLineA$="I'm not putting my title on the line in a"
   redLineB$="'"+textMatch$(negMatch)+"'! Anything could happen..."
  EndIf
  If negTim>350 Then PushLuck(1) : negTim=10 : keytim=20
 EndIf
 If negStage=2 And negVerdict=11
  If negTim>25 And negTim<325
   Speak(2,0,1) 
   redLineA$="Who are we to arrange a match of that size?!"
   redLineB$="We can't speak for other members of the roster..."
  EndIf
  If negTim>350 Then PushLuck(1) : negTim=10 : keytim=20
 EndIf 
 If negStage=2 And negVerdict=12
  If negTim>25 And negTim<325
   Speak(2,0,1) 
   redLineA$="No, I'd rather not have a team contest."
   redLineB$="I'm not sure I could find a decent partner!"
  EndIf
  If negTim>350 Then PushLuck(1) : negTim=10 : keytim=20
 EndIf 
 If negStage=2 And negVerdict=>13 And negVerdict=<16
  If negTim>25 And negTim<325
   Speak(2,0,1) 
   redLineA$="No, I'm not facing YOU in a '"+textMatch$(negMatch)+"'!"
   redLineB$="Everybody knows you'd have an advantage over me..."
  EndIf
  If negTim>350 Then PushLuck(1) : negTim=10 : keytim=20
 EndIf 
 If negStage=2 And negVerdict=17
  If negTim>25 And negTim<325 And negMatch=2
   Speak(2,0,1) 
   redLineA$="Why are we sitting here talking about a normal"
   redLineB$="match? We can have one of those any time!"
  EndIf
  If negTim>25 And negTim<325 And negMatch<>2
   Speak(2,0,1) 
   redLineA$="No, I'm not interested in a '"+textMatch$(negMatch)+"'."
   redLineB$="Surely we can find something more appropriate?"
  EndIf
  If negTim>350 Then PushLuck(1) : negTim=10 : keytim=20
 EndIf 
 If negStage=2 And negVerdict=18
  If negTim>25 And negTim<325
   Speak(2,0,1) 
   redLineA$="I thought we were talking about a TEAM match?!"
   redLineB$="Those rules wouldn't work in a team environment..."
  EndIf
  If negTim>350 Then PushLuck(1) : negTim=10 : keytim=20
 EndIf  
 ;gimmick objections
 If negStage=2 And negVerdict=20
  If negTim>25 And negTim<325
   Speak(2,0,1) 
   redLineA$="No, I'm not putting my reputation on the line"
   redLineB$="in something as stupid as '"+textGimmick$(negGimmick)+"'!"
  EndIf
  If negTim>350 Then PushLuck(1) : negTim=10 : keytim=20
 EndIf 
 If negStage=2 And negVerdict=21
  If negTim>25 And negTim<325
   Speak(2,0,1) 
   redLineA$="No, I'm not facing YOU in '"+textGimmick$(negGimmick)+"'"
   redLineB$="conditions! You'd enjoy that too much..."
  EndIf
  If negTim>350 Then PushLuck(1) : negTim=10 : keytim=20
 EndIf 
 If negStage=2 And negVerdict=22
  If negTim>25 And negTim<325 And negGimmick=0
   Speak(2,0,1) 
   redLineA$="No, we've got to have some sort of gimmick!"
   redLineB$="We can have a meaningless match anytime..."
  EndIf
  If negTim>25 And negTim<325 And negGimmick>0
   Speak(2,0,1) 
   redLineA$="No, I'm not interested in '"+textGimmick$(negGimmick)+"'."
   redLineB$="Surely we can find something more creative?"
  EndIf
  If negTim>350 Then PushLuck(1) : negTim=10 : keytim=20
 EndIf 
 If negStage=2 And negVerdict=23
  If negTim>25 And negTim<325
   Speak(2,0,1) 
   redLineA$="Hey, it's easy to talk about 'Hair Vs Hair'"
   redLineB$="when you haven't got any of it to LOSE!"
  EndIf
  If negTim>350 Then PushLuck(1) : negTim=10 : keytim=20
 EndIf 
 If negStage=2 And negVerdict=24
  If negTim>25 And negTim<325
   Speak(2,0,1) 
   redLineA$="Hey, it's easy to talk about 'Loser Leaves Town'"
   redLineB$="when you haven't got a contract left to LOSE!"
  EndIf
  If negTim>350 Then PushLuck(1) : negTim=10 : keytim=20
 EndIf 
 ;arena objections
 If negStage=2 And negVerdict=30
  If negTim>25 And negTim<325
   Speak(2,0,1) 
   redLineA$="It's a Pay-Per-View on the "+DescribeDate$(negDate,0)+"!"
   redLineB$="We can't stage a big show at a small venue..."
  EndIf
  If negTim>350 Then PushLuck(1) : negTim=10 : keytim=20
 EndIf
 If negStage=2 And negVerdict=31
  If negTim>25 And negTim<325
   Speak(2,0,1) 
   redLineA$="It's only a TV taping on the "+DescribeDate$(negDate,0)+"!"
   redLineB$="We'll never get clearance on such a large venue..."
  EndIf
  If negTim>350 Then PushLuck(1) : negTim=10 : keytim=20
 EndIf
 If negStage=2 And negVerdict=32
  If negTim>25 And negTim<325
   Speak(2,0,1) 
   redLineA$="No, I'm not interested in staging a match at"
   redLineB$="the '"+textArena$(negVenue)+"'. I don't like it there..."
  EndIf
  If negTim>350 Then PushLuck(1) : negTim=10 : keytim=20
 EndIf 
 If negStage=2 And negVerdict=33
  If negTim>25 And negTim<325
   Speak(2,0,1) 
   redLineA$="How can we stage a match at the '"+textArena$(negVenue)+"'?!"
   redLineB$="You know the school only has access to a gym..."
  EndIf
  If negTim>350 Then PushLuck(1) : negTim=10 : keytim=20
 EndIf 
 ;----------------- ENDINGS -----------------------
 If negStage=>4 And negTim>10 Then camFoc=2
 ;successful deal 
 If negStage=4
  If negTim>25 And negTim<325 And charRelationship(negChar,gamChar)=>0
   Speak(2,0,3)
   If charged=0 Then PlaySound sPaper : charged=1
   lineA$="Great, I'm glad we could come to an arrangement!"
   lineB$="I'll see you on the "+DescribeDate$(negDate,0)+"..."
  EndIf
  If negTim>25 And negTim<325 And charRelationship(negChar,gamChar)<0
   Speak(2,0,1)
   If charged=0 Then PlaySound sPaper : charged=1
   lineA$="There's no turning back now, "+charName$(gamChar)+"!"
   lineB$="Your ass is mine on the "+DescribeDate$(negDate,0)+"..."
  EndIf
  If negTim>375 Then go=1
 EndIf
 ;deal breakdown
 If negStage=5
  If negTim>25 And negTim<325
   Speak(2,0,1)
   lineA$="Alright, this obviously isn't going to happen."
   lineB$="Let's just forget about it for the time being..."
  EndIf
  If negTim>375 Then go=1
 EndIf
 ;---------- DISPLAY SUBTITLES ----------
 DisplaySubtitles()
 ;cursor
 If foc<>oldfoc Then oldfoc=foc : PlaySound sMenuSelect 
 DrawImage gCursor,MouseX(),MouseY()
 ;mask shaky start
 If gotim=<0 Then Loader("Please Wait","Meeting "+charName$(negChar))

 Flip
 ;screenshot (F12)
 If KeyHit(88) Then Screenshot()

Wend
;restore sound
;ChannelVolume chTheme,1.0	
FreeTimer timer
;free entities
FreeEntity world
FreeEntity cam 
FreeEntity camPivot
FreeEntity dummy
FreeEntity light(1)
FreeEntity lightPivot
;remove characters
For cyc=1 To no_plays
 If pChar(cyc)>0
  FreeEntity p(cyc)
  For limb=1 To 50
   If pShadow(cyc,limb)>0
    FreeEntity pShadow(cyc,limb)
   EndIf
  Next
 EndIf
Next
;confirm details
If negStage=4 Or negStage=6
 gamOpponent(negDate)=negChar
 gamMatch(negDate)=negMatch
 gamGimmick(negDate)=negGimmick
 gamVenue(negDate)=negVenue
EndIf
;proceed
gamNegotiated(negChar,3)=1
screen=20
End Function

;------------------------------------------------------------------------------
;////////////////////// 57. RECRUIT PARTNER/MANAGER ///////////////////////////
;------------------------------------------------------------------------------
Function Recruitment()
;get setting
Loader("Please Wait","Meeting "+charName$(negChar))
;ChannelVolume chTheme,0.5
fed=charFed(negChar) : negSetting=Rnd(2,13)
If charRole(negChar)>1 Then negSetting=Rnd(2,5)
If negChar=fedBooker(charFed(negChar)) Then negSetting=1 
If gamSchedule(gamDate)=<0 And negSetting>1 Then negSetting=Rnd(2,5)
If negTopic<1 Then camFoc=2 Else camFoc=1
PrepareMeeting()
;upgrade to removal
If negTopic=1 And negChar=charPartner(gamChar) Then negTopic=2
If negTopic=3 And negChar=charManager(gamChar) Then negTopic=4
;partner verdicts
If negTopic=1
 negInterest=Rnd(0,2) : negVerdict=0 
 If charRelationship(negChar,gamChar)>0 Then negInterest=negInterest+1
 If charRelationship(negChar,gamChar)=0 And charTeamHistory(negChar,gamChar)=0 And charHistory(negChar,gamChar)=0
  negInterest=negInterest-1 : negVerdict=14 ;not a friend
 EndIf
 If charRole(negChar)>1 Or negChar=fedBooker(charFed(negChar)) Then negInterest=negInterest-1 : negVerdict=5 ;not a wrestler
 If charHeel(negChar)<>charHeel(gamChar) Then negInterest=negInterest-1 : negVerdict=11 ;wrong allegiance 
 If charAttitude(gamChar)<80 And charAttitude(negChar)>70 And charAttitude(gamChar)=<charAttitude(negChar)-10
  If charRelationship(negChar,gamChar)=0 Then negInterest=negInterest-1 : negVerdict=19 ;bad attitude
 EndIf
 If GetValue(gamChar)>GetValue(negChar)-(GetValue(negChar)/8) Then negInterest=negInterest+1
 If GetValue(gamChar)>GetValue(negChar) Then negInterest=negInterest+1
 If GetValue(gamChar)>GetValue(negChar)+(GetValue(negChar)/8) Then negInterest=negInterest+1 
 If AverageStats(gamChar)<AverageStats(negChar) Then negInterest=negInterest-1 : negVerdict=16 ;scared of doing all the work
 If AverageStats(gamChar)<PercentOf#(AverageStats(negChar),75) Then negInterest=negInterest-1
 If charPopularity(gamChar)=<charPopularity(negChar)-5 Then negInterest=negInterest-1 : negVerdict=15 ;too popular
 If GetWinRate(gamChar,0)=<50 And GetWinRate(gamChar,0)<GetWinRate(negChar,0) Then negInterest=negInterest-1 : negVerdict=18 ;win rate is too poor
 If TitleHolder(gamChar,3) Then negInterest=negInterest+1
 If charPartner(gamChar)>0 Then negInterest=negInterest-1 : negVerdict=17 ;you already have a partner 
 For count=1 To fedSize(charFed(negChar))
  v=fedRoster(charFed(negChar),count)
  If v<>gamChar And v<>negChar
   If charRelationship(gamChar,v)>0 And charRelationship(negChar,v)<0 And negVerdict<>13 Then negVariable=v : negInterest=negInterest-1 : negVerdict=13 ;friends with enemy 
   If charRelationship(gamChar,v)<0 And charRelationship(negChar,v)>0 And negVerdict<>12 Then negVariable=v : negInterest=negInterest-1 : negVerdict=12 ;enemy of friend
  EndIf
 Next
 If charPartner(negChar)>0 Then negInterest=negInterest-1 : negVerdict=10 ;they already have a partner
 If negInterest<0 Then negInterest=0
 If negInterest=0 And negVerdict=0 Then negVerdict=1 ;not interested
 If CountMatches(gamChar,0)=0 Then negInterest=0 : negVerdict=8 ;too green
 If InjuryStatus(negChar)>0 Then negInterest=0 : negVerdict=7 ;injured
 If TournamentStatus(negChar)>0 Then negInterest=0 : negVerdict=6 ;focusing on tournament 
 If charRole(negChar)=3 Or negChar=fedBooker(charFed(negChar)) Then negInterest=0 : negVerdict=5 ;not a wrestler
 For date=gamDate To 48 
  If gamSchedule(date)>0 And gamOpponent(date)=negChar And negVerdict<>4 Then negVariable=date : negInterest=0 : negVerdict=4 ;fighting soon 
 Next
 If TitleHolder(negChar,0)>0 Then negInterest=0 : negVerdict=9 ;not if they're a champion
 If TitleHolder(gamChar,1) Or TitleHolder(gamChar,2) Then negInterest=0 : negVerdict=20 ;not if you're a champion
 If charRelationship(negChar,gamChar)<0 Then negInterest=0 : negVerdict=3 ;no chance with enemies!
 If gamNegotiated(negChar,1)>0 Then negInterest=0 : negVerdict=2 ;already tried
EndIf
If negTopic=2 Then negVerdict=Rnd(0,1)
;manager verdicts
If negTopic=3
 negInterest=Rnd(0,2) : negVerdict=0 
 If charRelationship(negChar,gamChar)>0 Then negInterest=negInterest+1
 If charRelationship(negChar,gamChar)=0 And charTeamHistory(negChar,gamChar)=0 And charHistory(negChar,gamChar)=0
  negInterest=negInterest-1 : negVerdict=14 ;not a friend
 EndIf
 If charSalary(gamChar)=>2000 Then negInterest=negInterest+1
 If charSalary(gamChar)<1000 Then negInterest=negInterest-1 : negVerdict=22 ;not enough money 
 If charRole(negChar)=1 Or negChar=fedBooker(charFed(negChar)) Then negInterest=negInterest-1 : negVerdict=5 ;not a manager
 If charHeel(negChar)<>charHeel(gamChar) Then negInterest=negInterest-1 : negVerdict=11 ;wrong allegiance
 If charAttitude(gamChar)<80 And charAttitude(negChar)>70 And charAttitude(gamChar)=<charAttitude(negChar)-10
  If charRelationship(negChar,gamChar)=0 Then negInterest=negInterest-1 : negVerdict=21 ;bad attitude
 EndIf
 If GetValue(gamChar)>GetValue(negChar)-(GetValue(negChar)/8) Then negInterest=negInterest+1
 If GetValue(gamChar)>GetValue(negChar) Then negInterest=negInterest+1 
 If GetValue(gamChar)>GetValue(negChar)+(GetValue(negChar)/8) Then negInterest=negInterest+1  
 If AverageStats(gamChar)<AverageStats(negChar) Then negInterest=negInterest-1 : negVerdict=16 ;scared of doing all the work
 If charPopularity(gamChar)<charPopularity(negChar) Then negInterest=negInterest-1 : negVerdict=15 ;too popular
 If GetWinRate(gamChar,0)=<50 And GetWinRate(gamChar,0)<GetWinRate(negChar,0) Then negInterest=negInterest-1 : negVerdict=20 ;win rate is too poor 
 If TitleHolder(gamChar,0)>0 Then negInterest=negInterest+1
 If charManager(gamChar)>0 Then negInterest=negInterest-1 : negVerdict=17 ;you already have a manager 
 For count=1 To fedSize(charFed(negChar))
  v=fedRoster(charFed(negChar),count)
  If v<>gamChar And v<>negChar
   If charRelationship(gamChar,v)>0 And charRelationship(negChar,v)<0 And negVerdict<>13 Then negVariable=v : negInterest=negInterest-1 : negVerdict=13 ;friends with enemy 
   If charRelationship(gamChar,v)<0 And charRelationship(negChar,v)>0 And negVerdict<>12 Then negVariable=v : negInterest=negInterest-1 : negVerdict=12 ;enemy of friend
   If charManager(v)=negChar And negVerdict<>10 Then negVariable=v : negInterest=negInterest-1 : negVerdict=10 ;they already have a client
  EndIf
 Next
 If charManager(negChar)>0 Then negInterest=negInterest-1 : negVerdict=18 ;they already have a manager  
 If negInterest<0 Then negInterest=0
 If negInterest=0 And negVerdict=0 Then negVerdict=1 ;not interested
 If TitleHolder(negChar,0)>0 Then negInterest=0 : negVerdict=9 ;no champions
 If CountMatches(gamChar,0)=0 Then negInterest=0 : negVerdict=8 ;too green
 If InjuryStatus(negChar)>0 Then negInterest=0 : negVerdict=7 ;injured
 If TournamentStatus(negChar)>0 Then negInterest=0 : negVerdict=6 ;focusing on tournament 
 If charRole(negChar)=3 Then negInterest=0 : negVerdict=5 ;not a manager
 For date=gamDate To 48 
  If gamSchedule(date)>0 And gamOpponent(date)=negChar And negVerdict<>4 Then negVariable=date : negInterest=0 : negVerdict=4 ;fighting soon 
 Next
 If CountClients(negChar)=>4 Then negInterest=0 : negVerdict=19 ;too many clients
 If charRelationship(negChar,gamChar)<0 Then negInterest=0 : negVerdict=3 ;no chance with enemies!
 If gamNegotiated(negChar,2)>0 Then negInterest=0 : negVerdict=2 ;already tried
EndIf
If negTopic=4 Then negVerdict=Rnd(0,1)
;reset progress
negTim=0 : negStage=0
;frame rating
timer=CreateTimer(30)
;MAIN LOOP
foc=1 : oldfoc=foc : charged=0
go=0 : gotim=-20 : keytim=20
While go=0

 Cls
 frames=WaitTimer(timer)
 For framer=1 To frames
	
	;timers
	keytim=keytim-1
	If keytim<1 Then keytim=0  
	
	;PORTAL
    gotim=gotim+1
    If gotim>25 Then negTim=negTim+1 
    ;speed-ups
    If gotim>0 And negStage<>2 And keytim=0
	 If KeyDown(1) Or KeyDown(28) Or ButtonPressed() Or MouseDown(1) Then negTim=negTim+50 : keytim=5 ;: PlaySound sMenuBrowse
	EndIf
	
	;CONFIGURATION
	If gotim>20 And keytim=0 And negStage=2
	 ;highlight option
	 If KeyDown(200) Or JoyYDir()=-1 Then foc=foc-1 : keytim=10
	 If KeyDown(208) Or JoyYDir()=1 Then foc=foc+1 : keytim=10
	 ;proceed 
     If KeyDown(1) Or KeyDown(28) Or ButtonPressed() Or MouseDown(1)
	  PlaySound sMenuGo : keytim=20
	  If foc=1 Then negStage=3 : negTim=0
	  If foc=2 Or KeyDown(1) Then negStage=4 : negTim=0
	 EndIf 
	EndIf
	;check limits
	If foc>2 Then foc=1
	If foc<1 Then foc=2 
	
	;PLAYER CYCLE
	For cyc=1 To no_plays
	 If pChar(cyc)>0
	  ;facial expressions
	  RandomizeAnimation(cyc)
	  FacialExpressions(cyc)
	  ManageEyes(cyc)
	  ;shadows
	  For limb=1 To 50
       If pShadow(cyc,limb)>0
        RotateEntity pShadow(cyc,limb),90,EntityYaw(pLimb(cyc,limb),1),0
        PositionEntity pShadow(cyc,limb),EntityX(pLimb(cyc,limb),1),pY#(cyc)+0.4,EntityZ(pLimb(cyc,limb),1)
       EndIf
      Next
     EndIf   
	Next
	
	;CAMERA
	Camera()
	;music
	ManageMusic(-1)
	
 UpdateWorld
 Next  
 
 ;ANIMATION OVERRIDE
 pFoc(1)=2 :  pFoc(2)=1
 For cyc=1 To 2
  If pFoc(cyc)>0
   If negSetting=>2 Then PointHead(cyc,pLimb(pFoc(cyc),1))
   LookAtPerson(cyc,pFoc(cyc))
  Else
   RotateEntity pLimb(cyc,45),0,0,0
   RotateEntity pLimb(cyc,46),0,0,0
  EndIf
  If charEyeShape(pChar(cyc))=112 Then LookAtPerson(cyc,cyc)
 Next
 
 RenderWorld 1

 ;DISPLAY
 DrawImage gLogo(2),rX#(400),rY#(65)
 ;reset speech
 For cyc=1 To no_plays
  pSpeaking(cyc)=0
 Next
 ;reset subtitles
 lineA$="" : lineB$=""
 redLineA$="" : redLineB$=""
 greenLineA$="" : greenLineB$=""
 ;------------- INTERCEPTIONS -----------------
 ;not allowed to arrange matches
 If negTopic=-3
  If negTim>25 And negTim<325
   Speak(2,0,1)
   lineA$="Hey, "+charName$(gamChar)+", what's all this I hear"
   lineB$="about you trying to arrange your own matches?"
  EndIf
  If negTim>350 And negTim<650
   Speak(2,0,1)
   lineA$="I'm the boss of "+fedName$(fed)
   lineB$="and you'll fight whoever I tell you to!"
  EndIf
  If negTim>675 And negTim<975
   Speak(2,0,1)
   lineA$="If you wanted to play God you should have"
   lineB$="paid more attention to your contract..."
  EndIf
  If negTim>1025 Then go=1
 EndIf
 ;not allowed to hire managers
 If negTopic=-2
  If negTim>25 And negTim<325
   Speak(2,0,1)
   lineA$="Hey, "+charName$(gamChar)+", what's all this I hear"
   lineB$="about you trying to hire your own manager?"
  EndIf
  If negTim>350 And negTim<650
   Speak(2,0,1)
   lineA$="I'm the boss of "+fedName$(fed)
   lineB$="and you'll work with whoever I give you!"
  EndIf
  If negTim>675 And negTim<975
   Speak(2,0,1)
   lineA$="If you wanted to play God you should have"
   lineB$="paid more attention to your contract..."
  EndIf
  If negTim>1025 Then go=1
 EndIf
 ;not allowed to recruit partners
 If negTopic=-1
  If negTim>25 And negTim<325
   Speak(2,0,1)
   lineA$="Hey, "+charName$(gamChar)+", what's all this I hear"
   lineB$="about you trying to form your own team?"
  EndIf
  If negTim>350 And negTim<650
   Speak(2,0,1)
   lineA$="I'm the boss of "+fedName$(fed)
   lineB$="and you'll team with whoever I give you!"
  EndIf
  If negTim>675 And negTim<975
   Speak(2,0,1)
   lineA$="If you wanted to play God you should have"
   lineB$="paid more attention to your contract..."
  EndIf
  If negTim>1025 Then go=1
 EndIf
 ;------------- INTRODUCTIONS -----------------
 ;new partner
 If negTopic=1 And negStage=0 And charPartner(gamChar)=0 And charTeamHistory(gamChar,negChar)<>2
  If negTim>25 And negTim<325
   Speak(1,0,3)
   lineA$="Hey, "+charName$(negChar)+", how would you feel about"
   lineB$="teaming up with me on a full-time basis?"
  EndIf
  If negTim>325 Then camFoc=2
  If negTim>350 Then negStage=1 : negTim=0 : keytim=20
 EndIf
 ;replace partner
 If negTopic=1 And negStage=0 And charPartner(gamChar)>0 And charTeamHistory(gamChar,negChar)<>2
  If negTim>25 And negTim<325
   Speak(1,0,3)
   lineA$="Hey, "+charName$(negChar)+", how would you feel about"
   lineB$="replacing "+charName$(charPartner(gamChar))+" as my partner?"
  EndIf
  If negTim>325 Then camFoc=2
  If negTim>350 Then negStage=1 : negTim=0 : keytim=20
 EndIf
 ;reinstate partner
 If negTopic=1 And negStage=0 And charTeamHistory(gamChar,negChar)=2
  If negTim>25 And negTim<325
   Speak(1,0,3)
   lineA$="Hey, "+charName$(negChar)+", how would you feel about"
   lineB$="putting that team of ours back together?"
  EndIf
  If negTim>325 Then camFoc=2
  If negTim>350 Then negStage=1 : negTim=0 : keytim=20
 EndIf 
 ;end partnership
 If negTopic=2 And negStage=0
  If negTim>25 And negTim<325
   Speak(1,0,2)
   lineA$="Listen, "+charName$(negChar)+", don't you think it's time we"
   lineB$="disbanded the team and went our separate ways?"
  EndIf
  If negTim>325 Then camFoc=2
  If negTim>350 Then negStage=1 : negTim=0 : keytim=20
 EndIf
 ;new manager
 If negTopic=3 And negStage=0 And charManager(gamChar)=0 And charTeamHistory(gamChar,negChar)<>3
  If negTim>25 And negTim<325
   Speak(1,0,3)
   lineA$="Hey, "+charName$(negChar)+", how would you feel about"
   lineB$="becoming my manager on a full-time basis?"
  EndIf
  If negTim>325 Then camFoc=2
  If negTim>350 Then negStage=1 : negTim=0 : keytim=20
 EndIf
 ;replace manager
 If negTopic=3 And negStage=0 And charManager(gamChar)>0 And charTeamHistory(gamChar,negChar)<>3
  If negTim>25 And negTim<325
   Speak(1,0,3)
   lineA$="Hey, "+charName$(negChar)+", how would you feel about"
   lineB$="replacing "+charName$(charManager(gamChar))+" as my manager?"
  EndIf
  If negTim>325 Then camFoc=2
  If negTim>350 Then negStage=1 : negTim=0 : keytim=20
 EndIf
 ;reinstate manager
 If negTopic=3 And negStage=0 And charTeamHistory(gamChar,negChar)=3
  If negTim>25 And negTim<325
   Speak(1,0,3)
   lineA$="Hey, "+charName$(negChar)+", how would you feel about"
   lineB$="coming back to be my manager once again?"
  EndIf
  If negTim>325 Then camFoc=2
  If negTim>350 Then negStage=1 : negTim=0 : keytim=20
 EndIf
 ;end management
 If negTopic=4 And negStage=0
  If negTim>25 And negTim<325
   Speak(1,0,2)
   lineA$="Listen, "+charName$(negChar)+", do you mind if I drop you"
   lineB$="as my manager and start fending for myself?"
  EndIf
  If negTim>325 Then camFoc=2
  If negTim>350 Then negStage=1 : negTim=0 : keytim=20
 EndIf
 ;--------------- PARTNER VERDICTS --------------------
 ;acceptances
 If KeyDown(49) And negInterest<1 Then negInterest=1 ;cheat guarantee!
 If negTopic=1 And negStage=1 And negInterest>0 
  If negTim>25 And negTim<325
   Speak(2,0,3)
   greenLineA$="Sure, I'd be happy to team up with you! But"
   greenLineB$="are you sure you want to go down that route?"
  EndIf
  If negTim>350 Then negStage=2 : negTim=0 : keytim=20
 EndIf
 If negTopic=2 And negStage=1 And negVerdict=0
  If negTim>25 And negTim<325
   Speak(2,0,3)
   lineA$="Alright, if that's what you want. Are you sure"
   lineB$="you want to throw away what we've got though?"
  EndIf
  If negTim>350 Then negStage=2 : negTim=0 : keytim=20
 EndIf
 ;objections
 If negTopic=2 And negStage=1 And negVerdict=1
  If negTim>25 And negTim<325
   Speak(2,0,1) 
   If charged=0
    gamPromo(gamDate)=0
    charPartner(gamChar)=0 : charPartner(negChar)=0 : AnnounceBreakup()
    ChangeRelationship(negChar,gamChar,-1)
    charHappiness(gamChar)=charHappiness(gamChar)-1 : charAttitude(gamChar)=charAttitude(gamChar)-1
    charHappiness(negChar)=charHappiness(negChar)+PursueValue(charHappiness(negChar),30,0)
    charAttitude(negChar)=charAttitude(negChar)+PursueValue(charAttitude(negChar),30,0)
    gamCasted=0 : charged=1
   EndIf
   lineA$="Fine, you were holding me back anyway!"
   lineB$="Good luck getting anywhere without me..."
  EndIf
  If negTim>375 Then go=1
 EndIf
 If negTopic=1 And negStage=1 And negInterest=0
  If negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,1) 
   redLineA$="Sorry, but I'm not interested in having you"
   redLineB$="as a tag team partner. Try somebody else..."
  EndIf
  If negTim>25 And negTim<325 And negVerdict=2
   Speak(2,0,1) 
   redLineA$="I've already told you what I think about this!"
   redLineB$="Go and pester somebody else with your ideas..."
  EndIf
  If negTim>25 And negTim<325 And negVerdict=3
   Speak(2,0,1) 
   redLineA$="Well, look who's come begging for MY help!"
   redLineB$="I would never work with an asshole like you..."
  EndIf
  If negTim>25 And negTim<325 And negVerdict=4
   Speak(2,0,1) 
   redLineA$="We're booked against each other on the"
   redLineB$=DescribeDate$(negVariable,0)+"! I can't help you..."
  EndIf
  If negTim>25 And negTim<325 And negVerdict=5
   Speak(2,0,3) 
   redLineA$="Sorry, but I don't think of myself as a wrestler!"
   redLineB$="There are plenty of other people to team with..."
  EndIf
  If negTim>25 And negTim<325 And negVerdict=6
   Speak(2,0,3) 
   redLineA$="Sorry, but I've got a tournament to concentrate on."
   redLineB$="I won't be able to make any plans until it's over..."
  EndIf
  If negTim>25 And negTim<325 And negVerdict=7
   Speak(2,0,3) 
   redLineA$="Sorry, but I'm still recovering from an injury."
   redLineB$="I'd rather not make any plans until I'm fit..."
  EndIf
  If negTim>25 And negTim<325 And negVerdict=8
   Speak(2,0,1) 
   redLineA$="Who the hell are you?! I'm not wasting my time"
   redLineB$="on some amateur who's still learning the ropes..."
  EndIf
  If negTim>25 And negTim<325 And negVerdict=9 And TitleHolder(negChar,0)=1
   Speak(2,0,1) 
   redLineA$="In case you haven't noticed, I'm the World"
   redLineB$="Champion! Team matches are beneath me..."
  EndIf
  If negTim>25 And negTim<325 And negVerdict=9 And TitleHolder(negChar,0)=2
   Speak(2,0,1) 
   redLineA$="In case you haven't noticed, I'm the Inter"
   redLineB$="Champion! I haven't got time for teams..."
  EndIf
  If negTim>25 And negTim<325 And negVerdict=9 And TitleHolder(negChar,0)=3
   Speak(2,0,1) 
   redLineA$="In case you haven't noticed, I'm already a"
   redLineB$="Tag Champion! I'm not giving that up for you..."
  EndIf
  If negTim>25 And negTim<325 And negVerdict=10
   Speak(2,0,1) 
   redLineA$="Sorry, but I've already got a partner - and"
   redLineB$="I'm not giving up "+charName$(charPartner(negChar))+" for you!"
  EndIf
  If negTim>25 And negTim<325 And negVerdict=11 And charHeel(negChar)=1
   Speak(2,0,1) 
   redLineA$="In case you haven't noticed, I'm a Heel!"
   redLineB$="I could never be seen teaming with a Face..."
  EndIf
  If negTim>25 And negTim<325 And negVerdict=11 And charHeel(negChar)=0
   Speak(2,0,1) 
   redLineA$="In case you haven't noticed, I'm a Face!"
   redLineB$="I could never be seen teaming with a Heel..."
  EndIf
  If negTim>25 And negTim<325 And negVerdict=12
   Speak(2,0,1) 
   redLineA$=charName$(negVariable)+" is a good friend of mine!"
   redLineB$="I can't piss "+Him$(charGender(negVariable))+" off by teaming with you..."
  EndIf
  If negTim>25 And negTim<325 And negVerdict=13
   Speak(2,0,1) 
   redLineA$="You're friends with that asshole, "+charName$(negVariable)+"!"
   redLineB$="I could never team with a friend of "+Lower$(His$(charGender(negVariable)))+"..."
  EndIf
  If negTim>25 And negTim<325 And negVerdict=14
   Speak(2,0,1) 
   redLineA$="Who the hell are you?! Come on, I can't trust"
   redLineB$="my career to somebody that I hardly even know..."
  EndIf
  If negTim>25 And negTim<325 And negVerdict=15
   Speak(2,0,1) 
   redLineA$="Why would I associate with a nobody like YOU?!"
   redLineB$="I'd have everything to lose and nothing to gain..."
  EndIf
  If negTim>25 And negTim<325 And negVerdict=16
   Speak(2,0,1) 
   redLineA$="Sorry, but everyone knows I'm a better wrestler"
   redLineB$="than you! I'd be the one doing all the work..."
  EndIf
  If negTim>25 And negTim<325 And negVerdict=17
   Speak(2,0,1) 
   redLineA$="Come on, you've already got a partner!"
   redLineB$="I'm not treading on "+charName$(charPartner(gamChar))+"'s toes..."
  EndIf
  If negTim>25 And negTim<325 And negVerdict=18
   Speak(2,0,1) 
   redLineA$="Hey, don't bring your "+GetWinRate(gamChar,0)+"% win rate near me!"
   redLineB$="Teaming with you would be career suicide..."
  EndIf
  If negTim>25 And negTim<325 And negVerdict=19
   Speak(2,0,1) 
   redLineA$="No, I can't trust you with my career! Your"
   redLineB$="attitude doesn't exactly inspire confidence..."
  EndIf
  If negTim>25 And negTim<325 And negVerdict=20
   Speak(2,0,1) 
   If TitleHolder(gamChar,2) Then redLineA$="Have you forgotten that you're the Inter Champion?!"
   If TitleHolder(gamChar,1) Then redLineA$="Have you forgotten that you're the World Champion?!"
   redLineB$=charName$(fedBooker(charFed(gamChar)))+" would never let you form a team..."
  EndIf
  If negTim>375 Then go=1
 EndIf
 ;--------------- MANAGEMENT VERDICTS --------------------
 ;acceptances
 If KeyDown(49) And negInterest<1 Then negInterest=1 ;cheat guarantee!
 If negTopic=3 And negStage=1 And negInterest>0
  If negTim>25 And negTim<325
   Speak(2,0,3)
   greenLineA$="Sure, I'd be happy to lend you my services!"
   greenLineB$="It'll cost you 10% of your earnings though?"
  EndIf
  If negTim>350 Then negStage=2 : negTim=0 : keytim=20
 EndIf
 If negTopic=4 And negStage=1 And negVerdict=0
  If negTim>25 And negTim<325
   Speak(2,0,3)
   lineA$="Alright, if that's what you want. Are you sure"
   lineB$="you're ready to go out there alone though?"
  EndIf
  If negTim>350 Then negStage=2 : negTim=0 : keytim=20
 EndIf
 ;objections
 If negTopic=4 And negStage=1 And negVerdict=1
  If negTim>25 And negTim<325
   Speak(2,0,1) 
   If charged=0
    charManager(gamChar)=0 : AnnounceBreakup()
    ChangeRelationship(negChar,gamChar,-1)
    charHappiness(gamChar)=charHappiness(gamChar)-1 : charAttitude(gamChar)=charAttitude(gamChar)-1
    charHappiness(negChar)=charHappiness(negChar)+PursueValue(charHappiness(negChar),30,0)
    charAttitude(negChar)=charAttitude(negChar)+PursueValue(charAttitude(negChar),30,0)
    charged=1
   EndIf
   lineA$="Fine, you weren't worth my time anyway!"
   lineB$="Good luck getting anywhere without me..."
  EndIf
  If negTim>375 Then go=1
 EndIf
 If negTopic=3 And negStage=1 And negInterest=0
  If negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,1) 
   redLineA$="Sorry, but I'm not interested in having you as"
   redLineB$="a client. Perhaps you should try somebody else..."
  EndIf
  If negTim>25 And negTim<325 And negVerdict=2
   Speak(2,0,1) 
   redLineA$="I've already told you what I think about this!"
   redLineB$="Go and find somebody else to save your career..."
  EndIf
  If negTim>25 And negTim<325 And negVerdict=3
   Speak(2,0,1) 
   redLineA$="Well, look who's come begging for MY help!"
   redLineB$="I would never work with an asshole like you..."
  EndIf
  If negTim>25 And negTim<325 And negVerdict=4
   Speak(2,0,1) 
   redLineA$="We're booked against each other on the"
   redLineB$=DescribeDate$(negVariable,0)+"! I can't help you..."
  EndIf
  If negTim>25 And negTim<325 And negVerdict=5
   Speak(2,0,1) 
   redLineA$="Sorry, but I don't think of myself as a manager!"
   redLineB$="I've got more important things to worry about..."
  EndIf
  If negTim>25 And negTim<325 And negVerdict=6
   Speak(2,0,3) 
   redLineA$="Sorry, but I've got a tournament to concentrate on."
   redLineB$="I won't be able to make any plans until it's over..."
  EndIf
  If negTim>25 And negTim<325 And negVerdict=7
   Speak(2,0,3) 
   redLineA$="Sorry, but I'm still recovering from an injury."
   redLineB$="I'd rather not make any plans until I'm fit..."
  EndIf
  If negTim>25 And negTim<325 And negVerdict=8
   Speak(2,0,1) 
   redLineA$="Who the hell are you?! I'm not wasting my time"
   redLineB$="on some amateur who's still learning the ropes..."
  EndIf
  If negTim>25 And negTim<325 And negVerdict=9
   Speak(2,0,1) 
   If TitleHolder(negChar,0)=3 Then redLineA$="In case you haven't noticed, I'm a Tag"
   If TitleHolder(negChar,0)=2 Then redLineA$="In case you haven't noticed, I'm the Inter"
   If TitleHolder(negChar,0)=1 Then redLineA$="In case you haven't noticed, I'm the World"
   redLineB$="Champion! I don't have to manage anyone..."
  EndIf
  If negTim>25 And negTim<325 And negVerdict=10
   Speak(2,0,3) 
   redLineA$="Sorry, but I'm already managing "+charName$(negVariable)+"."
   redLineB$="I'd rather not make my life any more complicated!"
  EndIf
  If negTim>25 And negTim<325 And negVerdict=11 And charHeel(negChar)=1
   Speak(2,0,1) 
   redLineA$="In case you haven't noticed, I'm a Heel!"
   redLineB$="I could never be seen managing a Face..."
  EndIf
  If negTim>25 And negTim<325 And negVerdict=11 And charHeel(negChar)=0
   Speak(2,0,1) 
   redLineA$="In case you haven't noticed, I'm a Face!"
   redLineB$="I could never be seen managing a Heel..."
  EndIf
  If negTim>25 And negTim<325 And negVerdict=12
   Speak(2,0,1) 
   redLineA$=charName$(negVariable)+" is a good friend of mine!"
   redLineB$=He$(charGender(negVariable))+"'d be pissed off if I was to help you..."
  EndIf
  If negTim>25 And negTim<325 And negVerdict=13
   Speak(2,0,1) 
   redLineA$="You're friends with that asshole, "+charName$(negVariable)+"!"
   redLineB$="I could never help out a friend of "+Lower$(His$(charGender(negVariable)))+"..."
  EndIf
  If negTim>25 And negTim<325 And negVerdict=14
   Speak(2,0,1) 
   redLineA$="Who the hell are you?! Come on, I can't work"
   redLineB$="with somebody that I hardly even know..."
  EndIf
  If negTim>25 And negTim<325 And negVerdict=15
   Speak(2,0,1) 
   redLineA$="Why would I be subservient to a nobody like"
   redLineB$="YOU?! You should be begging to be MY manager..."
  EndIf
  If negTim>25 And negTim<325 And negVerdict=16
   Speak(2,0,1) 
   redLineA$="Hey, I'm a better wrestler than you! I'd"
   redLineB$="be more use in the ring than at ringside..."
  EndIf
  If negTim>25 And negTim<325 And negVerdict=17
   Speak(2,0,1) 
   redLineA$="Come on, you've already got a manager!"
   redLineB$="I'm not treading on "+charName$(charManager(gamChar))+"'s toes..."
  EndIf
  If negTim>25 And negTim<325 And negVerdict=18
   Speak(2,0,3) 
   redLineA$="Hey, I've already got a manager myself!"
   redLineB$="I'm not sure "+charName$(charManager(negChar))+" would approve..."
  EndIf
  If negTim>25 And negTim<325 And negVerdict=19
   Speak(2,0,3) 
   redLineA$="Sorry, but I've already got plenty of clients!"
   redLineB$="I'd rather not make my life any more complicated..."
  EndIf
  If negTim>25 And negTim<325 And negVerdict=20
   Speak(2,0,1) 
   redLineA$="Sorry, but your "+GetWinRate(gamChar,0)+"% win rate doesn't exactly"
   redLineB$="inspire confidence! I doubt I'd earn anything..."
  EndIf
  If negTim>25 And negTim<325 And negVerdict=21
   Speak(2,0,1) 
   redLineA$="No, I'm not wasting my time on someone like you!"
   redLineB$="You've got a reputation for being difficult..."
  EndIf
  If negTim>25 And negTim<325 And negVerdict=22
   Speak(2,0,1) 
   redLineA$="Why would I manage someone that earns $"+GetFigure$(charSalary(gamChar))+"?"
   redLineB$="There's no point in earning 10% of NOTHING!"
  EndIf
  If negTim>375 Then go=1
 EndIf
 ;--------------- CONFIRMATION --------------------
 If negStage=2
  ;facial expression
  mood=2
  If negTopic=2 Or negTopic=4
   If foc=1 Then mood=1
   If foc=2 Then mood=3
  Else
   If foc=1 Then mood=3
   If foc=2 Then mood=1
  EndIf
  Speak(1,0,mood)
  ;options
  If negTopic=1 And charPartner(gamChar)=0 Then optionA$="Yes, recruit partner!" : optionB$="No, stay solo..."
  If negTopic=1 And charPartner(gamChar)>0 Then optionA$="Yes, replace partner!" : optionB$="No, keep partner..."
  If negTopic=2 Then optionA$="Yes, disband team..." : optionB$="No, keep team!"
  If negTopic=3 And charManager(gamChar)=0 Then optionA$="Yes, hire manager!" : optionB$="No, never mind..."
  If negTopic=3 And charManager(gamChar)>0 Then optionA$="Yes, replace manager!" : optionB$="No, keep manager..."
  If negTopic=4 Then optionA$="Yes, fire manager..." : optionB$="No, keep manager!"
  DrawOption(1,rX#(400),rY#(520),optionA$,"")
  DrawOption(2,rX#(400),rY#(560),optionB$,"")
  ;stat reminder
  If MouseDown(2) Then char=gamChar Else char=negChar
  DrawProfile(char,-1,-1,0)
 EndIf
 ;----------------- ENDINGS -----------------------
 If negStage=>3 And negTim>10 Then camFoc=2
 ;new partnership confirmation
 If negTopic=1 And negStage=3
  If negTim>25 And negTim<325
   Speak(2,0,3)
   If charged=0
    If gamPromo(gamDate)=0 And fed=<6
     gamMatch(gamDate)=12 : gamGimmick(gamDate)=0
     If charTeamHistory(gamChar,negChar)=2 Then gamPromo(gamDate)=62 Else gamPromo(gamDate)=3
    EndIf
    FormTeam(gamChar,negChar)
    charHappiness(gamChar)=charHappiness(gamChar)+1 : charAttitude(gamChar)=charAttitude(gamChar)+1
    If charHappiness(negChar)<75 Then charHappiness(negChar)=75
    charAttitude(negChar)=charAttitude(negChar)+1
    gamCasted=0 : charged=1
   EndIf
   lineA$="Then it's settled, partner! I can't wait to go"
   lineB$="out there and teach the other teams a lesson..."
  EndIf
  If negTim>375 Then go=1
 EndIf
 ;new partnership cancellation
 If negTopic=1 And negStage=4
  If negTim>25 And negTim<325
   Speak(2,0,1)
   If charged=0
    ;ChangeRelationship(negChar,gamChar,-1)
    charHappiness(gamChar)=charHappiness(gamChar)-1 : charAttitude(gamChar)=charAttitude(gamChar)-1
    charHappiness(negChar)=charHappiness(negChar)-1
    charged=1
   EndIf
   lineA$="Then what did you ask me for?! I haven't got time"
   lineB$="for mind games, so go and bother somebody else..."
  EndIf
  If negTim>375 Then go=1
 EndIf
 ;end partnership confirmation
 If negTopic=2 And negStage=3
  If negTim>25 And negTim<325
   Speak(2,0,3)
   If charged=0
    gamPromo(gamDate)=0
    charPartner(gamChar)=0 : charPartner(negChar)=0 : AnnounceBreakup()
    charHappiness(gamChar)=charHappiness(gamChar)+1 : charAttitude(gamChar)=charAttitude(gamChar)-1
    charHappiness(negChar)=charHappiness(negChar)-1 
    gamCasted=0 : charged=1
   EndIf
   lineA$="In that case, I guess there's nothing left to say."
   lineB$="Who knows? Maybe our paths will cross again some time!"
  EndIf
  If negTim>375 Then go=1
 EndIf
 ;end partnership cancellation
 If negTopic=2 And negStage=4
  If negTim>25 And negTim<325
   Speak(2,0,2)
   If charged=0
    charAttitude(gamChar)=charAttitude(gamChar)-1
    charHappiness(negChar)=charHappiness(negChar)-1
    charged=1
   EndIf
   lineA$="Well, I guess that's good news! I haven't got time"
   lineB$="for mind games though, so don't test my patience..."
  EndIf
  If negTim>375 Then go=1
 EndIf
 ;new management confirmation
 If negTopic=3 And negStage=3
  If negTim>25 And negTim<325
   Speak(2,0,3)
   If charged=0
    charManager(gamChar)=negChar
    charHappiness(gamChar)=charHappiness(gamChar)+1 : charAttitude(gamChar)=charAttitude(gamChar)+1
    If charHappiness(negChar)<75 Then charHappiness(negChar)=75
    charAttitude(negChar)=charAttitude(negChar)+1
    PlaySound sCash : charged=1
   EndIf
   lineA$="Then it's settled! With me in your corner, nothing"
   lineB$="can go wrong. Just don't forget to give me my cut..."
  EndIf
  If negTim>375 Then go=1
 EndIf
 ;new management cancellation
 If negTopic=3 And negStage=4
  If negTim>25 And negTim<325
   Speak(2,0,1)
   If charged=0
    ;ChangeRelationship(negChar,gamChar,-1)
    charHappiness(gamChar)=charHappiness(gamChar)-1 : charAttitude(gamChar)=charAttitude(gamChar)-1
    charHappiness(negChar)=charHappiness(negChar)-1
    charged=1
   EndIf
   lineA$="Then what did you ask me for?! I haven't got time"
   lineB$="for mind games, so go and bother somebody else..."
  EndIf
  If negTim>375 Then go=1
 EndIf
 ;end management confirmation
 If negTopic=4 And negStage=3
  If negTim>25 And negTim<325
   Speak(2,0,3)
   If charged=0
    charManager(gamChar)=0 : AnnounceBreakup()
    charHappiness(gamChar)=charHappiness(gamChar)+1 : charAttitude(gamChar)=charAttitude(gamChar)-1
    charHappiness(negChar)=charHappiness(negChar)-1 
    charged=1
   EndIf
   lineA$="In that case, I guess there's nothing left to say."
   lineB$="Good luck making it out there on your own, kid..."
  EndIf
  If negTim>375 Then go=1
 EndIf
 ;end management cancellation
 If negTopic=4 And negStage=4
  If negTim>25 And negTim<325
   Speak(2,0,2)
   If charged=0
    charAttitude(gamChar)=charAttitude(gamChar)-1
    charHappiness(negChar)=charHappiness(negChar)-1
    charged=1
   EndIf
   lineA$="Well, I guess that's good news! I haven't got time"
   lineB$="for mind games though, so don't test my patience..."
  EndIf
  If negTim>375 Then go=1
 EndIf
 ;---------- DISPLAY SUBTITLES ----------
 DisplaySubtitles()
 ;cursor
 If foc<>oldfoc Then oldfoc=foc : PlaySound sMenuSelect 
 DrawImage gCursor,MouseX(),MouseY()
 ;mask shaky start
 If gotim=<0 Then Loader("Please Wait","Meeting "+charName$(negChar))

 Flip
 ;screenshot (F12)
 If KeyHit(88) Then Screenshot()

Wend
;restore sound
;ChannelVolume chTheme,1.0	
FreeTimer timer
;free entities
FreeEntity world
FreeEntity cam 
FreeEntity camPivot
FreeEntity dummy
FreeEntity light(1)
FreeEntity lightPivot
;remove characters
For cyc=1 To no_plays
 If pChar(cyc)>0
  FreeEntity p(cyc)
  For limb=1 To 50
   If pShadow(cyc,limb)>0
    FreeEntity pShadow(cyc,limb)
   EndIf
  Next
 EndIf
Next
;proceed
If negTopic=<2 Then gamNegotiated(negChar,1)=1
If negTopic=>3 Then gamNegotiated(negChar,2)=1
screen=20
End Function

;////////////////////////////////////////////////////////////////////////////
;-------------------------- RELATED FUNCTIONS -------------------------------
;////////////////////////////////////////////////////////////////////////////

;GET BEST FIGURE CHANGER
Function FigureRange(amount)
 ;negative values
 If amount<0 Then value=-10
 If amount=<-1000 Then value=-100
 If amount=<-10000 Then value=-1000
 If amount=<-100000 Then value=-10000
 If amount=<-1000000 Then value=-50000
 If amount=<-10000000 Then value=-100000
 ;positive values
 If amount=>0 Then value=10
 If amount=>1000 Then value=100
 If amount=>10000 Then value=1000
 If amount=>100000 Then value=10000
 If amount=>1000000 Then value=50000
 If amount=>10000000 Then value=100000
 Return value
End Function

;CALCULATE MARKET VALUE
Function GetValue(char)
 value=charPopularity(char)*4
 value=value+charStrength(char)+charSkill(char)+charAgility(char)+charStamina(char)+charToughness(char)
 value=value/8
 ;If charPeaked(char)>0 Then value=value-(value/10)
 If value>99 Then value=99 
 Return value
End Function

;GET BOOKER GENEROSITY
Function GetGenerosity(char)
 value=70+(charAttitude(char)-50)
 If value<50 Then value=50
 Return value
End Function

;CALCULATE WORTH (RAW SALARY)
Function CalculateWorth(char,promotion)
 ;base value
 chunk#=GetValue(char)-40
 If chunk#<10 Then chunk#=10
 magnifier#=chunk#/15
 If magnifier#<1 Then magnifier#=1
 value=Int((chunk#*chunk#)*magnifier#)
 ;filters
 If charFed(char)=7 Then value=value/2 ;unemployed
 If InjuryStatus(char)=>1 And InjuryStatus(char)=<4 Then value=value-(value/4) ;injured
 If InjuryStatus(char)>4 Then value=value/2 ;severely injured
 If charRelationship(fedBooker(promotion),char)<0 Or charRealRelationship(fedBooker(promotion),char)<0
  If game=1 And char=gamChar Then value=value/2 ;boss vendetta
 EndIf
 If promotion=2 Then value=value+(value/4) ;big promotion bonus
 If TitleHolder(char,1)
  value=value+(value/3) ;bonus for world champions
 Else
;newbelts
  If TitleHolder(char,2) Or TitleHolder(char,3) Or TitleHolder(char,5) Or TitleHolder(char,6) Or TitleHolder(char,7) Or charPopularity(char)>fedPopularity(promotion) Then value=value+(value/5) ;bonus for other champions 
;end 
EndIf
 If charRole(char)=>2 Then value=value/2
 ;promotion popularity
 wealth=50+(fedPopularity(promotion)/2) 
 If wealth>100 Then wealth=100
 value=Int(PercentOf#(value,wealth))
 ;booker generosity
 value=Int(PercentOf#(value,GetGenerosity(fedBooker(promotion))))
 ;learning curve
 curve=50+CountMatches(char,0)
 If curve>100 Then curve=100
 value=Int(PercentOf#(value,curve))
 ;If value>charWorth(char)*2 Then value=charWorth(char)*2 
 ;limits
 value=RoundOff(value,10) 
 If value>1000 Then value=RoundOff(value,100)
 If value<100 Then value=100
 Return value
End Function

;GENERATE CPU CONTRACT
Function GenerateContract(char)
 If charFed(char)=7 And char<>fedBooker(7)
  ;independent status
  charWorth(char)=CalculateWorth(char,charFed(char))
  charSalary(char)=0
  charContract(char)=0
  charClause(char,1)=2
  charClause(char,2)=0
  charClause(char,3)=0
 Else
  ;calculate wages
  charContract(char)=Rnd(8,48)
  For count=1 To 3
   charClause(char,count)=Rnd(0,ClauseEntitled(char,charFed(char),count))
   randy=Rnd(0,2)
   If randy=0 And charClause(char,count)<2 Then charClause(char,count)=charClause(char,count)+1
  Next
  charWorth(char)=CalculateWorth(char,charFed(char))
  charSalary(char)=charWorth(char)
  charSalary(char)=ContractFilter(charSalary(char),charContract(char),charFed(char))
  charSalary(char)=ClauseFilter(charSalary(char),charClause(char,1),charClause(char,2),charClause(char,3),charFed(char))
  charSalary(char)=Rnd(PercentOf#(charSalary(char),70),PercentOf#(charSalary(char),120))
  charSalary(char)=RoundOff(charSalary(char),10)
  If charSalary(char)>1000 Then charSalary(char)=RoundOff(charSalary(char),100)
 EndIf
End Function

;FILTER SALARY THROUGH CONTRACT LENGTH
Function ContractFilter(value,contract,promotion)
 factor#=100
 ;negative swing
 If contract<24 And promotion<>3
  ;swing#=PercentOf#(8.0,GetPercent#(charAttitude(negChar)-45,45))
  ;If swing#<2.0 Then swing#=2.0
  ;factor#=GetPercent#((24-(24/swing#))+(Float#(contract)/swing#),24)
  factor#=GetPercent#(24-(Float#(24-contract)/3.0),24)
 EndIf
 ;positive swing
 If contract>24
  ;swing#=Float#(110-charAttitude(negChar))/5
  ;If swing#<3.0 Then swing#=3.0
  ;factor#=GetPercent#((24-(24/swing#))+(Float#(contract)/swing#),24)
  factor#=GetPercent#(24+(Float#(contract-24)/4.0),24)
 EndIf
 ;translate
 value=Int(PercentOf#(value,factor#))
 Return value
End Function

;FILTER SALARY THROUGH LUXURIES
Function ClauseFilter(value,clause1,clause2,clause3,promotion)
 reduction=0
 For count=1 To 2
  ;creative control reductions
  If clause1=>count
   If clause1>ClauseEntitled(char,promotion,1) Then reduction=reduction+10 Else reduction=reduction+5
  EndIf
  ;performance clause reductions
  If clause2=>count
   If clause2>ClauseEntitled(char,promotion,2) Then reduction=reduction+20 Else reduction=reduction+10
  EndIf
  ;health policy reductions
  If clause3=>count
   If clause3>ClauseEntitled(char,promotion,3) Then reduction=reduction+10 Else reduction=reduction+5
  EndIf
 Next
 ;apply reductions
 value=Int(PercentOf#(value,100-reduction))
 Return value
End Function

;FIND CLAUSE ENTITLEMENT
Function ClauseEntitled(char,promotion,clause)
 value=0
 ;creative control
 If clause=1
  experience=CountMatches(char,0)*3
  If experience>100 Then experience=100
  entitle=((charPopularity(char)*2)+experience+charAttitude(char))/4
  If TitleHolder(char,0)>0 Or charPopularity(char)>fedPopularity(promotion) Then entitle=entitle+(entitle/10)
  entitle=PercentOf#(entitle,GetGenerosity(fedBooker(promotion)))
  If entitle=>PercentOf#(fedPopularity(promotion),75) Then value=1
  If entitle=>PercentOf#(fedPopularity(promotion),90) Then value=2
  If promotion=6 Then value=value-1
  If promotion=1 Then value=value+1
 EndIf
 ;performance clause
 If clause=2
  success=GetWinRate(char,0)
  If CountMatches(char,0)<10 Then success=PercentOf#(success,CountMatches(char,0)*10)
  entitle=((AverageStats(char)*2)+success+charAttitude(char))/4
  If TitleHolder(char,0)>0 Or charPopularity(char)>fedPopularity(promotion) Then entitle=entitle+(entitle/10)
  entitle=PercentOf#(entitle,GetGenerosity(fedBooker(promotion)))
  If entitle=>PercentOf#(fedPopularity(promotion),75) Then value=1
  If entitle=>PercentOf#(fedPopularity(promotion),90) Then value=2
  If promotion=4 Then value=value+1
 EndIf
 ;health policy
 If clause=3
  entitle=((charToughness(char)*2)+charPopularity(char)+charAttitude(char))/4
  If TitleHolder(char,0)>0 Or charPopularity(char)>fedPopularity(promotion) Then entitle=entitle+(entitle/10)
  entitle=PercentOf#(entitle,GetGenerosity(fedBooker(promotion)))
  If entitle=>PercentOf#(fedPopularity(promotion),75) Then value=1
  If entitle=>PercentOf#(fedPopularity(promotion),90) Then value=2
  If InjuryStatus(char)>0 Then value=value-1
  If promotion=5 Then value=value+1
 EndIf
 ;limits
 If value<0 Then value=0
 If value>2 Then value=2
 Return value
End Function

;GET CONTRACT VERDICT
Function GetContractVerdict()
 ;translate values
 verdict=0
 negContractWorth=ContractFilter(negWorth,negContract,fed)
 negClauseWorth=ClauseFilter(negContractWorth,negClause(1),negClause(2),negClause(3),fed)
 negPaymentTotal=(negSalary*negContract)+negPayOff
 negInitialPaymentTotal=(negInitialSalary*negInitialContract)+negInitialPayOff
 negPaymentLimit=negClauseWorth*negContract
 ;clause issues
 If negPaymentTotal>negClauseWorth*negContract
  its=0
  Repeat
   randy=Rnd(1,9) : its=its+1
   If negClause(1)<negInitialClause(1) Or negClause(2)<negInitialClause(2) Or negClause(3)<negInitialClause(3) Or (negClause(1)+negClause(2)+negClause(3))=0
    If randy=1 Then verdict=19 ;sacrifice doesn't justify salary
   EndIf
   If negInitialClause(1)>0 Or negInitialClause(2)>0 Or negInitialClause(3)>0
    If (negClause(1)+negClause(2)+negClause(3))=>(negInitialClause(1)+negInitialClause(2)+negInitialClause(3))
     If randy=2 And negSalary+negPayOff>negInitialSalary+negInitialPayOff Then verdict=17 ;generosity ignored
    EndIf
    If (negClause(1)+negClause(2)+negClause(3))>(negInitialClause(1)+negInitialClause(2)+negInitialClause(3))
     If randy=3 And negSalary+negPayOff=>negInitialSalary+negInitialPayOff Then verdict=18 ;asking too much for same price
    EndIf
   EndIf 
   If randy=>4 And randy=<5 And negClause(3)>negInitialClause(3) And negClause(3)>ClauseEntitled(gamChar,fed,3)
    If charAttitude(gamChar)<PercentOf#(charToughness(gamChar),80) Then verdict=16 Else verdict=15 ;not entitled to health cover
   EndIf
   If randy=>6 And randy=<7 And negClause(2)>negInitialClause(2) And negClause(2)>ClauseEntitled(gamChar,fed,2)
    If charAttitude(gamChar)<PercentOf#(AverageStats(gamChar),80) Then verdict=14 Else verdict=13 ;not entitled to performance clause
   EndIf
   If randy=>8 And randy=<9 And negClause(1)>negInitialClause(1) And negClause(1)>ClauseEntitled(gamChar,fed,1)
    If charAttitude(gamChar)<PercentOf#(charPopularity(gamChar),80) Then verdict=12 Else verdict=11 ;not entitled to creative control
   EndIf
  Until verdict>0 Or its>100
 EndIf 
 ;contract issues
 ignoreClauses=0
 randy=Rnd(0,3)
 If randy=0 And verdict=>11 And verdict=<16 Then ignoreClauses=1
 If randy=<1 And verdict=>17 And verdict=<19 Then ignoreClauses=1
 If verdict=0 Then ignoreClauses=1
 If (ignoreClauses=1 And negPaymentTotal>negClauseWorth*negContract) Or negPaymentTotal>negContractWorth*negContract
  its=0
  Repeat
   randy=Rnd(1,5) : its=its+1
   If randy=1 And negSalary>negInitialSalary And negPayOff=<negInitialPayOff Then verdict=5 ;salary is too high
   If randy=2 And negPayOff>negInitialPayOff And negSalary=<negInitialSalary Then verdict=4 ;pay-off is too high
   If randy=3 And negPayOff>negInitialPayOff And negSalary>negInitialSalary Then verdict=3 ;everything is too high 
   If randy=>4 And negContract<negInitialContract Then verdict=7 ;too much for shorter contract
   If randy=>4 And negContract>negInitialContract Then verdict=6 ;too much even for contract rise
   If its>100 Then verdict=0
  Until (verdict=>3 And verdict=<7) Or its>100
  If verdict=0 Then verdict=3
 EndIf
 If negPaymentTotal>negPaymentLimit*2 Then verdict=8 ;obscene demands 
 If (negContract=<4 And negPaymentTotal>negPaymentLimit/2) Or negContract=<1 Then verdict=2 ;contract too short
 If negPayOff>fedBank(fed)/2 Then verdict=1 ;promotion can't afford pay-off
 ;cheat guarantee!
 If KeyDown(49) Then verdict=0
 Return verdict
End Function

;GET MATCH VERDICT
Function GetMatchVerdict()
 verdict=0
 ;30-40. arena objections
 If negVenue<>negInitialVenue Or negTopic=1 
  If negVenuePrefer(negVenue)=0 Then verdict=32 ;doesn't appeal
  If fed=7 And negVenue<>1 And negVenue<>11 Then verdict=33 ;no options at school
  If gamSchedule(negDate)=1 And negVenue>10 Then verdict=31 ;too large for TV
  If gamSchedule(negDate)=2 And negVenue=<10 Then verdict=30 ;too small for PPV
 EndIf
 ;20-30. gimmick objections
 If negGimmick<>negInitialGimmick Or negTopic=1
  If negGimmickPrefer(negGimmick)=0 Then verdict=22 ;gimmick doesn't appeal
  If charToughness(negChar)=<charToughness(gamChar)-10
   If negGimmick=>1 And negGimmick=<8 Then verdict=21 ;doesn't suit violent gimmicks
  EndIf
  If charPopularity(negChar)=>charToughness(gamChar)+10 Or (TitleHolder(negChar,0) And TitleHolder(gamChar,0)=0)
   If negGimmick=>9 And negGimmick=<12 Then verdict=20 ;too popular for humiliating gimmicks
  EndIf
  If negGimmick=9 And charHairStyle(gamChar,1)=<5 Then verdict=23 ;doesn't have hair to lose!
  If negGimmick=10 And charContract(gamChar)=<1 Then verdict=24 ;doesn't have contract to lose!
 EndIf
 ;10-20. match objections
 If negMatch<>negInitialMatch Or negTopic=1
  If negMatchPrefer(negMatch)=0 Then verdict=17 ;match type doesn't appeal
  If negMatch=>3 And negMatch=<5 And charStamina(negChar)=<charStamina(gamChar)-10 Then verdict=16 ;doesn't suit endurance matches
  If negMatch=>6 And negMatch=<7 And charSkill(negChar)=<charSkill(gamChar)-10 Then verdict=15 ;doesn't suit technical matches
  If negMatch=>7 And negMatch=<8 And charStrength(negChar)=<charStrength(gamChar)-10 Then verdict=14 ;doesn't suit KO matches
  If negMatch=9 And charToughness(negChar)=<charToughness(gamChar)-10 Then verdict=13 ;doesn't suit violent matches
  If (negMatch=>12 And negMatch=<13) Or (negMatch=>19 And negMatch=<20)
   If charPartner(negChar)=0 Then verdict=12 ;doesn't have partner for team matches
  EndIf
  If (TitleHolder(gamChar,1)=0 Or charPopularity(gamChar)<fedPopularity(charFed(gamChar))) And (TitleHolder(negChar,1)=0 Or charPopularity(negChar)<fedPopularity(charFed(negChar)))
   If negMatch=10 Or (negMatch=>14 And negMatch=<16) Or (negMatch=>19 And negMatch=<20) Then verdict=11 ;can't arrange huge matches
  EndIf
  If TitleHolder(negChar,0) And TitleHolder(gamChar,0)=0
   If negMatch=1 Or negMatch=9 Or (negMatch=>16 And negMatch=<18) Then verdict=10 ;too gimmicky for title
  EndIf
  If charPartner(gamChar)>0
   If negMatch=10 Or (negMatch=>14 And negMatch=<16) Then verdict=18 ;no multiples for tag teams
  EndIf
 EndIf
 ;1-10. date objections
 If negDate<>negInitialDate Or negTopic=1
  If negDatePrefer(negDate)=0 Then verdict=7 ;date isn't preferred
  If negDate=gamDate And charHealth(negChar)<90 And negDate=gamDate Then verdict=6 ;not fit to fight today!
  If negDate=gamDate+1 And charHealth(negChar)<50 Then verdict=5 ;not enough time to prepare
  If gamOpponent(negDate)>0 Then verdict=4 ;already got opponent
  If gamSchedule(negDate)=<0 Or gamSchedule(negDate)=>3 Then verdict=3 ;event conflict
  If InjuryDate(negDate) Then verdict=2 ;injured on date!
  If negDate>gamDate+4 Then verdict=1 ;too far away
 EndIf 
 ;cheat guarantee!
 If KeyDown(49) Then verdict=0
 Return verdict
End Function

;LOSE CHANCES
Function PushLuck(degree)
 negChances=negChances-degree
 If negChances>0 Then negStage=1 Else negStage=5
End Function

;DEAL CHANGED?
Function DealChanged()
 value=0
 ;contract negotiations
 If screen=52
  If negPayOff<>negInitialPayOff Or negSalary<>negInitialSalary Or negContract<>negInitialContract Then value=1
  For count=1 To 3
   If negClause(count)<>negInitialClause(count) Then value=1
  Next
 EndIf
 ;match arrangement
 If screen=56
  If negDate<>negInitialDate Or negMatch<>negInitialMatch Or negGimmick<>negInitialGimmick Or negVenue<>negInitialVenue Then value=1
 EndIf
 Return value
End Function