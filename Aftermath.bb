;//////////////////////////////////////////////////////////////////////////////
;----------------------- WRESTLING MPIRE 2008: AFTERMATH REPORTS --------------
;//////////////////////////////////////////////////////////////////////////////

;//////////////////////////////////////////////////////////////////////////////
;------------------------------ 21. AFTERMATH ---------------------------------
;//////////////////////////////////////////////////////////////////////////////
Function Aftermath()
;restore photo
gPressPhoto=LoadImage("Graphics/Articles/Photo.bmp")
MaskImage gPressPhoto,255,0,255
;translate health (for all)
For cyc=1 To no_plays
 char=pChar(cyc)
 charOldHealth(char)=charHealth(char)
 charHealth(char)=Int(GetPercent#(pHealth(cyc),optLength*5000))
 For count=0 To 5
  charOldInjured(char,count)=charInjured(char,count)
  If cyc>no_wrestlers And pInjured(cyc,count)>0
   charHealth(char)=0
   If charInjured(char,count)=0 Then charInjured(char,count)=Rnd(2,5)
  EndIf 
 Next
 For limb=1 To 50
  If pScar(cyc,limb)=>5 Then charLimb(char,limb)=0
 Next
 If pRole(cyc)=0 And screenAgenda<>10 And screenAgenda<>12
  charMatches(char,charFed(char))=charMatches(char,charFed(char))+1
  If pTeam(cyc)=pTeam(matchWinner) Then charWins(char,charFed(char))=charWins(char,charFed(char))+1
 EndIf
Next
;clock result
If matchPlayer=<no_wrestlers And screenAgenda<>10 And screenAgenda<>12
 gamResult(gamDate)=2
 If matchWinner>0
  If pTeam(matchWinner)=pTeam(matchPlayer)
   If matchLoser=0 Then matchLoser=FindCharacter(gamOpponent(gamDate))
   If pChar(matchLoser)<>gamChar Then gamOpponent(gamDate)=pChar(matchLoser)
   gamResult(gamDate)=3 
  EndIf
  If pTeam(matchWinner)<>pTeam(matchPlayer)
   If matchLoser=0 Then matchLoser=FindCharacter(gamChar)
   If pChar(matchWinner)<>gamChar Then gamOpponent(gamDate)=pChar(matchWinner)
   gamResult(gamDate)=1 
  EndIf
  charHistory(pChar(matchWinner),pChar(matchLoser))=1
  charHistory(pChar(matchLoser),pChar(matchWinner))=-1
 EndIf
EndIf
;find stat changes
For cyc=1 To no_wrestlers
 ;reset values
 newChamp=0
 eCharged(cyc)=0
 char=pChar(cyc)
 charEvent(char)=0
 ResetOldValues(char)
 ResetNewValues(char)
 ;boost health
 If charManager(char)>0 Then healer=PercentOf#(charStamina(char),125) Else healer=charStamina(char)
 charNewHealth(char)=charNewHealth(char)+Rnd(healer/4,healer/2)
 If FindInjury(cyc)>0 Then charNewHealth(char)=0
 If screenAgenda=12 And matchWinner>0 And pTeam(cyc)<>pTeam(matchWinner) Then charNewHealth(char)=0
 If charNewHealth(char)>charOldHealth(char) Then charNewHealth(char)=charOldHealth(char) 
 If screenAgenda=10 And charNewHealth(char)>charOldHealth(char)-25 Then charNewHealth(char)=charOldHealth(char)-25
 ;spontaneous developments
 If matchMins=>optLength Or screenAgenda=10
  ;stat fluctuation
  randy=Rnd(0,2) : level=Rnd(1,6)
  If screenAgenda=10 Then level=Rnd(2,5)
  If randy=<0 Then charNewStrength(char)=charNewStrength(char)+PursueValue(charStrength(char),statLevel(level),1)
  randy=Rnd(0,2) : level=Rnd(1,6)
  If screenAgenda=10 Then level=Rnd(2,5)
  If randy=<0 Then charNewSkill(char)=charNewSkill(char)+PursueValue(charSkill(char),statLevel(level),1)
  randy=Rnd(0,2) : level=Rnd(1,6)
  If screenAgenda=10 Then level=Rnd(2,5)
  If randy=<0 Then charNewAgility(char)=charNewAgility(char)+PursueValue(charAgility(char),statLevel(level),1)
  randy=Rnd(0,2) : level=Rnd(1,6)
  If screenAgenda=10 Then level=Rnd(2,5)
  If randy=<0 Then charNewStamina(char)=charNewStamina(char)+PursueValue(charStamina(char),statLevel(level),1)
  randy=Rnd(0,2) : level=Rnd(1,6)
  If screenAgenda=10 Then level=Rnd(2,5)
  If randy=<0 Then charNewToughness(char)=charNewToughness(char)+PursueValue(charToughness(char),statLevel(level),1)
  charNewAttitude(char)=charNewAttitude(char)+Rnd(-1,1)
  ;limit changes if old
  If charPeaked(char)>0
   If charNewStrength(char)>charOldStrength(char) Then charNewStrength(char)=charOldStrength(char)
   If charNewSkill(char)>charOldSkill(char) Then charNewSkill(char)=charOldSkill(char)
   If charNewAgility(char)>charOldAgility(char) Then charNewAgility(char)=charOldAgility(char)
   If charNewStamina(char)>charOldStamina(char) Then charNewStamina(char)=charOldStamina(char)
   If charNewToughness(char)>charOldToughness(char) Then charNewToughness(char)=charOldToughness(char)  
  EndIf
  ;acknowledge negative sparring effects
  If screenAgenda=10
   If charNewStrength(char)<charOldStrength(char) Then charEvent(char)=1
   If charNewSkill(char)<charOldSkill(char) Then charEvent(char)=2
   If charNewAgility(char)<charOldAgility(char) Then charEvent(char)=3
   If charNewStamina(char)<charOldStamina(char) Then charEvent(char)=4
   If charNewToughness(char)<charOldToughness(char) Then charEvent(char)=5
   If charEvent(char)=>1 And charEvent(char)=<5
    charNewAttitude(char)=charNewAttitude(char)-Rnd(0,1)
    charNewHappiness(char)=charNewHappiness(char)-Rnd(0,1)
   EndIf 
  EndIf
  ;acknowledge positive sparring effects
  If screenAgenda=10
   If charNewStrength(char)>charOldStrength(char) Then charEvent(char)=11
   If charNewSkill(char)>charOldSkill(char) Then charEvent(char)=12
   If charNewAgility(char)>charOldAgility(char) Then charEvent(char)=13
   If charNewStamina(char)>charOldStamina(char) Then charEvent(char)=14
   If charNewToughness(char)>charOldToughness(char) Then charEvent(char)=15
   If charEvent(char)=>11 And charEvent(char)=<15
    charNewAttitude(char)=charNewAttitude(char)+Rnd(0,1)
    charNewHappiness(char)=charNewHappiness(char)+Rnd(0,1)
   EndIf
  EndIf
 EndIf
 ;winning effects (1-20)
 If matchWinner>0 And pTeam(cyc)=pTeam(matchWinner) And screenAgenda<>10 And screenAgenda<>12
  level=Rnd(0,1) : charEvent(char)=1
  If matchWinner=cyc
   If level<1 Then level=1
   If matchWinStyle=>1 And matchWinStyle=<5
    If no_wrestlers>2 
     If no_wrestlers>4 Then level=level+1 Else level=level+Rnd(0,1) ;big match bonus 
     charEvent(char)=2    
    EndIf
    If matchReward=2 And TitleHolder(char,1) And TitleChanges(fed)=0 Then level=level+1 : charEvent(char)=3 ;world title defence
    If matchReward=3 And TitleHolder(char,2) And TitleChanges(fed)=0 Then level=level+Rnd(0,1) : charEvent(char)=4 ;inter title defence
;new belts
If matchReward=5 And TitleHolder(char,1) And TitleChanges(fed)=0 Then level=level+1 : charEvent(char)=71 ;womens title defence
If matchReward=6 And TitleHolder(char,1) And TitleChanges(fed)=0 Then level=level+1 : charEvent(char)=72 ;US title defence
If matchReward=7 And TitleHolder(char,1) And TitleChanges(fed)=0 Then level=level+1 : charEvent(char)=73 ;TV title defence
;end
    If matchReward=4 And TitleHolder(char,3) And TitleChanges(fed)=0 Then level=level+Rnd(0,1) : charEvent(char)=5 ;tag title defence
    If gamSchedule(gamDate)=>2 Then level=level+Rnd(0,1) : charEvent(char)=6 ;high profile bonus 
    If FindUnderdog(cyc,matchLoser)=cyc Then level=level+Rnd(0,1) : charEvent(char)=7 ;underdog bonus
    If FindSquash()=1 Then level=level+Rnd(0,1) : charEvent(char)=8 ;squash bonus
   EndIf
   For count=1 To 3
    If matchReward=count+1 And TitleHolder(char,count) And TitleChanges(fed)=count ;title win 
     level=level+1 : charEvent(char)=9+count
     charTitles(char,charFed(char),count)=charTitles(char,charFed(char),count)+1
     If count=1 And char=gamChar Then fedLocked(charFed(gamChar))=0
    EndIf
   Next
   If matchReward=>6 And matchReward=<7 Then level=level+1 ;high stakes
   If gamSchedule(gamDate)=>4 Or (charFed(pChar(matchLoser))<>charFed(char) And fed<>7) ;inter-promotional win
    level=level+1 : charEvent(char)=13 
    fedOldPopularity(charFed(char))=fedPopularity(charFed(char))
    fedPopularity(charFed(char))=fedPopularity(charFed(char))+1
   EndIf 
   If screenAgenda=11 ;tournament win
    charEvent(char)=9 
    If cupFoc(cupSlot)=1
     level=level+1
     charTitles(char,charFed(char),7)=charTitles(char,charFed(char),7)+1 ;new belts orig =4
     cupSize(cupSlot)=0
    EndIf
   EndIf
  EndIf
  If level>0
   charNewPopularity(char)=charNewPopularity(char)+PursueValue(charPopularity(char),100,level)
   charNewHappiness(char)=charNewHappiness(char)+PursueValue(charHappiness(char),100,level)
   charHappiness(charManager(char))=charHappiness(charManager(char))+PursueValue(charHappiness(charManager(char)),100,level)
  EndIf
 EndIf
 ;losing effects (20-40)
 If matchWinner>0 And pTeam(cyc)<>pTeam(matchWinner) And screenAgenda<>10 And screenAgenda<>12
  level=Rnd(0,1) : charEvent(char)=21
  If matchLoser=cyc
   If level<1 Then level=1
   If matchWinStyle=0 Then level=5 : charEvent(char)=20 ;quitter
   If matchWinStyle=>1 And matchWinStyle=<5 
    If no_wrestlers>2 Then level=level+Rnd(0,1) : charEvent(char)=22 ;singled out
    If char=fedOldChampWorld(fed) And char<>fedChampWorld(fed) Then level=level+Rnd(0,1) : charEvent(char)=23 ;world title loss
    If char=fedOldChampInter(fed) And char<>fedChampInter(fed) Then level=level+Rnd(0,1) : charEvent(char)=24 ;inter title loss
;new belts
If char=fedOldChampWomens(fed) And char<>fedChampWomens(fed) Then level=level+Rnd(0,1) : charEvent(char)=72 ;womens title loss
If char=fedOldChampUS(fed) And char<>fedChampUS(fed) Then level=level+Rnd(0,1) : charEvent(char)=73 ;US title loss
If char=fedOldChampTV(fed) And char<>fedChampTV(fed) Then level=level+Rnd(0,1) : charEvent(char)=74 ;TV title loss
;end
    If char=fedOldChampTag(fed,1) Or char=fedOldChampTag(fed,2)
     If TitleHolder(char,3)=0 Then level=level+Rnd(0,1) : charEvent(char)=25 ;tag title loss
    EndIf
    If matchReward=>6 And matchReward=<7 Then level=level+Rnd(0,1) ;high stakes
    If gamSchedule(gamDate)=>2 Then level=level+Rnd(0,1) : charEvent(char)=26 ;high profile humiliation 
    If FindUnderdog(matchWinner,cyc)=matchWinner Then level=level+Rnd(0,1) : charEvent(char)=27 ;underdog humiliation
    If FindSquash()=1 Then level=level+Rnd(0,1) : charEvent(char)=28 ;squash humiliation
   EndIf
   If gamSchedule(gamDate)=>4 Or (charFed(pChar(matchLoser))<>charFed(char) And fed<>7) ;inter-promotional effects
    level=level+1 : charEvent(char)=29 
    fedOldPopularity(charFed(char))=fedPopularity(charFed(char))
    fedPopularity(charFed(char))=fedPopularity(charFed(char))-1
   EndIf
  EndIf
  If level>0
   If fed=7 Then limit=30 Else limit=50
   charNewPopularity(char)=charNewPopularity(char)+PursueValue(charPopularity(char),limit,level)
   charNewHappiness(char)=charNewHappiness(char)+PursueValue(charHappiness(char),30,level)
   charHappiness(charManager(char))=charHappiness(charManager(char))+PursueValue(charHappiness(charManager(char)),30,level)
  EndIf
 EndIf
 ;novice exposure
 If fed=<6 And matchType>0 And screenAgenda<>10 And screenAgenda<>12 
  If charPopularity(char)<50
   If charNewPopularity(char)<50 Then charNewPopularity(char)=charPopularity(char)+1
   charNewAttitude(char)=charNewAttitude(char)+1
   charNewHappiness(char)=charNewHappiness(char)+1 
   charEvent(char)=16
  EndIf
 EndIf 
 ;significant draws
 If matchTimeLim>optLength And matchWinner=0 And no_wrestlers=2 And screenAgenda<>10 And screenAgenda<>12
  If (cyc=1 And FindUnderdog(1,2)=1) Or (cyc=2 And FindUnderdog(1,2)=2)
   charNewPopularity(char)=charPopularity(char)+Rnd(0,1)
   charNewHappiness(char)=charHappiness(char)+Rnd(0,1)
   charEvent(char)=17  
  EndIf
  If (cyc=1 And FindUnderdog(1,2)=2) Or (cyc=2 And FindUnderdog(1,2)=1)
   charNewPopularity(char)=charPopularity(char)-Rnd(0,1)
   charNewHappiness(char)=charHappiness(char)-Rnd(0,1)
   charEvent(char)=31 
  EndIf
 EndIf
 ;credit for tournament final
 If screenAgenda=11 And cupFoc(cupSlot)=1 And matchWinner>0 And pTeam(cyc)<>pTeam(matchWinner)
  charNewPopularity(char)=charPopularity(char)+Rnd(0,1)
  charNewHappiness(char)=charHappiness(char)+Rnd(0,1)
  If ImportantEvent(char)=0 Then charEvent(char)=14
 EndIf 
 ;glass ceiling
 fedLimit=fedPopularity(fed)
 If fedLimit<60 Then fedLimit=60
 If fed>0 And gamSchedule(gamDate)=<3 And charNewPopularity(char)>charPopularity(char) And charNewPopularity(char)=>fedLimit
  If charPopularity(char)<fedLimit
   charNewPopularity(char)=fedLimit
   If ImportantEvent(char)=0 Then charEvent(char)=-1
  EndIf
  If charPopularity(char)=>fedLimit And charNewPopularity(char)<fedLimit+5
   charNewPopularity(char)=charPopularity(char)+1
   If ImportantEvent(char)=0 Then charEvent(char)=-1
  EndIf
  If charPopularity(char)=>fedLimit And charNewPopularity(char)=>fedLimit+5
   charNewPopularity(char)=charPopularity(char)
   If ImportantEvent(char)=0 Then charEvent(char)=-2
  EndIf
 EndIf 
 ;brawl consequences
 If screenAgenda=12  
  If matchWinner>0 And pTeam(cyc)=pTeam(matchWinner)
   charNewPopularity(char)=charNewPopularity(char)+Rnd(0,1)
   charNewHappiness(char)=charNewHappiness(char)+Rnd(0,1)
   charHappiness(charManager(char))=charHappiness(charManager(char))+Rnd(0,1)
   charEvent(char)=15
  EndIf
  If matchWinner>0 And pTeam(cyc)<>pTeam(matchWinner)
   charNewPopularity(char)=charNewPopularity(char)-Rnd(0,1)
   charNewHappiness(char)=charNewHappiness(char)-Rnd(0,1)
   charHappiness(charManager(char))=charHappiness(charManager(char))-Rnd(0,1)
   charEvent(char)=30
  EndIf 
  charNewAttitude(char)=charNewAttitude(char)-1
 EndIf
 ;limit changes below 50
 If charNewPopularity(char)<50 And charNewPopularity(char)<charPopularity(char)-1 Then charNewPopularity(char)=charPopularity(char)-1 
 ;nullify if no changes
 If charNewPopularity(char)=charOldPopularity(char) And screenAgenda<>10
  If ImportantEvent(char)=0 And charEvent(char)>0 Then charEvent(char)=0
 EndIf
 ;acknowledge injuries (40-60)
 For count=0 To 5
  If pInjured(cyc,count)>0 And charInjured(char,count)=0 
   randy=Rnd(0,10)
   If count<>1 And count<>2 And pInjured(cyc,count)>5000 And charInjured(char,count)=0
    If randy=>3 And randy=<5 Then randy=6
   EndIf
   If randy=>7 And randy=<8 And (count=1 Or pInjured(cyc,count)<5000) Then randy=0
   If (charLimb(char,7)=0 And charLimb(char,20)=0) Or (charLimb(char,40)=0 And charLimb(char,43)=0) Then randy=Rnd(7,8)
   If randy=8 And fedSize(9)=>optRosterLim Then randy=0
   If randy=>9 And ImportantEvent(char) Then randy=0
   If randy=<5
    ApplyInjury(char,count,1)
    If ImportantEvent(char)=0 Then charEvent(char)=40+count ;mild injury (40-45)
   EndIf
   If randy=6
    ApplyInjury(char,count,2)
    If ImportantEvent(char)=0 Then charEvent(char)=46+count ;serious injury (46-51)
   EndIf
   If randy=7 Then ApplyInjury(char,99,2) : charEvent(char)=52 ;paralysis!
   If randy=8 Then fedFatality(charFed(char))=char : MoveChar(char,9) : charEvent(char)=53 ;death! 
   If randy=>9 Then charEvent(char)=54 ;false alarm
  EndIf
 Next
 ;stitches
 If pScar(cyc,1)>0 And InjuryStatus(char)=0
  chance=20/pScar(cyc,1)
  If optGore=>4 Then chance=chance*10
  randy=Rnd(0,chance)
  If randy=0 
   charInjured(char,5)=1 : charNewHealth(char)=0
   charNewToughness(char)=charNewToughness(char)-1
   charNewHappiness(char)=charNewHappiness(char)-1
   charEvent(char)=55
  EndIf 
 EndIf 
 ;acknowledge stipulations (60-80)
 If pHeel(cyc)=0 And charHeel(char)=1
  charHeel(char)=0
  If ImportantEvent(char)=0 Then charEvent(char)=60 ;turned face
 EndIf
 If pHeel(cyc)=1 And charHeel(char)=0
  charHeel(char)=1
  If ImportantEvent(char)=0 Then charEvent(char)=61 ;turned heel 
 EndIf
 If matchWinner>0 And pTeam(cyc)<>pTeam(matchWinner) And matchReward=6 ;lost hair!
  If char=gamChar Then gamAgreement(2)=4
  charNewHappiness(char)=charNewHappiness(char)+PursueValue(charHappiness(char),30,0)
  If ImportantEvent(char)=0 Then charEvent(char)=62 
 EndIf
 If matchWinner>0 And pTeam(cyc)<>pTeam(matchWinner) And matchReward=7 ;leave town!
  charNewHappiness(char)=charNewHappiness(char)+PursueValue(charHappiness(char),30,0)
  For date=gamDate To 48
   If date>gamDate And gamOpponent(date)=char Then gamOpponent(date)=0 : gamPromo(date)=0 : gamMatch(date)=0 : gamGimmick(date)=0
  Next
  If ImportantEvent(char)=0 Then charEvent(char)=63 
 EndIf
 If matchPromo=33 And cyc=matchWinner ;world contender
  If char=gamChar
   date=NextDate()
   gamOpponent(date)=fedChampWorld(charFed(gamChar))
   gamMatch(date)=2 : gamGimmick(date)=0 : gamPromo(date)=0
  EndIf
  If ImportantEvent(char)=0 Then charEvent(char)=64 
 EndIf
 If matchPromo=41 And cyc=matchWinner ;inter contender
  If char=gamChar
   date=NextDate()
   gamOpponent(date)=fedChampInter(charFed(gamChar))
   gamMatch(date)=2 : gamGimmick(date)=0 : gamPromo(date)=0
  EndIf
  If ImportantEvent(char)=0 Then charEvent(char)=65 
 EndIf
 ;shoot promo effects
 If matchPromo=>47 And matchPromo=<49 And cyc=promoActor(1) 
  charNewPopularity(char)=charPopularity(char)+PursueValue(charPopularity(char),100,0) 
  charNewAttitude(char)=charAttitude(char)+PursueValue(charAttitude(char),30,0)
  charNewHappiness(char)=charHappiness(char)+PursueValue(charHappiness(char),100,0) 
  If ImportantEvent(char)=0 Then charEvent(char)=66
 EndIf
 ;endorsement promo effects
 If matchPromo=29 And cyc=promoActor(1) 
  charNewPopularity(char)=charPopularity(char)+PursueValue(charPopularity(char),30,0) 
  charNewAttitude(char)=charAttitude(char)+PursueValue(charAttitude(char),30,0)
  charNewHappiness(char)=charHappiness(char)+PursueValue(charHappiness(char),30,0) 
  ;charHeel(char)=1
  If ImportantEvent(char)=0 Then charEvent(char)=67
 EndIf 
 If screenAgenda=12
  If (negTopic=23 Or matchPromo=56) And pTeam(matchWinner)=1 ;invasion defence
   If cyc=<2 Then charNewHappiness(char)=charNewHappiness(char)+1
   If cyc=>3 Then charNewHappiness(char)=charNewHappiness(char)-1
   If cyc=1 And ImportantEvent(char)=0 Then charEvent(char)=68 
  EndIf
  If (negTopic=23 Or matchPromo=56) And pTeam(matchWinner)=2 ;invasion damage
   gamSchedule(gamDate)=0 
   If cyc=<2 Then charNewHappiness(char)=charNewHappiness(char)+PursueValue(charHappiness(char),30,0)  
   If cyc=>3 Then charNewHappiness(char)=charNewHappiness(char)+1
   If cyc=3 And ImportantEvent(char)=0 Then charEvent(char)=69
  EndIf
  If negTopic=24 And matchWinner=cyc ;riot success
   charNewHappiness(char)=charNewHappiness(char)+PursueValue(charHappiness(char),100,0) 
   charSalary(char)=charSalary(char)+(charSalary(char)/4) 
   If ImportantEvent(char)=0 Then charEvent(char)=70
  EndIf
  If negTopic=24 And matchWinner<>cyc ;riot failure
   charNewHappiness(char)=charNewHappiness(char)-1
   If ImportantEvent(char)=0 Then charEvent(char)=71 
  EndIf
 EndIf
 ;limit changes
 CheckNewValues(char)
Next
;frame rating
timer=CreateTimer(30)
;MAIN LOOP
foc=10 : cyc=0
go=0 : gotim=-30 : keytim=10
While go=0

 Cls
 frames=WaitTimer(timer)
 For framer=1 To frames
	
	;timers
	keytim=keytim-1
	If keytim<1 Then keytim=0
	
	;PROCESS
    gotim=gotim+1
	If gotim>20 And keytim=0
	 ;find next subject
	 If cyc=0 Or KeyDown(1) Or ButtonPressed() Or MouseDown(1)
	  If foc=10 And cyc<no_wrestlers 
	   PlaySound sMenuSelect : keytim=30 : gotim=0
	   cyc=cyc+1 : foc=1
	  EndIf
	  If foc=10 And cyc=no_wrestlers  Then go=1
	 EndIf
	 ;administer changes
	 If cyc=>1 And cyc=<no_wrestlers 
	  AlterStats(pChar(cyc))
	  If keytim>2 And foc=<9
	   If KeyDown(1) Or KeyDown(28) Or ButtonPressed() Or MouseDown(1) Then keytim=2 ;speed up
	  EndIf
	 EndIf
	EndIf	
	;effects
	If gotim=-10 Then ProduceSound(0,sPaper,0,0.5)
	If gotim=-5 Then PlaySound sCrowd(4)
	;music
	ManageMusic(-1)  
	
 UpdateWorld
 Next 
 RenderWorld 1

 ;DISPLAY
 TileImage gBackground,rX#(400),rY#(300)
 If fed>0
  DrawImage gFed(fed),rX#(400),rY#(60) 
 Else 
  If cyc=>1 And cyc=<no_wrestlers Then DrawImage gFed(charFed(pChar(cyc))),rX#(400),rY#(60) Else DrawImage gLogo(2),rX#(400),rY#(60) 
 EndIf
 
 ;show profiles
 If cyc=>1 And cyc=<no_wrestlers And gotim>0
  If gotim>10 Then HighlightStats(pChar(cyc)) 
  DrawProfile(pChar(cyc),-1,-1,0)
 EndIf
 ;MAGAZINE ARTICLE
 If gotim>-10
  x=rX#(400) : y=rY#(390)
  ;images
  DrawImage gMagazine,x,y
  DrawImage gPressPhoto,x-123,y-5
  Color 100,100,100
  Rect x-254,y-72,262,134,0
  Rect x-255,y-73,264,136,0
  Color 0,0,0
  Rect x-251,y-69,256,127,0
  ;smallprint
  SetFont fontNews(0) : Color 100,100,100
  Text x-270,y-208,"Wrestling Review Magazine",0,1
  Text x,y-208,DescribeDate$(gamDate,gamYear),1,1 
  Text x+210,y-208,"comments@wrestlingreview.com",1,1
  ;intro
  SetFont fontNews(1) : Color 70,70,70
  If screenAgenda=10 
   Text x+135,y-50,"All the latest developments from the",1,1
   Text x+135,y-33,"sparring session that took place on",1,1
   Text x+135,y-16,"the "+DescribeDate$(gamDate,gamYear)+":",1,1 
  EndIf
  If screenAgenda=12 
   Text x+135,y-50,"All the latest gossip from the",1,1
   Text x+135,y-33,"chaos that erupted backstage on",1,1
   Text x+135,y-16,"the "+DescribeDate$(gamDate,gamYear)+":",1,1
  EndIf
  If screenAgenda<>10 And screenAgenda<>12
   If gamSchedule(gamDate)=1 And fed<>7
    Text x+135,y-50,"All the latest from the TV show",1,1
    Text x+135,y-33,"that "+fedName$(fed)+" taped on",1,1
    Text x+135,y-16,"the "+DescribeDate$(gamDate,gamYear)+":",1,1
   EndIf
   If gamSchedule(gamDate)=1 And fed=7
    Text x+135,y-50,"All the latest developments from the",1,1
    Text x+135,y-33,"training session that took place on",1,1
    Text x+135,y-16,"the "+DescribeDate$(gamDate,gamYear)+":",1,1
   EndIf
   If gamSchedule(gamDate)=2 And fed<>7
    Text x+135,y-50,"All the latest from the PPV event",1,1
    Text x+135,y-33,"that "+fedName$(fed)+" promoted",1,1
    Text x+135,y-16,"on the "+DescribeDate$(gamDate,gamYear)+":",1,1
   EndIf
   If gamSchedule(gamDate)=2 And fed=7
    Text x+135,y-50,"All the latest from the student",1,1
    Text x+135,y-33,"showcase that took place on",1,1  
    Text x+135,y-16,"the "+DescribeDate$(gamDate,gamYear)+":",1,1
   EndIf
   If gamSchedule(gamDate)=3
    Text x+135,y-50,"All the latest results from the",1,1
    Text x+135,y-33,"tournament that took place on",1,1
    Text x+135,y-16,"the "+DescribeDate$(gamDate,gamYear)+":",1,1
   EndIf
   If gamSchedule(gamDate)=4
    Text x+135,y-50,"All the latest from the inter-",1,1
    Text x+135,y-33,"promotional contest that took place",1,1 
    Text x+135,y-16,"on the "+DescribeDate$(gamDate,gamYear)+":",1,1
   EndIf
   If gamSchedule(gamDate)=5
    Text x+135,y-50,"All the latest from the charity",1,1
    Text x+135,y-33,"event that the industry staged on",1,1  
    Text x+135,y-16,"the "+DescribeDate$(gamDate,gamYear)+":",1,1
   EndIf
   If gamSchedule(gamDate)=6
    Text x+135,y-50,"All the latest from the tribute to",1,1
    Text x+135,y-33,charName$(fedRoster(9,fedSize(9)))+", which took place on",1,1  
    Text x+135,y-16,"the "+DescribeDate$(gamDate,gamYear)+":",1,1
   EndIf
  EndIf
  ;results
  lineA$="" : lineB$=""
  winner$=charName$(pChar(matchWinner)) : loser$=charName$(pChar(matchLoser))
  If matchTeams>0 And no_wrestlers=>4 Then winner$=charTeamName$(pChar(matchWinner)) : loser$=charTeamName$(pChar(matchLoser))
  timing$=matchSecs+" seconds"
  If matchMins=1 Then timing$="1 minute"
  If matchMins>1 Then timing$=matchMins+" minutes"
  If matchWinner>0
   If no_wrestlers=2 And matchType<>4 And matchCountOuts<>3
    If matchWinStyle=0
     lineA$=loser$+" conceded defeat to"
     lineB$=winner$+" after "+timing$+"..."
    EndIf
    If matchWinStyle=1
     lineA$=winner$+" defeated "+loser$
     lineB$="by pinfall after "+timing$+"..."
    EndIf
    If matchWinStyle=2
     lineA$=winner$+" made "+loser$
     lineB$="tap out after "+timing$+"..."
    EndIf
    If matchWinStyle=3
     lineA$=loser$+" was knocked out by"
     lineB$=winner$+" after "+timing$+"..."
    EndIf  
    If matchWinStyle=5
     lineA$=loser$+" was eliminated by"
     lineB$=winner$+" after "+timing$+"..."
    EndIf
    If matchWinStyle=4 Or matchWinStyle=6 Or matchWinStyle=7
     lineA$="The match was awarded to"
     lineB$=winner$+" after "+timing$+"..."
    EndIf
    If matchType=>2 And matchType=<3
     lineA$=winner$+" defeated "+loser$
     If matchTeams>0 
      If pTeam(matchWinner)=1 Then lineB$=teamFalls(1)+"-"+teamFalls(2)+" in "+Lower$(review$)+" contest..." 
      If pTeam(matchWinner)=2 Then lineB$=teamFalls(2)+"-"+teamFalls(1)+" in "+Lower$(review$)+" contest..."
     Else 
      If matchWinner=1 Then lineB$=pFalls(1)+"-"+pFalls(2)+" in "+Lower$(review$)+" contest..."
      If matchWinner=2 Then lineB$=pFalls(2)+"-"+pFalls(1)+" in "+Lower$(review$)+" contest..."
     EndIf
    EndIf
    If screenAgenda=12
     lineA$=winner$+" knocked out "+loser$
     lineB$="in a backstage brawl after "+timing$+"..."
    EndIf
   Else
    If screenAgenda=0 And gamGimmick(gamDate)>0 Then prefix$="'"+textGimmick$(gamGimmick(gamDate))+"' " Else prefix$=""
    lineA$=winner$+" emerged victorious"
    lineB$="in a "+prefix$+""+textMatch$(matchPreset)+"..."
    If screenAgenda=10
     lineA$=winner$+" emerged victorious"
     lineB$="after "+timing$+"..."
    EndIf
    If screenAgenda=12
     lineA$=winner$+" emerged victorious"
     lineB$="in a backstage brawl..."
    EndIf
   EndIf
   If gamSchedule(gamDate)=4
    lineA$=winner$+" chalked up a victory"
    lineB$="for "+fedName$(charFed(pChar(matchWinner)))+"..."
   EndIf
   If screenAgenda=10 And matchWinStyle=0
    lineA$=charName$(gamChar)+" brought the session"
    lineB$="to a close after "+timing$+"..."
   EndIf
  Else
   If screenAgenda=0 And gamGimmick(gamDate)>0 Then prefix$="'"+textGimmick$(gamGimmick(gamDate))+"' " Else prefix$=""
   lineA$="A "+prefix$+""+textMatch$(matchPreset)
   lineB$="failed to produce a winner..."
   If gamSchedule(gamDate)=4 And gamRivalFed(gamDate)>0
    lineA$="A match between "+fedName$(charFed(gamChar))
    lineB$=fedName$(gamRivalFed(gamDate))+" ended in a tie..."
   EndIf
   If screenAgenda=10
    lineA$="The session was brought to"
    lineB$="a close after "+timing$+"..."
   EndIf
   If screenAgenda=12
    lineA$="A backstage brawl was stopped"
    lineB$="before it got out of hand..."
   EndIf
  EndIf
  Text x+135,y+20,lineA$,1,1
  Text x+135,y+37,lineB$,1,1
  ;REPORTS
  If foc=10 And cyc=>1 And cyc=<no_wrestlers
   char=pChar(cyc)
   g=charGender(char)
   SetFont fontNews(3) : Color 0,0,0
   ;standard comment
   If charEvent(char)=0
    namer$="match"
    If screenAgenda=10 Then namer$="session"
    If screenAgenda=12 Then namer$="altercation"
    Text x+20,y+105,charName$(char)+"'s status hasn't been affected",1,1
    Text x+20,y+133,"by the result of that "+namer$+"...",1,1
   EndIf
   ;limitations
   If charEvent(char)=-1
    Text x+20,y+105,charName$(char)+"'s progress is being hindered",1,1
    Text x+20,y+133,"by the limitations of "+fedName$(fed)+"...",1,1
   EndIf
   If charEvent(char)=-2
    Text x+20,y+105,charName$(char)+" has reached the limit",1,1
    Text x+20,y+133,"of "+fedName$(fed)+"'s popularity...",1,1
   EndIf
   ;sparring developments
   If screenAgenda=10
    If charEvent(char)=1
     Text x+20,y+105,charName$(char)+"'s strength has fallen to",1,1
     Text x+20,y+133,charStrength(char)+"% after that sparring session...",1,1
    EndIf
    If charEvent(char)=2
     Text x+20,y+105,charName$(char)+"'s skill has fallen to",1,1
     Text x+20,y+133,charSkill(char)+"% after that sparring session...",1,1
    EndIf
    If charEvent(char)=3
     Text x+20,y+105,charName$(char)+"'s agility has fallen to",1,1
     Text x+20,y+133,charAgility(char)+"% after that sparring session...",1,1
    EndIf
    If charEvent(char)=4
     Text x+20,y+105,charName$(char)+"'s stamina has fallen to",1,1
     Text x+20,y+133,charStamina(char)+"% after that sparring session...",1,1
    EndIf
    If charEvent(char)=5
     Text x+20,y+105,charName$(char)+"'s toughness has fallen to",1,1
     Text x+20,y+133,charToughness(char)+"% after that sparring session...",1,1
    EndIf
    If charEvent(char)=11
     Text x+20,y+105,charName$(char)+"'s strength has improved to",1,1
     Text x+20,y+133,charStrength(char)+"% after that sparring session!",1,1
    EndIf
    If charEvent(char)=12
     Text x+20,y+105,charName$(char)+"'s skill has improved to",1,1
     Text x+20,y+133,charSkill(char)+"% after that sparring session!",1,1
    EndIf
    If charEvent(char)=13
     Text x+20,y+105,charName$(char)+"'s agility has improved to",1,1
     Text x+20,y+133,charAgility(char)+"% after that sparring session!",1,1
    EndIf
    If charEvent(char)=14
     Text x+20,y+105,charName$(char)+"'s stamina has improved to",1,1
     Text x+20,y+133,charStamina(char)+"% after that sparring session!",1,1
    EndIf
    If charEvent(char)=15
     Text x+20,y+105,charName$(char)+"'s toughness has improved to",1,1
     Text x+20,y+133,charToughness(char)+"% after that sparring session!",1,1
    EndIf
   EndIf
   ;win descriptions
   If screenAgenda<>10
    If charEvent(char)=1 And matchTeams=<0
     Text x+20,y+105,charName$(char)+"'s profile has improved by",1,1
     Text x+20,y+133,"securing a victory in that match...",1,1
    EndIf
    If charEvent(char)=1 And matchTeams>0
     Text x+20,y+105,charName$(char)+"'s profile has improved by",1,1
     Text x+20,y+133,"being on the winning team in that match...",1,1
    EndIf
    If charEvent(char)=2
     Text x+20,y+105,charName$(char)+"'s profile has improved significantly",1,1
     Text x+20,y+133,"by being the winning participant in that match!",1,1
    EndIf
    If charEvent(char)=3
     Text x+20,y+105,charName$(char)+"'s profile has improved significantly",1,1
     Text x+20,y+133,"by defending the World Championship in that match!",1,1
    EndIf
    If charEvent(char)=4
     Text x+20,y+105,charName$(char)+"'s profile has improved significantly",1,1
     Text x+20,y+133,"by defending the Inter Championship in that match!",1,1
    EndIf
    If charEvent(char)=5
     Text x+20,y+105,charName$(char)+"'s profile has improved significantly",1,1
     Text x+20,y+133,"by defending the Tag Championships in that match!",1,1
    EndIf 
    If charEvent(char)=6
     Text x+20,y+105,charName$(char)+"'s profile has improved significantly",1,1
     Text x+20,y+133,"by securing a victory at a high profile event!",1,1
    EndIf
    If charEvent(char)=7
     Text x+20,y+105,charName$(char)+"'s profile has improved significantly",1,1
     Text x+20,y+133,"by defeating a superior opponent in that match!",1,1
    EndIf
    If charEvent(char)=8
     Text x+20,y+105,charName$(char)+"'s profile has improved significantly",1,1
     Text x+20,y+133,"by winning so convincingly in that match!",1,1
    EndIf
    If charEvent(char)=9 And screenAgenda=11
     If cupFoc(cupSlot)=1
      If eCharged(cyc)=0 Then PlaySound sCrowd(9) : eCharged(cyc)=1
      If cupTeams(cupSlot)>0 Then namer$=charTeamName$(char) Else namer$=charName$(char)
      Text x+20,y+105,"Congratulations to "+namer$,1,1
      Text x+20,y+133,"for winning the tournament!",1,1
     Else
      nextOpponent=0
      If cupTargetSlot(cupFoc(cupSlot))=2 And cupBracket(cupSlot,cupTargetBracket(cupFoc(cupSlot)),1)>0 Then nextOpponent=cupBracket(cupSlot,cupTargetBracket(cupFoc(cupSlot)),1)
      If cupTargetSlot(cupFoc(cupSlot))=1 And cupBracket(cupSlot,cupTargetBracket(cupFoc(cupSlot)),2)>0 Then nextOpponent=cupBracket(cupSlot,cupTargetBracket(cupFoc(cupSlot)),2)
      If nextOpponent>0 
       If cupTeams(cupSlot)>0
        Text x+20,y+105,charTeamName$(char)+" will meet "+charTeamName$(nextOpponent)+" in",1,1
        Text x+20,y+133,"the next round after winning that match!",1,1
       Else
        Text x+20,y+105,charName$(char)+" will meet "+charName$(nextOpponent)+" in",1,1
        Text x+20,y+133,"the next round after winning that match!",1,1
       EndIf
      Else 
       For b=1 To 32
        If cupTargetBracket(b)=cupTargetBracket(cupFoc(cupSlot)) And cupTargetSlot(b)<>cupTargetSlot(cupFoc(cupSlot)) Then otherBracket=b
       Next
       If cupTeams(cupSlot)>0
        Text x+20,y+105,charTeamName$(char)+" have advanced to the next round and",1,1
        Text x+20,y+133,"will face either "+charTeamName$(cupBracket(cupSlot,otherBracket,1))+" or "+charTeamName$(cupBracket(cupSlot,otherBracket,2))+"!",1,1
       Else 
        Text x+20,y+105,charName$(char)+" has advanced to the next round and",1,1
        Text x+20,y+133,"will face either "+charName$(cupBracket(cupSlot,otherBracket,1))+" or "+charName$(cupBracket(cupSlot,otherBracket,2))+"!",1,1
       EndIf
      EndIf
     EndIf
    EndIf
    If charEvent(char)=10
     If eCharged(cyc)=0 Then ProduceSound(0,sCrowd(9),0,0.5) : eCharged(cyc)=1
     Text x+20,y+105,"Congratulations to "+charName$(char)+" for becoming",1,1
     Text x+20,y+133,"the World Champion of "+fedName$(fed)+"!",1,1
    EndIf
    If charEvent(char)=11
     If eCharged(cyc)=0 Then ProduceSound(0,sCrowd(9),0,0.5) : eCharged(cyc)=1
     Text x+20,y+105,"Congratulations to "+charName$(char)+" for becoming",1,1
     Text x+20,y+133,"the Inter Champion of "+fedName$(fed)+"!",1,1
    EndIf
    If charEvent(char)=12
     If eCharged(cyc)=0 Then ProduceSound(0,sCrowd(9),0,0.5) : eCharged(cyc)=1
     Text x+20,y+105,"Congratulations to "+charTeamName$(char)+" for becoming",1,1
     Text x+20,y+133,"the Tag Champions of "+fedName$(fed)+"!",1,1
    EndIf
    If charEvent(char)=13
     If eCharged(cyc)=0 Then ProduceSound(0,sCrowd(9),0,0.5) : eCharged(cyc)=1
     Text x+20,y+105,charName$(char)+" has raised the profile of",1,1
     Text x+20,y+133,fedName$(charFed(char))+" by winning that match!",1,1
    EndIf
    If charEvent(char)=14
     If eCharged(cyc)=0 Then ProduceSound(0,sCrowd(9),0,0.5) : eCharged(cyc)=1
     Text x+20,y+105,"Congratulations to "+charName$(char)+" for",1,1
     Text x+20,y+133,"reaching the finals of the tournament!",1,1
    EndIf
    If charEvent(char)=15
     Text x+20,y+105,charName$(char)+"'s reputation has improved by",1,1
     Text x+20,y+133,"being involved in that altercation...",1,1
    EndIf
    If charEvent(char)=16
     Text x+20,y+105,charName$(char)+"'s profile has improved",1,1
     Text x+20,y+133,"by being involved in that match...",1,1
    EndIf
    If charEvent(char)=17
     Text x+20,y+105,charName$(char)+"'s profile has improved",1,1
     If char=pChar(1) Then Text x+20,y+133,"by going the distance with "+charName$(pChar(2))+"!",1,1
     If char=pChar(2) Then Text x+20,y+133,"by going the distance with "+charName$(pChar(1))+"!",1,1
    EndIf
   EndIf
   ;loss descriptions
   If screenAgenda<>10
    If charEvent(char)=20
     Text x+20,y+105,charName$(char)+"'s profile has suffered considerably",1,1
     Text x+20,y+133,"by bailing out of that match before it had finished!",1,1
    EndIf
    If charEvent(char)=21 And matchTeams=<0
     Text x+20,y+105,charName$(char)+"'s profile has suffered",1,1
     Text x+20,y+133,"by incurring a loss in that match...",1,1
    EndIf
    If charEvent(char)=21 And matchTeams>0
     Text x+20,y+105,charName$(char)+"'s profile has suffered by",1,1
     Text x+20,y+133,"being on the losing team in that match...",1,1
    EndIf
    If charEvent(char)=22
     Text x+20,y+105,charName$(char)+"'s profile has suffered considerably",1,1
     Text x+20,y+133,"by being the losing participant in that match...",1,1
    EndIf
    If charEvent(char)=23
     Text x+20,y+105,charName$(char)+"'s profile has suffered considerably",1,1
     Text x+20,y+133,"by losing the World Championship in that match...",1,1
    EndIf
    If charEvent(char)=24
     Text x+20,y+105,charName$(char)+"'s profile has suffered considerably",1,1
     Text x+20,y+133,"by losing the Inter Championship in that match...",1,1
    EndIf
    If charEvent(char)=25
     Text x+20,y+105,charName$(char)+"'s profile has suffered considerably",1,1
     Text x+20,y+133,"by losing the Tag Championships in that match...",1,1
    EndIf 
    If charEvent(char)=26
     Text x+20,y+105,charName$(char)+"'s profile has suffered considerably",1,1
     Text x+20,y+133,"by incurring a loss at a high profile event...",1,1
    EndIf
    If charEvent(char)=27
     Text x+20,y+105,charName$(char)+"'s profile has suffered considerably",1,1
     Text x+20,y+133,"by losing to an inferior opponent in that match...",1,1
    EndIf
    If charEvent(char)=28
     Text x+20,y+105,charName$(char)+"'s profile has suffered considerably",1,1
     Text x+20,y+133,"by being defeated so easily in that match...",1,1
    EndIf
    If charEvent(char)=29
     If eCharged(cyc)=0 Then ProduceSound(0,sCrowd(3),0,0.5) : eCharged(cyc)=1
     Text x+20,y+105,charName$(char)+" has damaged the profile of",1,1
     Text x+20,y+133,fedName$(charFed(char))+" by losing that match...",1,1
    EndIf
    If charEvent(char)=30
     Text x+20,y+105,charName$(char)+"'s reputation has been tainted",1,1
     Text x+20,y+133,"by being involved in that altercation...",1,1
    EndIf
    If charEvent(char)=31
     Text x+20,y+105,charName$(char)+"'s profile has suffered",1,1
     If char=pChar(1) Then Text x+20,y+133,"after failing to defeat "+charName$(pChar(2))+"...",1,1
     If char=pChar(2) Then Text x+20,y+133,"after failing to defeat "+charName$(pChar(1))+"...",1,1
    EndIf
   EndIf
   ;injury descriptions
   If charEvent(char)=40
    Text x+20,y+105,charName$(char)+" incurred a minor injury in that match",1,1
    Text x+20,y+133,"and will be out of action for the next "+InjuryStatus(char)+" weeks...",1,1
   EndIf
   If charEvent(char)=>41 And charEvent(char)=<45
    Text x+20,y+105,charName$(char)+" incurred a "+textInjury$(charEvent(char)-40)+" injury in that match",1,1
    Text x+20,y+133,"and will be suffering with it for the next "+charInjured(char,charEvent(char)-40)+" weeks...",1,1
   EndIf
   If charEvent(char)=46
    ChannelPitch chTheme,PercentOf#(chThemePitch,90)
    Text x+20,y+95,charName$(char)+" sustained a serious injury in that match",1,1
    Text x+20,y+120,"and will be hospitalized for the next "+InjuryStatus(char)+" weeks!",1,1
    Text x+20,y+145,"Even then, "+Lower$(He$(g))+" may be a shadow of "+Lower$(His$(g))+" former self...",1,1
   EndIf
   If charEvent(char)=>47 And charEvent(char)=<51
    ChannelPitch chTheme,PercentOf#(chThemePitch,90)
    Text x+20,y+95,charName$(char)+" sustained a serious "+textInjury$(charEvent(char)-46)+" injury in that",1,1
    Text x+20,y+120,"match and will be suffering with it for the next "+charInjured(char,charEvent(char)-46)+" weeks!",1,1
    Text x+20,y+145,"Even then, "+Lower$(He$(g))+" may be a shadow of "+Lower$(His$(g))+" former self...",1,1
   EndIf
   If charEvent(char)=52
    ChannelPitch chTheme,PercentOf#(chThemePitch,90)
    Text x+20,y+95,charName$(char)+" sustained an extremely serious injury in that match.",1,1
    Text x+20,y+120,He$(g)+" was rushed to hospital, where it's feared "+Lower$(He$(g))+" may be paralyzed!",1,1
    Text x+20,y+145,His$(g)+" wrestling career is almost certainly over...",1,1
   EndIf
   If charEvent(char)=53
    ChannelPitch chTheme,PercentOf#(chThemePitch,90)
    Text x+20,y+95,charName$(char)+" sustained an extremely serious injury in that match.",1,1
    Text x+20,y+120,"Despite their best efforts, medics were not able to resuscitate "+Lower$(Him$(g))+".",1,1
    Text x+20,y+145,charName$(char)+" has died as a result of "+Lower$(His$(g))+" injuries. "+He$(g)+" was "+charAge(char)+"...",1,1
   EndIf
   If charEvent(char)=54
    Text x+20,y+105,"It seems "+charName$(char)+"'s injury was a false alarm!",1,1
    Text x+20,y+133,He$(g)+" hasn't suffered any permanent damage...",1,1
   EndIf
   If charEvent(char)=55
    Text x+20,y+105,charName$(char)+" needed stitches after incurring",1,1
    Text x+20,y+133,"some serious facial damage in that match...",1,1
   EndIf
   ;stipulation acknowledgements
   If charEvent(char)=60
    If eCharged(cyc)=0 Then ProduceSound(0,sCrowd(2),0,0.5) : eCharged(cyc)=1
    Text x+20,y+120,"That match established "+charName$(char)+" as a Face...",1,1
   EndIf
   If charEvent(char)=61
    If eCharged(cyc)=0 Then ProduceSound(0,sCrowd(3),0,0.5) : eCharged(cyc)=1
    Text x+20,y+120,"That match established "+charName$(char)+" as a Heel...",1,1
   EndIf   
   If charEvent(char)=62
    If eCharged(cyc)=0 Then ProduceSound(0,sCrowd(8),0,0.5) : eCharged(cyc)=1
    Text x+20,y+105,"In accordance with the stipulations of that match,",1,1
    Text x+20,y+133,charName$(char)+" has had "+Lower$(His$(g))+" hair shaved off!",1,1
   EndIf
   If charEvent(char)=63
    If eCharged(cyc)=0 Then ProduceSound(0,sCrowd(6),0,0.5) : eCharged(cyc)=1
    Text x+20,y+105,"In accordance with the stipulations of that match,",1,1
    Text x+20,y+133,charName$(char)+" must now leave "+fedName$(fed)+"!",1,1
   EndIf
   If charEvent(char)=64
    If eCharged(cyc)=0 Then ProduceSound(0,sCrowd(9),0,0.5) : eCharged(cyc)=1
    Text x+20,y+105,"In accordance with the stipulations of that match,",1,1
    Text x+20,y+133,charName$(char)+" will receive a World title shot!",1,1
   EndIf
   If charEvent(char)=65
    If eCharged(cyc)=0 Then ProduceSound(0,sCrowd(9),0,0.5) : eCharged(cyc)=1
    Text x+20,y+105,"In accordance with the stipulations of that match,",1,1
    Text x+20,y+133,charName$(char)+" will receive an Inter title shot!",1,1
   EndIf
   If charEvent(char)=66
    If eCharged(cyc)=0 Then ProduceSound(0,sCrowd(7),0,0.5) : eCharged(cyc)=1
    Text x+20,y+105,charName$(char)+"'s controversial comments",1,1
    Text x+20,y+133,"have made "+Him$(g)+" the talk of the industry!",1,1
   EndIf
   If charEvent(char)=67
    If eCharged(cyc)=0 Then ProduceSound(0,sCrowd(8),0,0.5) : eCharged(cyc)=1
    Text x+20,y+105,charName$(char)+"'s product endorsement",1,1
    Text x+20,y+133,"has made "+Him$(g)+" a laughing stock!",1,1
   EndIf
   If charEvent(char)=68
    If eCharged(cyc)=0 Then ProduceSound(0,sCrowd(9),0,0.5) : eCharged(cyc)=1
    Text x+20,y+105,fedName$(charFed(char))+" thwarted the",1,1
    Text x+20,y+133,"attempt to sabotage tonight's show!",1,1
   EndIf
   If charEvent(char)=69
    If eCharged(cyc)=0 Then ProduceSound(0,sCrowd(3),0,0.5) : eCharged(cyc)=1
    Text x+20,y+105,fedName$(charFed(char))+" proceeded to",1,1
    Text x+20,y+133,"ruin the venue for tonight's show...",1,1
    ChannelPitch chTheme,PercentOf#(chThemePitch,90)
   EndIf
   If charEvent(char)=70
    If eCharged(cyc)=0 Then ProduceSound(0,sCrowd(9),0,0.5) : eCharged(cyc)=1
    Text x+20,y+105,charName$(char)+" successfully negotiated",1,1
    Text x+20,y+133,"a 25% pay rise because of that protest!",1,1
   EndIf
   If charEvent(char)=71
    Text x+20,y+105,charName$(char)+" failed to negotiate",1,1
    Text x+20,y+133,"a pay rise after that performance...",1,1
   EndIf
   
   ;new belts
   
   If charEvent(char)=72
     Text x+20,y+105,charName$(char)+"'s profile has suffered considerably",1,1
     Text x+20,y+133,"by losing the Womens Championship in that match...",1,1
    EndIf
   
   If charEvent(char)=73
     Text x+20,y+105,charName$(char)+"'s profile has suffered considerably",1,1
     Text x+20,y+133,"by losing the US Championship in that match...",1,1
    EndIf
   
   If charEvent(char)=74
     Text x+20,y+105,charName$(char)+"'s profile has suffered considerably",1,1
     Text x+20,y+133,"by losing the TV Championship in that match...",1,1
    EndIf
   
   ;end
   
  EndIf
  ;prompt
  If foc=10 And gotim>50
   SetFont font(2)
   Outline(">>> PRESS ANY COMMAND TO PROCEED >>>",x,y+190,100,100,100,250,250,250)
  EndIf
 EndIf
 ;cursor
 DrawImage gCursor,MouseX(),MouseY()

 Flip
 ;screenshot (F12)
 If KeyHit(88) Then Screenshot()

Wend
;leave
FreeTimer timer
If go=-1 Then PlaySound sMenuBack Else PlaySound sMenuGo
;remove photo
FreeImage gPressPhoto
;post-report checks
For cyc=1 To no_wrestlers
 ;adjust weight
 If InjuryStatus(pChar(cyc))=0 Then FindWeightChanges(pChar(cyc))
 ;offset injury times
 If screenAgenda<>12
  For count=0 To 5
   If charInjured(pChar(cyc),count)>0 And charOldInjured(pChar(cyc),count)=0 Then charInjured(pChar(cyc),count)=charInjured(pChar(cyc),count)+1 
  Next
 EndIf
Next
;restore length option
optLength=optOldLength
;find consequent angles
If fed>0 And screenAgenda<>10 And screenAgenda<>12 Then BookAngles()
;update tournament status
If screenAgenda=11
 cupResult(cupSlot,cupFoc(cupSlot))=pTeam(matchWinner)
 If cupFoc(cupSlot)>1 And cupResult(cupSlot,cupFoc(cupSlot))>0
  cupBracket(cupSlot,cupTargetBracket(cupFoc(cupSlot)),cupTargetSlot(cupFoc(cupSlot)))=cupBracket(cupSlot,cupFoc(cupSlot),cupResult(cupSlot,cupFoc(cupSlot))) 
  cupFoc(cupSlot)=0
 EndIf
EndIf
;detour to meetings
If screenAgenda<>10 And screenAgenda<>12 And screenAgenda<>13 And screenAgenda<>14 And screenAgenda<>15 And charFed(gamChar)<>9
 RiskLateMeetings()
EndIf
;proceed
If screen<>50 And screen<>55
 screen=24
 If gamResult(gamDate)=0 Then screen=20
 If screenAgenda=10 Then screen=54
 If screenAgenda=11 And cupSpan(cupSlot)=1
  If TournamentStatus(gamChar)=0 Or cupFoc(cupSlot)=1 Then screen=15
 EndIf
 If charFed(gamChar)=9 Then screen=25
EndIf
End Function

;/////////////////////////////////////////////////////////////////////////////////////
;------------------------------ RELATED FUNCTIONS ------------------------------------
;/////////////////////////////////////////////////////////////////////////////////////

;RESET OLD VALUES
Function ResetOldValues(char)
 charOldPopularity(char)=charPopularity(char)
 charOldStrength(char)=charStrength(char)
 charOldSkill(char)=charSkill(char)
 charOldAgility(char)=charAgility(char)
 charOldStamina(char)=charStamina(char)
 charOldToughness(char)=charToughness(char)
 charOldAttitude(char)=charAttitude(char)
 charOldHappiness(char)=charHappiness(char)
End Function

;RESET NEW VALUES
Function ResetNewValues(char)
 charNewHealth(char)=charHealth(char)
 charNewPopularity(char)=charPopularity(char)
 charNewStrength(char)=charStrength(char)
 charNewSkill(char)=charSkill(char)
 charNewAgility(char)=charAgility(char)
 charNewStamina(char)=charStamina(char)
 charNewToughness(char)=charToughness(char)
 charNewAttitude(char)=charAttitude(char)
 charNewHappiness(char)=charHappiness(char)
End Function

;CHECK ALTERED VALUES
Function CheckNewValues(char)
 If charNewHealth(char)>100 Then charNewHealth(char)=100
 If charNewHealth(char)<0 Then charNewHealth(char)=0
 If charNewPopularity(char)<30 Then charNewPopularity(char)=30
 If charNewPopularity(char)>99 Then charNewPopularity(char)=99
 If charNewStrength(char)<30 Then charNewStrength(char)=30
 If charNewStrength(char)>99 Then charNewStrength(char)=99
 If charNewSkill(char)<30 Then charNewSkill(char)=30
 If charNewSkill(char)>99 Then charNewSkill(char)=99
 If charNewAgility(char)<30 Then charNewAgility(char)=30
 If charNewAgility(char)>99 Then charNewAgility(char)=99 
 If charNewStamina(char)<30 Then charNewStamina(char)=30
 If charNewStamina(char)>99 Then charNewStamina(char)=99 
 If charNewToughness(char)<30 Then charNewToughness(char)=30
 If charNewToughness(char)>99 Then charNewToughness(char)=99
 If charNewAttitude(char)<30 Then charNewAttitude(char)=30
 If charNewAttitude(char)>99 Then charNewAttitude(char)=99
 If charNewHappiness(char)<30 Then charNewHappiness(char)=30
 If charNewHappiness(char)>99 Then charNewHappiness(char)=99 
End Function

;PURSUE NEW STATISTIC
Function PursueValue(current,target,limit)
 value=0
 ;climb level 1
 If current<target Then value=1
 If current>target Then value=-1
 ;climb level 2
 If current<target-10 Then value=Rnd(2,4)
 If current>target+10 Then value=-Rnd(2,4)
 ;climb level 3
 If current<target-20 Then value=Rnd(3,7)
 If current>target+20 Then value=-Rnd(3,7)
 ;climb level 4
 If current<target-40 Then value=Rnd(4,10)
 If current>target+40 Then value=-Rnd(4,10)
 ;limit values
 If limit>0
  If value>limit Then value=limit
  If value<-limit Then value=-limit
 EndIf
 Return value
End Function

;HIGHLIGHT STAT CHANGES
Function HighlightStats(char)
 x=rX#(400)-225 : y=rY#(25)
 If foc=1 Then HighlightStat(x-52,y+3,104,10)
 If foc=2 Then HighlightStat(x-92,y+14,91,15)
 If foc=3 Then HighlightStat(x-82,y+29,81,15)
 If foc=4 Then HighlightStat(x-59,y+44,58,15)
 If foc=5 Then HighlightStat(x-68,y+59,67,15)
 If foc=6 Then HighlightStat(x+18,y+14,73,15)
 If foc=7 Then HighlightStat(x,y+29,91,15)
 If foc=8 Then HighlightStat(x+16,y+44,75,15)
 If foc=9 Then HighlightStat(x+3,y+59,88,15)
End Function

;HIGHLIGHT FED STAT CHANGES
Function HighlightFedStats(char)
 x=rX#(400)-220 : y=rY#(27)
 If foc=2 Then HighlightStat(x-67,y+14,127,15)
 If foc=3 Then HighlightStat(x-67,y+29,127,15)
End Function

;HIGHLIGHT STAT CALL
Function HighlightStat(startX,startY,width,height)
 ;main highlight
 Color 250,130,60
 Rect startX,startY,width,height,0
 ;add border
 If screen=>50
  Color 0,0,0
  Rect startX-1,startY-1,width+2,height+2,0
  ;Rect startX+1,startY+1,width-2,height-2,0
 EndIf
End Function

;ALTER STATS PROCESS
Function AlterStats(char)
 ;health
 If foc=1 And keytim=0
  If charNewHealth(char)<charHealth(char)-20 Or charNewHealth(char)>charHealth(char)+20 Then range=5 Else range=1
  If screen=54 Then timmer=8 Else timmer=2
  If charHealth(char)<charNewHealth(char) Then charHealth(char)=charHealth(char)+range : keytim=timmer
  If charHealth(char)>charNewHealth(char) Then charHealth(char)=charHealth(char)-range : keytim=timmer
  If charHealth(char)=charNewHealth(char) And keytim=0 Then foc=foc+1 : keytim=10 : PlaySound sMenuSelect
 EndIf
 ;stats
 If foc=2 Then charPopularity(char)=AlterStat(charPopularity(char),charNewPopularity(char)) 
 If foc=3 Then charStrength(char)=AlterStat(charStrength(char),charNewStrength(char))
 If foc=4 Then charSkill(char)=AlterStat(charSkill(char),charNewSkill(char))
 If foc=5 Then charAgility(char)=AlterStat(charAgility(char),charNewAgility(char))
 If foc=6 Then charStamina(char)=AlterStat(charStamina(char),charNewStamina(char))
 If foc=7 Then charToughness(char)=AlterStat(charToughness(char),charNewToughness(char))
 If foc=8 Then charAttitude(char)=AlterStat(charAttitude(char),charNewAttitude(char))
 If foc=9 Then charHappiness(char)=AlterStat(charHappiness(char),charNewHappiness(char))
 ;clock changes
 If foc=>1 And foc=<9 Then charUpdated(char)=1
End Function

;ALTER PROMOTION STATS
Function AlterFedStats(cyc)
 If foc=1 And keytim=0 Then foc=foc+1 : keytim=20 : PlaySound sMenuSelect
 If foc=2 Then fedPopularity(cyc)=AlterStat(fedPopularity(cyc),fedNewPopularity(cyc)) 
 If foc=3 Then fedReputation(cyc)=AlterStat(fedReputation(cyc),fedNewReputation(cyc)) 
 If foc>3 Then foc=10
End Function

;ALTER SPECIFIC STAT
Function AlterStat(value,newStat)
 If keytim=0
  If newStat<value-20 Or newStat>value+20 Then range=5 Else range=1
  If screen=54 Then timmer=16 Else timmer=4
  If value<newStat Then value=value+range : PlaySound sMenuBrowse : keytim=timmer
  If value>newStat Then value=value-range : PlaySound sMenuBrowse : keytim=timmer
  If value=newStat And keytim=0 Then foc=foc+1 : keytim=10 : PlaySound sMenuSelect
 EndIf 
 Return value
End Function

;CHECK FED LIMITS
Function CheckFedLimits(cyc)
 ;standard limits
 If fedPopularity(cyc)<30 Then fedPopularity(cyc)=30 
 If fedPopularity(cyc)>99 Then fedPopularity(cyc)=99
 If fedReputation(cyc)<30 Then fedReputation(cyc)=30
 If fedReputation(cyc)>99 Then fedReputation(cyc)=99
 ;new limits
 If fedNewPopularity(cyc)>99 Then fedNewPopularity(cyc)=99
 If fedNewPopularity(cyc)<1 Then fedNewPopularity(cyc)=1
 If fedNewReputation(cyc)>99 Then fedNewReputation(cyc)=99
 If fedNewReputation(cyc)<1 Then fedNewReputation(cyc)=1
End Function

;FIND UNDERDOG (out of 2 players)
Function FindUnderdog(cyc,v)
 underdog=0
 ;mismatched reputation
 If charPopularity(pChar(cyc))=<charPopularity(pChar(v))-10 Then underdog=cyc
 If charPopularity(pChar(v))=<charPopularity(pChar(cyc))-10 Then underdog=v 
 ;mismatched ability
 If charPopularity(pChar(cyc))=<charPopularity(pChar(v))
  If AverageStats(pChar(cyc))=<AverageStats(pChar(v))-(AverageStats(pChar(v))/6) Then underdog=cyc
 EndIf
 If charPopularity(pChar(v))=<charPopularity(pChar(cyc))
  If AverageStats(pChar(v))=<AverageStats(pChar(cyc))-(AverageStats(pChar(cyc))/6) Then underdog=v
 EndIf 
 ;mismatched health
 If charOldHealth(pChar(cyc))<charOldHealth(pChar(v))/2 And charOldHealth(pChar(v))=>75 Then underdog=cyc  
 If charOldHealth(pChar(v))<charOldHealth(pChar(cyc))/2 And charOldHealth(pChar(cyc))=>75 Then underdog=v
 If FindInjury(cyc)>0 And FindInjury(v)=0 Then underdog=cyc 
 If FindInjury(v)>0 And FindInjury(cyc)=0 Then underdog=v 
 ;mismatched experience
 If charMatches(pChar(cyc),charFed(pChar(cyc)))=<1 And charMatches(pChar(v),charFed(pChar(v)))>1 Then underdog=cyc 
 If charMatches(pChar(v),charFed(pChar(v)))=<1 And charMatches(pChar(cyc),charFed(pChar(cyc)))>1 Then underdog=v 
 ;handicap match
 If matchTeams>0 And no_wrestlers=3 
  If cyc=1 Then underdog=cyc
  If v=1 Then underdog=v
 EndIf
 Return underdog
End Function

;FIND SQUASH RESULT
Function FindSquash()
 squash=0
 If matchWinner>0 And matchLoser>0 And pTeam(matchLoser)<>pTeam(matchWinner) And matchWinStyle=>1 And matchWinStyle=<4 And FindInjury(matchWinner)=0 And InjuryStatus(pChar(matchLoser))=0
  ;substantial health difference
  If no_wrestlers=2 And matchMins=<optLength*2 And pHealth(matchWinner)>pHealth(matchLoser)+(2500*optLength) Then squash=1
  ;quick finish
  If matchMins=<optLength-1 Then squash=1
  ;dispropotionate scores
  If matchType=3
   If matchTeams=<0 And no_wrestlers=2 And pFalls(matchWinner)>pFalls(matchLoser)+2 Then squash=1 
   If matchTeams>0 And teamFalls(pTeam(matchWinner))>teamFalls(pTeam(matchLoser))+2 Then squash=1
  EndIf
 EndIf
 ;handicap override
 If matchTeams>0 And no_wrestlers=3 And matchWinner>1 Then squash=0
 Return squash
End Function

;FIND TITLE CHANGES
Function TitleChanges(promotion)
 value=0
 If fedChampTag(promotion,1)<>fedOldChampTag(promotion,1) And fedChampTag(promotion,1)<>fedOldChampTag(promotion,2) Then value=3
 If fedChampTag(promotion,2)<>fedOldChampTag(promotion,1) And fedChampTag(promotion,2)<>fedOldChampTag(promotion,2) Then value=3
 If fedChampInter(promotion)<>fedOldChampInter(promotion) Then value=2 
 If fedChampWorld(promotion)<>fedOldChampWorld(promotion) Then value=1
;new belts
If fedChampWomens(promotion)<>fedOldChampWomens(promotion) Then value=5
If fedChampUS(promotion)<>fedOldChampUS(promotion) Then value=6
If fedChampTV(promotion)<>fedOldChampTV(promotion) Then value=7
;end 
 Return value
End Function

;IMPORTANT AFTERMATH EVENT ON FILE?
Function ImportantEvent(char)
 important=0
 If charEvent(char)=>9 And charEvent(char)=<13 Then important=1 ;significant victories
 If charEvent(char)=29 Then important=1 ;interpromotional loss
 If charEvent(char)=>46 And charEvent(char)=<53 Then important=1 ;injuries
 Return important
End Function

;APPLY INJURY
Function ApplyInjury(char,injury,severity) ;0-5=limbs, 99=paralysis
 ;generic injuries
 If injury=0
  charInjured(char,0)=Rnd(1,5) : charNewHealth(char)=0
  If severity=2 Then charInjured(char,0)=Rnd(4,10)
  charNewStrength(char)=charNewStrength(char)+(PursueValue(charStrength(char),30,0)*severity)
  charNewSkill(char)=charNewSkill(char)+(PursueValue(charSkill(char),30,0)*severity)
  charNewAgility(char)=charNewAgility(char)+(PursueValue(charAgility(char),30,0)*severity)
  charNewStamina(char)=charNewStamina(char)+(PursueValue(charStamina(char),30,0)*severity)
  charNewToughness(char)=charNewToughness(char)+(PursueValue(charToughness(char),30,0)*severity)
  charNewHappiness(char)=charNewHappiness(char)+(PursueValue(charHappiness(char),30,0)*severity)
 EndIf
 ;hand injury
 If injury=1
  charInjured(char,injury)=Rnd(1,5) : charNewHealth(char)=0
  If severity=2 Then charInjured(char,injury)=Rnd(4,10)
  charNewStrength(char)=charNewStrength(char)+(PursueValue(charStrength(char),30,1)*severity)
  charNewSkill(char)=charNewSkill(char)+(PursueValue(charSkill(char),30,0)*severity)
  charNewToughness(char)=charNewToughness(char)+(PursueValue(charToughness(char),30,1)*severity)
  charNewHappiness(char)=charNewHappiness(char)+(PursueValue(charHappiness(char),30,0)*severity)
 EndIf
 ;arm injury
 If injury=2
  charInjured(char,injury)=Rnd(1,5) : charNewHealth(char)=0
  If severity=2 Then charInjured(char,injury)=Rnd(4,10)
  charNewStrength(char)=charNewStrength(char)+(PursueValue(charStrength(char),30,0)*severity)
  charNewSkill(char)=charNewSkill(char)+(PursueValue(charSkill(char),30,0)*severity)
  charNewToughness(char)=charNewToughness(char)+(PursueValue(charToughness(char),30,1)*severity)
  charNewHappiness(char)=charNewHappiness(char)+(PursueValue(charHappiness(char),30,0)*severity)
 EndIf
 ;rib injury
 If injury=3
  charInjured(char,3)=Rnd(1,5) : charNewHealth(char)=0
  If severity=2 Then charInjured(char,3)=Rnd(4,10)
  charNewStrength(char)=charNewStrength(char)+(PursueValue(charStrength(char),30,0)*severity)
  charNewSkill(char)=charNewSkill(char)+(PursueValue(charSkill(char),30,1)*severity)
  charNewAgility(char)=charNewAgility(char)+(PursueValue(charAgility(char),30,1)*severity)
  charNewStamina(char)=charNewStamina(char)+(PursueValue(charStamina(char),30,0)*severity)
  charNewToughness(char)=charNewToughness(char)+(PursueValue(charToughness(char),30,0)*severity)
  charNewHappiness(char)=charNewHappiness(char)+(PursueValue(charHappiness(char),30,0)*severity)
 EndIf
 ;leg injury
 If injury=4
  charInjured(char,4)=Rnd(1,5) : charNewHealth(char)=0
  If severity=2 Then charInjured(char,4)=Rnd(4,10)
  charNewStrength(char)=charNewStrength(char)+(PursueValue(charStrength(char),30,1)*severity)
  charNewSkill(char)=charNewSkill(char)+(PursueValue(charSkill(char),30,0)*severity)
  charNewAgility(char)=charNewAgility(char)+(PursueValue(charAgility(char),30,0)*severity)
  charNewStamina(char)=charNewStamina(char)+(PursueValue(charStamina(char),30,1)*severity)
  charNewToughness(char)=charNewToughness(char)+(PursueValue(charToughness(char),30,1)*severity)
  charNewHappiness(char)=charNewHappiness(char)+(PursueValue(charHappiness(char),30,0)*severity)
 EndIf
 ;head injury
 If injury=5
  charInjured(char,5)=Rnd(1,5) : charNewHealth(char)=0
  If severity=2 Then charInjured(char,5)=Rnd(4,10)
  charNewStrength(char)=charNewStrength(char)+(PursueValue(charStrength(char),30,1)*severity)
  charNewSkill(char)=charNewSkill(char)+(PursueValue(charSkill(char),30,0)*severity)
  charNewAgility(char)=charNewAgility(char)+(PursueValue(charAgility(char),30,0)*severity)
  charNewStamina(char)=charNewStamina(char)+(PursueValue(charStamina(char),30,0)*severity)
  charNewToughness(char)=charNewToughness(char)+(PursueValue(charToughness(char),30,0)*severity)
  charNewHappiness(char)=charNewHappiness(char)+(PursueValue(charHappiness(char),30,0)*severity)
 EndIf
 ;paralysis!
 If injury=99
  charInjured(char,5)=Rnd(5,15) : charNewHealth(char)=0
  charNewStrength(char)=30 : charNewSkill(char)=30 : charNewAgility(char)=30
  charNewStamina(char)=30 : charNewToughness(char)=30 : charNewHappiness(char)=30
 EndIf
 ;sadden manager
 If charManager(char)>0
  v=charManager(char)
  charHappiness(v)=charHappiness(v)+(PursueValue(charHappiness(v),30,0)*severity)
 EndIf
End Function

;CREATE ANGLES BASED ON MATCH RESULT
Function BookAngles()
 date=NextDate()
 ;annoyed at victory
 If pChar(matchWinner)=gamChar And matchLoser>0 And pTeam(matchLoser)<>pTeam(matchWinner)
  randy=Rnd(0,5)
  If randy=0 Or (randy=<1 And FindInjury(matchLoser)>0) Then ChangeRelationship(gamChar,pChar(matchLoser),-1)
  If randy=1 And pHealth(matchWinner)=<1000*optLength And pHealth(matchLoser)=<1000*optLength ;close call
   gamOpponent(date)=pChar(matchLoser) : gamPromo(date)=39 
   ChangeRelationship(gamChar,pChar(matchLoser),-1)
  EndIf
  If randy=>2 And randy=<3 And charOldHealth(pChar(matchWinner))>PercentOf#(charOldHealth(pChar(matchLoser)),125) ;fitness dispute
   gamOpponent(date)=pChar(matchLoser) : gamPromo(date)=67
   ChangeRelationship(gamChar,pChar(matchLoser),-1)
  EndIf
  If randy=4 And matchWeapon>0 And matchRules=<1 ;weapon claim
   gamOpponent(date)=pChar(matchLoser) : gamPromo(date)=74
   ChangeRelationship(gamChar,pChar(matchLoser),-1)
   charWeapon(pChar(matchLoser))=matchWeapon
  EndIf
  If randy=5 Or (randy=<1 And FindInjury(matchLoser)>0) ;friend avenges 
   avenger=0 : its=0
   Repeat 
    avenger=fedRoster(charFed(gamChar),Rnd(1,fedSize(charFed(gamChar))))
    its=its+1
    If its>100 Then avenger=0
   Until avenger=0 Or (avenger<>gamChar And charRelationship(avenger,pChar(matchLoser))>0)
   If avenger>0
    gamOpponent(date)=avenger : gamPromo(date)=24
    gamPromoVariable(date)=pChar(matchLoser)
   EndIf
  EndIf
 EndIf
 ;time out leads to rematch
 If pRole(matchPlayer)=0 And matchMins=matchTimeLim And matchTimeLim=>10 And matchWinner=0
  randy=Rnd(0,1)
  If randy=0 Then gamOpponent(date)=gamOpponent(gamDate) : gamPromo(date)=79 
 EndIf
 ;world title win leads to feud
 If fedOldChampWorld(charFed(gamChar))<>gamChar And fedChampWorld(charFed(gamChar))=gamChar
  randy=Rnd(0,2)
  If randy=<1 Then ChangeRelationship(gamChar,fedOldChampWorld(charFed(gamChar)),-1)
  If randy=1 Then gamOpponent(date)=fedOldChampWorld(charFed(gamChar)) : gamPromo(date)=46
 EndIf
 ;world title loss leads to rematch
 If fedOldChampWorld(charFed(gamChar))=gamChar And fedChampWorld(charFed(gamChar))<>gamChar
  randy=Rnd(0,2)
  If randy=<1 Then ChangeRelationship(gamChar,fedChampWorld(charFed(gamChar)),-1)
  If randy=1 Then gamOpponent(date)=fedChampWorld(charFed(gamChar)) : gamPromo(date)=45
 EndIf 
 ;inter title win leads to feud
 If fedOldChampInter(charFed(gamChar))<>gamChar And fedChampInter(charFed(gamChar))=gamChar
  randy=Rnd(0,2)
  If randy=<1 Then ChangeRelationship(gamChar,fedOldChampInter(charFed(gamChar)),-1)
  If randy=1 Then gamOpponent(date)=fedOldChampInter(charFed(gamChar)) : gamPromo(date)=46
 EndIf
 ;inter title loss leads to rematch
 If fedOldChampInter(charFed(gamChar))=gamChar And fedChampInter(charFed(gamChar))<>gamChar
  randy=Rnd(0,2)
  If randy=<1 Then ChangeRelationship(gamChar,fedChampInter(charFed(gamChar)),-1)
  If randy=1 Then gamOpponent(date)=fedChampInter(charFed(gamChar)) : gamPromo(date)=45
 EndIf 
;new belts
;Womens title win leads to feud
 If fedOldChampWomens(charFed(gamChar))<>gamChar And fedChampWomens(charFed(gamChar))=gamChar
  randy=Rnd(0,2)
  If randy=<1 Then ChangeRelationship(gamChar,fedOldChampWomens(charFed(gamChar)),-1)
  If randy=1 Then gamOpponent(date)=fedOldChampWomens(charFed(gamChar)) : gamPromo(date)=46
 EndIf
 ;Womens title loss leads to rematch
 If fedOldChampWomens(charFed(gamChar))=gamChar And fedChampWomens(charFed(gamChar))<>gamChar
  randy=Rnd(0,2)
  If randy=<1 Then ChangeRelationship(gamChar,fedChampWomens(charFed(gamChar)),-1)
  If randy=1 Then gamOpponent(date)=fedChampWomens(charFed(gamChar)) : gamPromo(date)=45
 EndIf
;US title win leads to feud
 If fedOldChampUS(charFed(gamChar))<>gamChar And fedChampUS(charFed(gamChar))=gamChar
  randy=Rnd(0,2)
  If randy=<1 Then ChangeRelationship(gamChar,fedOldChampUS(charFed(gamChar)),-1)
  If randy=1 Then gamOpponent(date)=fedOldChampUS(charFed(gamChar)) : gamPromo(date)=46
 EndIf
 ;US title loss leads to rematch
 If fedOldChampUS(charFed(gamChar))=gamChar And fedChampUS(charFed(gamChar))<>gamChar
  randy=Rnd(0,2)
  If randy=<1 Then ChangeRelationship(gamChar,fedChampUS(charFed(gamChar)),-1)
  If randy=1 Then gamOpponent(date)=fedChampUS(charFed(gamChar)) : gamPromo(date)=45
 EndIf
;TV title win leads to feud
 If fedOldChampTV(charFed(gamChar))<>gamChar And fedChampTV(charFed(gamChar))=gamChar
  randy=Rnd(0,2)
  If randy=<1 Then ChangeRelationship(gamChar,fedOldChampTV(charFed(gamChar)),-1)
  If randy=1 Then gamOpponent(date)=fedOldChampTV(charFed(gamChar)) : gamPromo(date)=46
 EndIf
 ;TV title loss leads to rematch
 If fedOldChampTV(charFed(gamChar))=gamChar And fedChampTV(charFed(gamChar))<>gamChar
  randy=Rnd(0,2)
  If randy=<1 Then ChangeRelationship(gamChar,fedChampTV(charFed(gamChar)),-1)
  If randy=1 Then gamOpponent(date)=fedChampTV(charFed(gamChar)) : gamPromo(date)=45
 EndIf
;end


 ;novelty ref leads to feud
 ref=pChar(no_wrestlers+1)
 If pRole(matchPlayer)=0 And no_refs>0 And charRole(ref)<>3 And ref<>fedBooker(7) And TitleHolder(ref,0)=0 And matchWinStyle>0 And matchWinner>0 And pTeam(matchWinner)<>pTeam(matchPlayer)
  randy=Rnd(0,4) 
  If randy=<1 And (charRelationship(ref,gamChar)<0 Or charRelationship(ref,pChar(matchWinner))>0) ;victim of corruption
   gamMatch(date)=12 : gamOpponent(date)=ref
   gamPromo(date)=90 : gamPromoVariable(date)=pChar(matchWinner)
   ChangeRelationship(gamChar,ref,-1)
  EndIf
  If randy=2
   gamOpponent(date)=ref : gamPromo(date)=75 ;victim of incompetence   
   ChangeRelationship(gamChar,ref,-1)
  EndIf
 EndIf
 ;your refereeing leads to feud
 If pRole(matchPlayer)=1 And matchWinner>0
  For cyc=1 To no_wrestlers
   If pTeam(cyc)<>pTeam(matchWinner)
    randy=Rnd(0,3)
    If randy=<1 Then ChangeRelationship(gamChar,pChar(cyc),-1) 
    If randy=1 Then gamOpponent(date)=pChar(cyc) : gamPromo(date)=14
   EndIf
  Next
 EndIf 
 ;team issues
 If matchTeams>0 And pRole(matchPlayer)=0 And charPartner(gamChar)>0 And FindCharacter(charPartner(gamChar))>0 And TitleHolder(gamChar,3)=0
  ;loss threatens to break team
  randy=Rnd(0,(charHappiness(charPartner(gamChar))-25)/2)
  If randy=<1 And matchWinner>0 And pTeam(matchWinner)<>pTeam(matchPlayer) And negTopic<>72
   gamOpponent(date)=charPartner(gamChar) : gamPromo(date)=55
   ChangeRelationship(gamChar,charPartner(gamChar),-1)
  EndIf
  ;win threatens to break team
  If randy=2 And matchWinner=matchPlayer
   gamOpponent(date)=charPartner(gamChar) : gamPromo(date)=70
   ChangeRelationship(gamChar,charPartner(gamChar),-1)
  EndIf
  ;odd behaviour threaten to break team
  If randy=3 
   gamOpponent(date)=charPartner(gamChar) : gamPromo(date)=88
   ChangeRelationship(gamChar,charPartner(gamChar),-1)
  EndIf
 EndIf
 ;team mate abuse
 If matchTeams>0 And pRole(matchPlayer)=0 And TitleHolder(gamChar,3)=0
  For v=1 To no_wrestlers
   randy=Rnd(0,100)
   If pAbused(v)>0 And randy=<pAbused(v) And pTeam(v)=pTeam(matchPlayer) And pChar(v)<>gamChar
    gamOpponent(date)=pChar(v) : gamPromo(date)=100
    ChangeRelationship(gamChar,pChar(v),-1)
   EndIf
  Next
 EndIf
 ;rumble/tournament contender
 If (matchTeams=<0 And no_wrestlers>4) Or (screenAgenda=11 And cupFoc(cupSlot)=1)
  If pChar(matchWinner)=gamChar And TitleHolder(gamChar,0)=0
   If matchTeams>0
    gamOpponent(date)=fedChampTag(charFed(gamChar),1)
    gamMatch(date)=12 : gamPromo(date)=0
   Else
    gamOpponent(date)=fedChampInter(charFed(gamChar))
    If charTitles(gamChar,charFed(gamChar),2)>0 Or charTitles(gamChar,charFed(gamChar),1)>0 Then gamOpponent(date)=fedChampWorld(charFed(gamChar)) 
    gamMatch(date)=2 : gamPromo(date)=0
   EndIf
  EndIf 
 EndIf
 ;your interference (management) leads to feud
 If pRole(matchPlayer)=2 And matchWinner=pClient(matchPlayer) And matchLoser>0
  randy=Rnd(0,3)
  If randy=<1 Then ChangeRelationship(gamChar,pChar(matchLoser),-1)  
  If randy=1 Then gamOpponent(date)=pChar(matchLoser) : gamPromo(date)=1
 EndIf
 ;interference leads to feud
 If pRole(matchPlayer)=0 And matchIntruder>0
  If pTeam(matchWinner)=pTeam(matchPlayer) Then randy=Rnd(0,4) Else randy=Rnd(0,2)
  If randy=<2 Then ChangeRelationship(gamChar,pChar(matchIntruder),-1)
  If randy=1 Then gamOpponent(date)=pChar(matchIntruder) : gamPromo(date)=2
  If randy=2 And matchWinner>0 And pTeam(matchWinner)<>pTeam(matchPlayer)
   gamMatch(date)=12 : gamOpponent(date)=pChar(matchIntruder)
   gamPromo(date)=90 : gamPromoVariable(date)=pChar(matchWinner) 
  EndIf
 EndIf
 ;positive intruder becomes friend
 If pRole(matchPlayer)=0 And matchIntruder<0
  char=pChar(MakePositive#(matchIntruder))
  If pTeam(MakePositive#(matchIntruder))=pTeam(matchPlayer) Then ChangeRelationship(gamChar,char,1)
  ;resent interference
  chance=charAttitude(gamChar)
  If matchWinner>0 And pChar(matchWinner)<>gamChar Then chance=chance/2
  randy=Rnd(0,chance)
  If randy=<5 And char<>charPartner(gamChar) And char<>charManager(gamChar)
   gamOpponent(date)=char : gamPromo(date)=89
   ChangeRelationship(gamChar,char,-1)
  EndIf
 EndIf
 ;betrayal leads to feud
 If pRole(matchPlayer)=0 And matchBetrayal>0
  randy=Rnd(0,1)
  If randy=0 And matchWinner>0 And matchWinner<>matchPlayer
   gamMatch(date)=12 : gamOpponent(date)=pChar(matchBetrayal)
   gamPromo(date)=90 : gamPromoVariable(date)=pChar(matchWinner)
   If pChar(matchBetrayal)=charPartner(gamChar) Then FormTeam(pChar(matchWinner),pChar(matchBetrayal))
   If pChar(matchBetrayal)=charManager(gamChar) Then charManager(pChar(matchWinner))=pChar(matchBetrayal)
   ChangeRelationship(pChar(matchBetrayal),pChar(matchWinner),1) 
  Else
   gamOpponent(date)=pChar(matchBetrayal) : gamPromo(date)=73
  EndIf
  ChangeRelationship(gamChar,pChar(matchBetrayal),-1)   
 EndIf
 ;tag partners become friends
 If matchTeams>0
  For cyc=1 To no_wrestlers
   If matchWinner>0 And pTeam(cyc)=pTeam(matchWinner)
    For v=1 To no_wrestlers
     If cyc<>v And pTeam(cyc)=pTeam(v) And charRelationship(pChar(cyc),pChar(v))=0 Then ChangeRelationship(pChar(cyc),pChar(v),1)
    Next
   EndIf
  Next
 EndIf
 ;prevent promo going ahead
 reset=0
 If date=0 Or date=>gamDate+4 Then reset=1 ;bad date
 ;If charFed(gamChar)=7 Then reset=1 ;not in school
 If InjuryStatus(gamOpponent(date))>0 Then reset=1 ;opponent is injured
 If gamPromo(date)=gamPromo(gamDate) Or gamPromo(date)=gamPromo(gamDate-1) Then reset=1 ;used recently
 If reset=1 Then gamOpponent(date)=0 : gamMatch(date)=0 : gamPromo(date)=0
End Function