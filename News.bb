;//////////////////////////////////////////////////////////////////////////////
;------------------------- WRESTLING MPIRE 2008: NEWS REPORTS -----------------
;//////////////////////////////////////////////////////////////////////////////

;------------------------------------------------------------------------
;///////////////////// 22. INTRODUCE NEW WEEK ///////////////////////////
;------------------------------------------------------------------------
Function IntroduceDate()
;advance date
AdvanceDate()
;frame rating
timer=CreateTimer(30)
;MAIN LOOP
x1=-100 : x2=900
go=0 : gotim=0 : keytim=0
While go=0

 Cls
 frames=WaitTimer(timer)
 For framer=1 To frames 
	
    ;PORTAL
    gotim=gotim+1
	If gotim>20
	 If KeyDown(1) Or KeyDown(28) Or ButtonPressed() Or MouseDown(1) Then go=1
	EndIf
	If gotim>500 Then go=1 
	;swing in boxes
	x1=x1+20
	If x1>305 Then x1=305
	x2=x2-20
	If x2<495 Then x2=495
	
 UpdateWorld
 Next
 RenderWorld 1

 ;DISPLAY
 DrawImage gBackground,rX#(400),rY#(300)
 DrawImage gLogo(1),rX#(400),rY#(250)
 DrawImage gMDickie,rX#(400),rY#(530)
 ;date boxes
 If x1<305 And x2>495
  DrawOption(-1,rX#(x1),rY#(425),"New Week","")
  DrawOption(-1,rX#(x2),rY#(425),textWeek$(GetWeek(gamDate))+" of "+textMonth$(GetMonth(gamDate)),"")
 Else
  DrawOption(-1,rX#(400),rY#(425),"New Week",textWeek$(GetWeek(gamDate))+" of "+textMonth$(GetMonth(gamDate)))
 EndIf
 ;cursor
 DrawImage gCursor,MouseX(),MouseY()

 Flip
 ;screenshot (F12)
 If KeyHit(88) Then Screenshot()

Wend
;proceed to news
FreeTimer timer
SaveUniverse()
SaveProgress(slot)
SaveWorld(slot)
SaveChars(slot) 
fed=1 : screen=23
End Function

;------------------------------------------------------------------
;///////////////////// ADVANCE DATE PROCESS ///////////////////////
;------------------------------------------------------------------
Function AdvanceDate()
 ;inspire new destinies
 SeedRnd(MilliSecs())
 ;advance date
 gamDate=gamDate+1
 If gamDate>48 
  gamDate=1 : gamYear=gamYear+1
  For char=1 To no_chars
   If charFed(char)=<8 Then charAge(char)=charAge(char)+1
  Next
  ResetSchedule(1)
 EndIf
 ;expire agreements
 For count=1 To 50
  If count<>21 Then gamAgreement(count)=gamAgreement(count)-1
  If gamAgreement(count)<0 Then gamAgreement(count)=0
 Next
 ;reset character handles
 For char=1 To no_chars
  For count=1 To 3
   gamNegotiated(char,count)=0
  Next
  charOpponent(char)=0
  charFought(char)=0
  charBracket(char)=0
  ResetOldValues(char)
 Next
 ;promotion title status
 For cyc=1 To 6
  fedOldChampWorld(cyc)=fedChampWorld(cyc)
  fedOldChampInter(cyc)=fedChampInter(cyc)
;new belts
fedOldChampWomens(cyc)=fedChampWomens(cyc)
fedOldChampUS(cyc)=fedChampUS(cyc)
fedOldChampTV(cyc)=fedChampTV(cyc)

  For count=1 To 2
   fedOldChampTag(cyc,count)=fedChampTag(cyc,count)
  Next
 Next
 ;CHARACTER DEVELOPMENTS
 For char=1 To no_chars
  ;weight changes
  charWeightChange(char)=charWeightChange(char)+Rnd(-1,1)
  If char=gamChar And gamAgreement(13)>0 And charWeightChange(char)<1 Then charWeightChange(char)=1
  If char=gamChar And gamAgreement(14)>0 And charWeightChange(char)>-1 Then charWeightChange(char)=-1
  charWeight(char)=charWeight(char)+charWeightChange(char)
  charWeightChange(char)=0
  ;health status
  For count=0 To 5
   charOldInjured(char,count)=charInjured(char,count)
   charInjured(char,count)=charInjured(char,count)-1
   If charInjured(char,count)<0 Then charInjured(char,count)=0
  Next
  If charManager(char)>0 Then healer=PercentOf#(charStamina(char),125) Else healer=charStamina(char)
  charHealth(char)=charHealth(char)+Rnd(healer/2,healer)
  If charHealth(char)>100 Then charHealth(char)=100
  If InjuryStatus(char)>0
   charHealth(char)=0
   If char=gamChar And charManager(gamChar)>0 Then charHappiness(charManager(gamChar))=charHappiness(charManager(gamChar))-Rnd(0,1)
  EndIf
  ;contract status
  charExperience(char,charFed(char))=charExperience(char,charFed(char))+1
  charOldVacant(char)=charVacant(char)
  charVacant(char)=charVacant(char)-1
  If charVacant(char)<0 Then charVacant(char)=0
  charContract(char)=charContract(char)-1
  If charContract(char)<0 Then charContract(char)=0
  ;assign CPU matches
  randy=Rnd(0,8) : workRate=2
  If TitleHolder(char,0)>0 Then workRate=workRate-1
  If TitleHolder(char,3) Then workRate=workRate-1
  If randy=<workRate And FightAvailable(char)
   charOpponent(char)=AssignOpponent(char,gamDate,0)
  EndIf
  ;tournament fixtures
  If cupSize(cupSlot)>0
   started=0
   If gamSchedule(gamDate)=3 Then started=1
   For b=1 To 32
    If cupResult(cupSlot,b)>0 Then started=1
   Next
   If started=1 And charFought(char)=0
    For b=1 To 32
     If cupBracket(cupSlot,b,1)>0 And cupBracket(cupSlot,b,2)>0 And cupBracket(cupSlot,b,1)<>gamChar And cupBracket(cupSlot,b,2)<>gamChar And cupResult(cupSlot,b)=0
      If cupBracket(cupSlot,b,1)=char Then charOpponent(char)=cupBracket(cupSlot,b,2) : charBracket(char)=b
      If cupBracket(cupSlot,b,2)=char Then charOpponent(char)=cupBracket(cupSlot,b,1) : charBracket(char)=b
     EndIf
    Next
   EndIf
  EndIf
  ;execute CPU matches
  If charOpponent(char)>0 And charFought(char)=0
   v=charOpponent(char) : charOpponent(v)=char
   charFought(char)=1 : charMatches(char,charFed(char))=charMatches(char,charFed(char))+1
   charFought(v)=1 : charMatches(v,charFed(v))=charMatches(v,charFed(v))+1
   If char=fedChampTag(charFed(char),1) Then charFought(fedChampTag(charFed(char),2))=1
   If char=fedChampTag(charFed(char),2) Then charFought(fedChampTag(charFed(char),1))=1
   If v=fedChampTag(charFed(v),1) Then charFought(fedChampTag(charFed(v),2))=1
   If v=fedChampTag(charFed(v),2) Then charFought(fedChampTag(charFed(v),1))=1
   winner=Rnd(-2,2)
   If (winner=1 Or winner=-1) And TitleHolder(v,0)>0 Then winner=2
   If (winner=2 Or winner=-2) And TitleHolder(char,0)>0 Then winner=1
   If winner=<0
    If PercentOf#(AverageStats(char),charHealth(char))=>PercentOf#(AverageStats(v),charHealth(v)) Then winner=1 Else winner=2
   EndIf 
   If winner=1 Then WinEffect(char,v)
   If winner=2 Then WinEffect(v,char)
   If charBracket(char)>0
    cupResult(cupSlot,charBracket(char))=winner
    If charBracket(char)>1
     cupBracket(cupSlot,cupTargetBracket(charBracket(char)),cupTargetSlot(charBracket(char)))=cupBracket(cupSlot,charBracket(char),cupResult(cupSlot,charBracket(char))) 
    EndIf
   EndIf
  EndIf
  ;stat fluctuations
  If ((InjuryStatus(char)=0 And charHealth(char)>50) Or charOpponent(char)>0) And char<>gamChar
   randy=Rnd(0,10) : level=Rnd(1,6)
   If randy=0 Then charStrength(char)=charStrength(char)+PursueValue(charStrength(char),statLevel(level),1)
   randy=Rnd(0,10) : level=Rnd(1,6)
   If randy=0 Then charSkill(char)=charSkill(char)+PursueValue(charSkill(char),statLevel(level),1)
   randy=Rnd(0,10) : level=Rnd(1,6)
   If randy=0 Then charAgility(char)=charAgility(char)+PursueValue(charAgility(char),statLevel(level),1)
   randy=Rnd(0,10) : level=Rnd(1,6)
   If randy=0 Then charStamina(char)=charStamina(char)+PursueValue(charStamina(char),statLevel(level),1)
   randy=Rnd(0,10) : level=Rnd(1,6)
   If randy=0 Then charToughness(char)=charToughness(char)+PursueValue(charToughness(char),statLevel(level),1)
   ;limit changes if old
   If charPeaked(char)>0
    If charStrength(char)>charOldStrength(char) Then charStrength(char)=charOldStrength(char)
    If charSkill(char)>charOldSkill(char) Then charSkill(char)=charOldSkill(char)
    If charAgility(char)>charOldAgility(char) Then charAgility(char)=charOldAgility(char)
    If charStamina(char)>charOldStamina(char) Then charStamina(char)=charOldStamina(char)
    If charToughness(char)>charOldToughness(char) Then charToughness(char)=charOldToughness(char) 
   EndIf
   ;affect weight
   ;FindWeightChanges(char)
  EndIf
  ;personality fluctuations
  If char<>gamChar And char<>charPartner(gamChar) And char<>charManager(gamChar)
   randy=Rnd(0,10) : level=Rnd(1,6)
   If randy=0 Then charAttitude(char)=charAttitude(char)+PursueValue(charAttitude(char),statLevel(level),1)
   randy=Rnd(0,10) : level=Rnd(1,6)
   If randy=0 Then charHappiness(char)=charHappiness(char)+PursueValue(charHappiness(char),statLevel(level),1)
  EndIf
  ;simulate training tiredness 
  If charHealth(char)>50 And charRole(char)=1 And charOpponent(char)=0 And TournamentStatus(char)=0
   If char<>gamChar ;And char<>charPartner(gamChar) And char<>gamOpponent(gamDate) And char<>charPartner(gamOpponent(gamDate))
    randy=Rnd(0,2)
    If randy=0 Then charHealth(char)=charHealth(char)-Rnd(0,25)
   EndIf
  EndIf
  ;develop relationships
  charRelationship(char,char)=0
  For v=1 To no_chars
   If charRelationship(char,v)>0 Then charRelationship(char,v)=charRelationship(char,v)+1
   If charRelationship(char,v)<0 Then charRelationship(char,v)=charRelationship(char,v)-1
   randy=Rnd(0,100-MakePositive#(charRelationship(char,v)))
   If randy=0 And (charRelationship(char,v)>4 Or charRelationship(char,v)<-4) Then charRelationship(char,v)=0
   If charRealRelationship(char,v)>0 Then charRealRelationship(char,v)=charRelationship(char,v)+1
   If charRealRelationship(char,v)<0 Then charRealRelationship(char,v)=charRelationship(char,v)-1
   randy=Rnd(0,100-MakePositive#(charRealRelationship(char,v)))
   If randy=0 And (charRealRelationship(char,v)>4 Or charRealRelationship(char,v)<-4) Then charRealRelationship(char,v)=0
   If charRelationship(char,v)>0 And charTeamHistory(char,v)<1 Then charTeamHistory(char,v)=1
  Next 
  If charPartner(char)>0 Then charTeamHistory(char,charPartner(char))=2 : charTeamHistory(charPartner(char),char)=2
  If charManager(char)>0 Then charTeamHistory(char,charManager(char))=3 : charTeamHistory(charManager(char),char)=3 
  ;team name consistency
  If char=charPartner(gamChar)
   If Right$(charTeamName$(gamChar),7)="'s Team" 
    charTeamName$(char)=charName$(char)+"'s Team"
   Else
    charTeamName$(char)=charTeamName$(gamChar)
   EndIf
  Else 
   If char<>gamChar And charTeamName$(char)=charTeamName$(gamChar) Then charTeamName$(char)=charName$(char)+"'s Team"
  EndIf
 Next
 ;check changes
 For char=1 To no_chars
  CheckLimits(char)
  ResetNewValues(char)
 Next
 ;update tournament opponent
 started=0
 If gamSchedule(gamDate)=3 Then started=1
 For b=1 To 32
  If cupResult(cupSlot,b)>0 Then started=1
 Next
 If cupSize(cupSlot)>0 And started=1
  For b=1 To 32
   If cupResult(cupSlot,b)=0
    If cupBracket(cupSlot,b,1)=gamChar Then gamOpponent(gamDate)=cupBracket(cupSlot,b,2)
    If cupBracket(cupSlot,b,2)=gamChar Then gamOpponent(gamDate)=cupBracket(cupSlot,b,1)
   EndIf
  Next
 EndIf
 ;PROMOTION DEVELOPMENTS
 For fed=1 To 9
  ;store old values
  fedOldPopularity(fed)=fedPopularity(fed)
  fedOldReputation(fed)=fedReputation(fed)
  ;fluctuate values
  If fed=<6
   randy=Rnd(0,2) : level=Rnd(2,6)
   If randy=0 Then fedPopularity(fed)=fedPopularity(fed)+PursueValue(fedPopularity(fed),statLevel(level),1)
   randy=Rnd(0,2) : level=Rnd(2,6)
   If randy=0 Then fedReputation(fed)=fedReputation(fed)+PursueValue(fedReputation(fed),statLevel(level),1)
   ;finances
   factor=Rnd(10,20)
   casher=fedBank(fed)/factor
   casher=RoundOff(casher,1000)
   If casher<1000 Then casher=1000
   randy=Rnd(-2,2)
   If (randy<0 Or fedBank(fed)>2000000) And fedBank(fed)>100000 Then fedBank(fed)=fedBank(fed)-casher
   If (randy>0 Or fedBank(fed)<100000) And fedBank(fed)<2000000 Then fedBank(fed)=fedBank(fed)+casher
   If fedBank(fed)<0 Then fedBank(fed)=0
  EndIf
  ;champion logic
  If fed=>7 Then fedChampWorld(fed)=0 : fedChampInter(fed)=0 : fedChampWomens(fed)=0 : fedChampUS(fed)=0 : fedChampTV(fed)=0 : fedChampTag(fed,1)=0 : fedChampTag(fed,1)=0 ;new belts
  If charFed(fedChampWorld(fed))<>fed Then fedChampWorld(fed)=0
  If charFed(fedChampInter(fed))<>fed Then fedChampInter(fed)=0
  If charFed(fedChampTag(fed,1))<>fed Or charFed(fedChampTag(fed,2))<>fed Then fedChampTag(fed,1)=0 : fedChampTag(fed,2)=0
  ;check limits
  CheckFedLimits(fed)
 Next
 ;rank promotions
 RankPromotions()
End Function

;--------------------------------------------------------------------
;////////////////////// FIND EVENTS FOR NEWS ////////////////////////
;--------------------------------------------------------------------
Function FindEvents()
 ;reset events
 no_events=0
 For count=1 To 200
  ResetEvent(count)
 Next
 gamNewcomer=0
 ;SCAN ROSTER FOR EVENTS
For fed=1 To 7
 For cyc=1 To fedSize(fed)
  char=fedRoster(fed,cyc)
  charEvent(char)=0 : charUpdated(char)=0
  ResetNewValues(char)
  ;1-20: HEALTH ISSUES
  If char<>gamChar And InjuryStatus(char)=0
   ;match injuries
   If charOpponent(char)>0
    randy=Rnd(0,charToughness(char)*300)
    If randy=<10 And charFed(char)=charFed(gamChar) Then AddEvent(char,1) ;minor injury from match
    If randy=>11 And randy=<15 Then AddEvent(char,2) ;major injury from match
    If randy=16 Then AddEvent(char,3) ;paralysis from match
    If randy=17 And fedSize(fed)>optRosterLim/2 And fedSize(9)<optRosterLim/4 And TournamentStatus(char)=0 Then AddEvent(char,4) ;death from match 
   EndIf
   ;accidents
   randy=Rnd(0,charToughness(char)*750)
   If randy=<10 And charFed(char)=charFed(gamChar) Then AddEvent(char,5) ;minor injury from accident
   If randy=>11 And randy=<15 Then AddEvent(char,6) ;major injury from accident
   If randy=16 Then AddEvent(char,7) ;paralysis from accident
   If fedSize(fed)>optRosterLim/2 And fedSize(9)<optRosterLim/4 And TournamentStatus(char)=0
    If randy=17 Then AddEvent(char,8) ;death from accident
    randy=Rnd(0,charAttitude(char)*500)
    If randy=0 Then AddEvent(char,9) ;hedonistic death
    If randy=1 Then AddEvent(char,10) ;violent death
    randy=Rnd(0,(charPopularity(char)*charHappiness(char))*5)
    If randy=0 Then AddEvent(char,11) ;death from suicide
    randy=Rnd(0,500000)
    If randy=<charAge(char) And charAge(char)=>50 Then AddEvent(char,12) ;death from old age
   EndIf
  EndIf
  ;return from injury
  If charFed(char)=charFed(gamChar) And InjuryStatus(char)=0 And OldInjuryStatus(char)>0 Then AddEvent(char,13) 
  ;20-40: CONTRACT ISSUES
  If char=gamChar And fed=<6
   If charContract(char)=1 Then AddEvent(char,20) ;expires soon
   If charContract(char)=0 Then AddEvent(char,21) ;expired 
  EndIf
  If char<>gamChar And charContract(char)=0
   ;assess chances
   chance=50
   If fed=7 And char<>fedBooker(fed)
    chance=(90-GetValue(char))*10
    If fedSize(7)<16 Then chance=chance*2 
    If fedSize(7)=>32 Then chance=chance/2
    If chance<10 Then chance=10
   EndIf
   randy=Rnd(0,chance)
   ;renew contract
   If fed<>7 Or char=fedBooker(fed)
    If (randy=<1 And fedSize(fed)<optRosterLim) Or (randy=<3 And fedSize(fed)=<32 And charHappiness(char)=>60)
     AddEvent(char,22)
    EndIf
   EndIf
   ;move elsewhere
   If fedSize(fed)=>16 And charRole(char)<>3 And TournamentStatus(char)=0
    If randy=4 Then AddEvent(char,23)
    ;released to school
    If fed<>7 And fedSize(7)=<32 And char<>fedBooker(fed) And GetValue(char)<70 And charPopularity(char)<fedPopularity(fed)-5 And TitleHolder(char,0)=0
     If randy=5 Or (randy=6 And fedSize(fed)>32) Or (randy=7 And fedSize(7)<16)
      AddEvent(char,24) 
     EndIf 
    EndIf
    ;retirement
    chance=(70-charAge(char))*20
    If fed=7 And char<>fedBooker(fed) Then chance=chance*5
    randy=Rnd(0,chance)
    If randy=<InjuryStatus(char) And charAge(char)=>30 And fedSize(fed)=>16 And fedSize(8)<16 Then AddEvent(char,25)
   EndIf
  EndIf
  ;return from absence
  If charFed(char)=charFed(gamChar) And charVacant(char)=0 And charOldVacant(char)>0 Then AddEvent(char,27) 
  ;reaction to trades
  If charTradeReaction(char)<>0
   randy=Rnd(0,3)
   If (randy=0 Or char=gamChar) And (charFed(char)=charFed(gamChar) Or charTradeReaction(char)=gamChar Or charTradeReaction(char)=-gamChar)
    If charTradeReaction(char)>0 Then AddEvent(char,28) ;happy about move
    If charTradeReaction(char)<0 Then AddEvent(char,29) ;unhappy about move
   Else
    If charTradeReaction(char)>0 Then charAttitude(char)=charAttitude(char)+1 : charHappiness(char)=charHappiness(char)+1
    If charTradeReaction(char)<0 Then charAttitude(char)=charAttitude(char)-1 : charHappiness(char)=charHappiness(char)-1
   EndIf
   charTradeReaction(char)=0
  EndIf
  ;40-60: STATUS ISSUES
  ;stripped of titles
  If (InjuryStatus(char)=>4 And charOpponent(char)=0) Or charVacant(char)=>4
   If TitleHolder(char,1) Then AddEvent(char,40) 
   If TitleHolder(char,2) Then AddEvent(char,41) 
   If TitleHolder(char,3) Then AddEvent(char,42)
;new belts
If TitleHolder(char,5) Then AddEvent(char,40)
If TitleHolder(char,6) Then AddEvent(char,40)
If TitleHolder(char,7) Then AddEvent(char,40)

 
  EndIf
  ;acknowledge new champion
  If char=fedChampWorld(fed) And char<>fedOldChampWorld(fed) Then AddEvent(char,43) 
  If char=fedChampInter(fed) And char<>fedOldChampInter(fed) Then AddEvent(char,44) 
  If char=fedChampTag(fed,1) And char<>fedOldChampTag(fed,1) And char<>fedOldChampTag(fed,2) Then AddEvent(char,45)
;new belts
If char=fedChampWomens(fed) And char<>fedOldChampWomens(fed) Then AddEvent(char,242)
If char=fedChampUS(fed) And char<>fedOldChampUS(fed) Then AddEvent(char,243)
If char=fedChampTV(fed) And char<>fedOldChampTV(fed) Then AddEvent(char,244)
 
  ;tournament progress
  If charOpponent(char)>0 And charBracket(char)>0 And cupResult(cupSlot,charBracket(char))>0
   If cupBracket(cupSlot,charBracket(char),cupResult(cupSlot,charBracket(char)))=char
    If charBracket(char)=1 Then AddEvent(char,46) 
    If charBracket(char)>1 And charEvent(charOpponent(char))<>26 Then AddEvent(char,47) 
   Else
    If charEvent(charOpponent(char))<>25 Then AddEvent(char,48) 
   EndIf
  EndIf
  ;60-100: MISC EVENTS
  If charFed(char)=charFed(gamChar)
   ;injury gets worse
   randy=Rnd(0,charToughness(char))
   For count=0 To 5
    If randy=count And charInjured(char,count)>0 Then eVariable(no_events+1)=count : AddEvent(char,60)
   Next  
   ;injury gets better
   randy=Rnd(0,(110-charToughness(char))*2)
   For count=0 To 5
    If randy=count And charInjured(char,count)>1 Then eVariable(no_events+1)=count : AddEvent(char,61) 
   Next
   ;health fluctuation
   randy=Rnd(0,100)
   If randy=0 And InjuryStatus(char)=0 And charHealth(char)>50 Then AddEvent(char,62) ;feeling ill
   If randy=1 And InjuryStatus(char)=0 And charHealth(char)<75 Then AddEvent(char,63) ;feeling good
   ;body changes
   If charGender(char)=0
    randy=Rnd(0,200)
    If (randy=0 Or (randy=1 And char=gamChar And gamAgreement(13)>0)) And charWeight(char)=<275 Then AddEvent(char,64) ;gain weight
    If (randy=2 Or (randy=3 And char=gamChar And gamAgreement(14)>0)) And charWeight(char)=>100 Then AddEvent(char,65) ;lose weight
   EndIf 
   If randy=>2 And randy=<3 And charHeight(char)<24
    If charAge(char)=<21 Or (char=gamChar And gamAgreement(13)>0) Then AddEvent(char,66) ;grow taller 
   EndIf
  EndIf
  ;reach peak
  chance=(50-charAge(char))*50
  If chance<0 Then chance=0
  randy=Rnd(0,chance) 
  If randy=0 And charAge(char)=>30 And charPeaked(char)=0 Then AddEvent(char,67) 
  ;spontaneous stat improvements
  If charFed(char)=charFed(gamChar)
   randy=Rnd(0,(110-charAttitude(char))*20)
   If randy=1 And charPopularity(char)<90 Then AddEvent(char,68) ;popularity
   If charPeaked(char)=0
    If randy=2 And charStrength(char)<90 Then AddEvent(char,69) ;strength
    If randy=3 And charSkill(char)<90 Then AddEvent(char,70) ;skill
    If randy=4 And charAgility(char)<90 Then AddEvent(char,71) ;agility
    If randy=5 And charStamina(char)<90 Then AddEvent(char,72) ;stamina
    If randy=6 And charToughness(char)<90 Then AddEvent(char,73) ;toughness
   EndIf
   If randy=7 And charAttitude(char)<90 And char<>gamChar Then AddEvent(char,74) ;attitude
   If randy=8 And charHappiness(char)<90 And char<>gamChar Then AddEvent(char,75) ;happiness
   ;spontaneous stat deteriorations
   randy=Rnd(0,charAttitude(char)*10)
   If randy=1 And charPopularity(char)>60 Then AddEvent(char,76) ;popularity
   If randy=2 And charStrength(char)>60 Then AddEvent(char,77) ;strength
   If randy=3 And charSkill(char)>60 Then AddEvent(char,78) ;skill
   If randy=4 And charAgility(char)>60 Then AddEvent(char,79) ;agility
   If randy=5 And charStamina(char)>60 Then AddEvent(char,80) ;stamina
   If randy=6 And charToughness(char)>60 Then AddEvent(char,81) ;toughness
   If randy=7 And charAttitude(char)>60 And char<>gamChar Then AddEvent(char,82) ;attitude
   If randy=8 And charHappiness(char)>60 And char<>gamChar Then AddEvent(char,83) ;happiness
  EndIf
  ;blamed for injuries
  If gamSchedule(gamDate-1)>0
   v=gamOpponent(gamDate-1)
   If char=gamChar And v>0 And v<>gamChar
    randy=Rnd(0,1)
    If randy=0 And InjuryStatus(v)>1 Then AddEvent(char,84) ;player blamed for injury 
    If charFed(v)=9 Then AddEvent(char,85) ;player blamed for death
   EndIf
   randy=Rnd(0,1)
   If randy=0 And char=v And char<>gamChar And InjuryStatus(gamChar)>1 Then AddEvent(char,84) ;blamed for player's injury 
  EndIf
  ;drug side effects
  If char=gamChar And (gamAgreement(13)>0 Or gamAgreement(14)>0)
   randy=Rnd(0,6)
   If randy=<1 And gamAgreement(13)>0 And charWeight(char)=<275 Then AddEvent(char,64) ;gain weight
   If randy=<1 And gamAgreement(14)>0 And charWeight(char)=>125 Then AddEvent(char,65) ;lose weight
   If randy=2 And InjuryStatus(char)=0 Then AddEvent(char,86) ;withdrawl symptons
   If randy=3 And charPopularity(char)=>60 Then AddEvent(char,87) ;exposed
  EndIf
  ;lack of exposure damages popularity
  randy=Rnd(0,5)
  If randy=0 And char=gamChar And gamDate>1 And gamSchedule(gamDate-1)=<0 And charPopularity(char)>50 Then AddEvent(char,88)
  ;89=friendship expires
  ;100-120: RELATIONSHIP ISSUES
  If charFed(char)=charFed(gamChar)
   ;management
   If char<>gamChar
    randy=Rnd(0,200)
    If randy=<1 And charManager(char)=0 And charPartner(char)=0 Then AddEvent(char,100) ;acquire a manager
    If charManager(char)>0
     If randy=2 Then AddEvent(char,101) ;amicable separation from manager
     If randy=3 Then AddEvent(char,102) ;bitter separation from manager
     If randy=>4 And randy=<5 And TitleHolder(charManager(char),0)>0 Then AddEvent(char,101) ;amicable separation from manager
    EndIf
   EndIf
   If charManager(char)>0
    randy=Rnd(0,50)
    If randy=0 And charPopularity(char)<charPopularity(charManager(char)) Then AddEvent(char,103) ;manager boosts profile
    If randy=1 And charPopularity(char)>charPopularity(charManager(char)) Then AddEvent(char,104) ;manager taints profile
   EndIf
   ;team-mates
   If char<>gamChar
    randy=Rnd(0,300)
    If randy=<1 And charPartner(char)=0 And TitleHolder(char,0)=0 Then AddEvent(char,105) ;acquire a partner
    If charPartner(char)>0 And TitleHolder(char,3)=0
     If randy=2 Then AddEvent(char,106) ;amicable separation from partner
;New Belts 5 6 7
     If TitleHolder(char,1) Or TitleHolder(char,2) Or TitleHolder(char,5) Or TitleHolder(char,6) Or TitleHolder(char,7) Or TitleHolder(charPartner(char),1) Or TitleHolder(charPartner(char),2) Or TitleHolder(charPartner(char),5) Or TitleHolder(charPartner(char),6) Or TitleHolder(charPartner(char),7)
      If randy=>3 And randy=<4 Then AddEvent(char,106) ;lose partner if solo champ 
     EndIf
    EndIf
    If randy=5 And charPartner(char)>0 And TitleHolder(char,3)=0 Then AddEvent(char,107) ;bitter separation from partner
   EndIf
   If charPartner(char)>0
    randy=Rnd(0,50)
    If randy=0 And charPopularity(char)<charPopularity(charPartner(char)) Then AddEvent(char,108) ;partner boosts profile
    If randy=1 And charPopularity(char)>charPopularity(charPartner(char)) Then AddEvent(char,109) ;partner taints profile
   EndIf
   ;relationships
   If char<>gamChar
    randy=Rnd(0,400)
    If randy=1 Then AddEvent(char,110) ;internal friendship
    If randy=2 Then AddEvent(char,111) ;internal rivalry
    If randy=3 Then AddEvent(char,112) ;external friendship
    If randy=4 Then AddEvent(char,113) ;external rivalry 
    If randy=5 Then AddEvent(char,118) ;settle differences
    ;feud with you loses momentum
    If charRelationship(char,gamChar)<-4
     chance=100+charRelationship(char,gamChar)
     randy=Rnd(0,chance)
     If randy=<0 Then AddEvent(char,119) 
    EndIf
    ;friendship with you loses meaning
    If charRelationship(char,gamChar)>8
     chance=100-charRelationship(char,gamChar)
     randy=Rnd(0,chance)
     If randy=<0 Then AddEvent(char,89) 
    EndIf
    ;allegiance 
    chance=100
    If charPartner(char)>0 And charHeel(char)<>charHeel(charPartner(char)) Then chance=chance/2
    If charManager(char)>0 And charHeel(char)<>charHeel(charManager(char)) Then chance=chance/2
    If charHeel(char)=1 And AllegianceRatio(fed,0)<AllegianceRatio(fed,1)-2 Then chance=chance/2
    If charHeel(char)=0 And AllegianceRatio(fed,1)<AllegianceRatio(fed,0)-2 Then chance=chance/2
    If charHeel(char)=1 And AllegianceRatio(fed,0)>AllegianceRatio(fed,1)+2 Then chance=chance*2
    If charHeel(char)=0 And AllegianceRatio(fed,1)>AllegianceRatio(fed,0)+2 Then chance=chance*2
    randy=Rnd(0,chance)
    If randy=0 And charHeel(char)=1 And AllegianceRatio(fed,0)=<AllegianceRatio(fed,1)+2 Then AddEvent(char,114) ;face turn
    If randy=1 And charHeel(char)=0 And AllegianceRatio(fed,1)=<AllegianceRatio(fed,0)+2 Then AddEvent(char,115) ;heel turn
   EndIf
   ;fans determine allegiance
   If fed=<6
    chance=100
    If charHeel(char)=1 And AllegianceRatio(fed,0)<AllegianceRatio(fed,1)-2 Then chance=chance/2
    If charHeel(char)=0 And AllegianceRatio(fed,1)<AllegianceRatio(fed,0)-2 Then chance=chance/2
    If charHeel(char)=1 And AllegianceRatio(fed,0)>AllegianceRatio(fed,1)+2 Then chance=chance*2
    If charHeel(char)=0 And AllegianceRatio(fed,1)>AllegianceRatio(fed,0)+2 Then chance=chance*2
    randy=Rnd(0,chance)
    If randy=0 And charHeel(char)=0 Then AddEvent(char,116) ;refuse to accept face
    If randy=1 And charHeel(char)=1 Then AddEvent(char,117) ;refuse to accept heel
   EndIf
  EndIf
  ;170-180: KNOWLEDGE REPORTS
  If char=gamChar
   ;new moves
   gamNewMoves=0 
   For style=1 To 4
    For move=1 To 200
     If moveLearned(style,move)=1 Then moveLearned(style,move)=2 : gamNewMoves=gamNewMoves+1
    Next
   Next
   ;new attacks
   gamNewAttacks=0
   For style=1 To 5
    For move=1 To 50
     If attackLearned(style,move)=1 Then attackLearned(style,move)=2 : gamNewAttacks=gamNewAttacks+1
     If crushLearned(style,move)=1 Then crushLearned(style,move)=2 : gamNewAttacks=gamNewAttacks+1
    Next
   Next
   ;new gestures 
   gamNewTaunts=0
   For move=1 To 200
    If tauntLearned(move)=1 Then tauntLearned(move)=2 : gamNewTaunts=gamNewTaunts+1
   Next
   If gamNewMoves>0 Or gamNewAttacks>0 Or gamNewTaunts>0 Then AddEvent(char,170)
   ;new costumes
   gamNewCostumes=0
   For coz=1 To 3
    For count=1 To 200
     If costumeLearned(coz,count)=1 Then costumeLearned(coz,count)=2 : gamNewCostumes=gamNewCostumes+1
    Next
   Next
   ;new music
   gamNewMusic=0
   For count=1 To 100
    If musicLearned(count)=1 Then musicLearned(count)=2 : gamNewMusic=gamNewMusic+1
   Next 
   If gamNewCostumes>0 Or gamNewMusic>0 Then AddEvent(char,171)
  EndIf
  ;120-140: SCHEDULE ISSUES
  If char=gamChar
   ;urgent cancellations
   For date=gamDate To 48
    v=gamOpponent(date) : cancel=0
    If v>0 And gamSchedule(date)<>3 And gamSchedule(date)<>4
     randy=Rnd(0,100)
     If randy=0 Then cancel=1
     If (InjuryStatus(char)>0 And date<gamDate+InjuryStatus(char)) Or (InjuryStatus(v)>0 And date<gamDate+InjuryStatus(v)) Then cancel=1
     If (charVacant(char)>0 And date<gamDate+charVacant(char)) Or (charVacant(v)>0 And date<gamDate+charVacant(v)) Then cancel=1
     If (gamSchedule(date)=<2 And charFed(char)<>charFed(v) And fed<>7) Or charFed(v)=>8 Then cancel=1
    EndIf
    If cancel=1 Then eVariable(no_events+1)=date : AddEvent(v,120)
   Next
   ;booking issues
   If fed=<6 And InjuryStatus(char)=0 And charVacant(char)=0
    ;left off card
    chance=(GetValue(char)-40)*(charHealth(char)/10)
    If chance<30 Then chance=30
    randy=Rnd(0,chance)
    If randy=<10 And gamSchedule(gamDate)>0 And gamSchedule(gamDate)<>3 And gamPromo(gamDate)=0 And gamOpponent(gamDate)=0 And TitleHolder(char,0)=0 Then AddEvent(char,121) 
    ;booked in advance
    For date=gamDate+1 To gamDate+4
     chance=(date-gamDate)*4
     If gamSchedule(date)=>2 Then chance=chance/2
     randy=Rnd(0,chance)
     If randy=0 And gamSchedule(date)>0 And gamSchedule(date)<>3 And gamOpponent(date)=0 Then eVariable(no_events+1)=date : AddEvent(char,122)
    Next
   EndIf
   ;event reminders
   If gamSchedule(gamDate)=3 And gamSchedule(gamDate-1)<>3 And gamOpponent(gamDate)>0 Then AddEvent(gamOpponent(gamDate),123) ;tournament draw
   If gamSchedule(gamDate)=>2 And gamSchedule(gamDate)<>3 And InjuryStatus(char)=0 And charVacant(char)=0 Then AddEvent(char,126) ;big night tonight
   If gamSchedule(gamDate+1)=>2 And gamSchedule(gamDate)<>3 And InjuryStatus(char)=0 And charVacant(char)=0 Then AddEvent(char,124) ;big night next
   If gamDate=1 Then AddEvent(char,125)
  EndIf
  ;140-170: MISSION REMINDERS
  If char=gamChar And gamMission>0 Then AddEvent(char,140+gamMission) 
 Next
 ;PROMOTION ISSUES (200+)
 fedNewPopularity(fed)=fedPopularity(fed)
 fedNewReputation(fed)=fedReputation(fed)  
 ;generate new character
 If gamNewcomer=0
  randy=Rnd(0,100)
  If randy=0 Or (randy=<5 And fed=7) Then AddEvent(0,31)
 EndIf
 ;buy talent
 randy=Rnd(0,fedSize(fed))
 If randy=0 And fed=<6 And fedSize(fed)=<32 Then AddEvent(0,26) 
 ;new booker
 randy=Rnd(0,1000)
 If randy=<1 Or fedBooker(fed)=0 Or charFed(fedBooker(fed))<>fed Then AddEvent(0,200)
 ;vacant titles
 If fed=<6 And fedSize(fed)=>5
  If fedChampWorld(fed)=0 Or charFed(fedChampWorld(fed))<>fed Then AddEvent(0,201) ;new world champ
  If fedChampInter(fed)=0 Or charFed(fedChampInter(fed))<>fed Then AddEvent(0,202) ;new inter champ
;new belts
If fedChampWomens(fed)=0 Or charFed(fedChampWomens(fed))<>fed Then AddEvent(0,204) ;new womens champ
If fedChampUS(fed)=0 Or charFed(fedChampUS(fed))<>fed Then AddEvent(0,205) ;new US champ
If fedChampTV(fed)=0 Or charFed(fedChampTV(fed))<>fed Then AddEvent(0,206) ;new TV champ

  If fedChampTag(fed,1)=0 Or fedChampTag(fed,2)=0 Or charFed(fedChampTag(fed,1))<>fed Or charFed(fedChampTag(fed,2))<>fed Then AddEvent(0,203) ;new tag champs
 EndIf
 ;status fluctuations
 If fed=<6
  If fedRanked(fed)<fedOldRanked(fed) Then AddEvent(0,210) ;acknowledge ranking change
  randy=Rnd(0,20)
  If randy=0 And fedChampWorld(fed)>0 And charPopularity(fedChampWorld(fed))<fedPopularity(fed) Then AddEvent(0,211) ;champ taints popularity
  If randy=1 And fedChampWorld(fed)>0 And charPopularity(fedChampWorld(fed))>fedPopularity(fed) Then AddEvent(0,212) ;champ boosts popularity
  randy=Rnd(0,100)
  If randy=1 And fedPopularity(fed)<90 Then AddEvent(0,213) ;major popularity boost
  If randy=2 And fedPopularity(fed)>60 Then AddEvent(0,214) ;major popularity decrease
  If randy=3 And fedReputation(fed)<90 Then AddEvent(0,215) ;major reptation boost
  If randy=4 And fedReputation(fed)>60 Then AddEvent(0,216) ;major reputation decrease
 EndIf
 If fed=charFed(gamChar)
  randy=Rnd(0,100)
  If randy=0 Then AddEvent(gamChar,217) ;whole industry improves
  If randy=1 Then AddEvent(gamChar,218) ;whole industry suffers
 EndIf
 ;schedule issues
 If fed=<6 And charFed(gamChar)=<6 And fedFatality(fed)>0 Then AddEvent(0,234) ;memorial show planned
 If fed=charFed(gamChar)
  randy=Rnd(0,50)
  If randy=0 And gamSchedule(gamDate)>0 And gamSchedule(gamDate)<>3 And gamPromo(gamDate)=0 Then AddEvent(0,230) ;last minute cancellation
  If randy=1 And gamDate=<47 And gamSchedule(gamDate+1)>0 And gamSchedule(gamDate+1)<>3 And gamPromo(gamDate+1)=0 And gamFatality=0 Then AddEvent(0,231) ;next week cancellation
  If randy=2 And gamDate=<44 And gamSchedule(gamDate+4)>0 And gamSchedule(gamDate+4)<>3 Then AddEvent(0,232) ;planned rest
  visit=0 : tour=0 : tourn=0 : inter=0 : charity=0
  For date=gamDate-2 To gamDate+4
   If date>0
    If charFed(gamOpponent(date))=<6 Then visit=1
    If gamSchedule(date)=2 And (gamSchedule(date-1)=2 Or gamSchedule(date+1)=2) Then tour=1
    If gamSchedule(date)=3 Then tourn=1
    If gamSchedule(date)=4 Then inter=1
    If gamSchedule(date)=5 Then charity=1
   EndIf
  Next
  If InjuryStatus(gamChar)=0
   randy=Rnd(-1,50)
   If randy=<1 And fed=7 And visit=0 And gamDate=<47 Then AddEvent(0,233) ;champion visits school
   If gamDate=<44 And gamSchedule(gamDate+4)=<2
    If randy=2 And fed=<6 And tour=0 And gamDate=<42 And gamSchedule(gamDate+5)=<2 And gamSchedule(gamDate+6)=<2 Then AddEvent(0,235) ;PPV tour
    If tourn=0 And cupSize(cupSlot)=0 And gamDate=<40 
     If randy=3 Then AddEvent(0,236) ;one-night tournament
     If randy=4 And inter=0 And charity=0 Then AddEvent(0,237) ;sprawling internal tournament
     If randy=5 And fed=<6 And inter=0 And charity=0 Then AddEvent(0,238) ;sprawling universal tournament
    EndIf
    If randy=6 And fed=<6 And inter=0 Then AddEvent(0,239) ;2-way inter-promotional
    If randy=7 And fed=<6 And inter=0 And optContent>0 Then AddEvent(0,241) ;6-way inter-promotional
    If randy=8 And fed=<6 And charity=0 Then AddEvent(0,240) ;charity event
   EndIf
  EndIf
 EndIf
 ;verify changes
 CheckFedLimits(fed)
 If fedNewPopularity(fed)<>fedPopularity(fed) Then fedOldPopularity(fed)=fedPopularity(fed)
 If fedNewReputation(fed)<>fedReputation(fed) Then fedOldReputation(fed)=fedReputation(fed)
 For char=1 To no_chars
  CheckLimits(char)
 Next
Next
 ;nothing to report!
 If no_events=0 Then fed=0 : AddEvent(0,0) 
End Function

;--------------------------------------------------------------------
;//////////////////// ADD AN EVENT TO THE LIST //////////////////////
;--------------------------------------------------------------------
Function AddEvent(char,event)
 If no_events<200 ;And (charFed(char)=<8 Or char=0)
  ;increment list
  no_events=no_events+1
  eChar(no_events)=char
  eFed(no_events)=fed
  eEvent(no_events)=event
  If char>0 Then charEvent(char)=event
  eFoc(no_events)=10
  ;newspaper make-up
  eNewspaper(no_events)=Rnd(1,1)
  If char=0 Or char=fedBooker(charFed(char)) Or charRole(char)>1
   eBackground(no_events)=Rnd(1,3)
  Else
   eBackground(no_events)=Rnd(4,12)
  EndIf
  eAdvert(no_events,1)=Rnd(1,9)
  Repeat
   eAdvert(no_events,2)=Rnd(1,9)
  Until eAdvert(no_events,2)<>eAdvert(no_events,1)
  ;HEALTH ISSUES
  If event=1 Or event=5 Then ApplyInjury(char,Rnd(0,5),1) ;minor injury
  If event=2 Or event=6 Then ApplyInjury(char,Rnd(0,5),2) ;major injury 
  If event=3 Or event=7 Then ApplyInjury(char,99,2) ;paralysis!
  ;death
  If event=4 Or (event=>8 And event=<12)
   If event=10
    its=0
    Repeat
     newbie=Rnd(1,no_chars) : its=its+1
     If its>1000 Then charRelationship(char,newbie)=-1
    Until newbie<>char And charRelationship(char,newbie)<0 And charFed(newbie)=<8
    eVariable(no_events)=newbie
   EndIf
   gamFatality=char
   fedFatality(fed)=char 
   MoveChar(char,9)
   charNewHealth(char)=0
  EndIf
  ;CONTRACT ISSUES
  ;renew contract
  If event=22 
   GenerateContract(char)
   charNewAttitude(char)=charNewAttitude(char)+Rnd(-1,1)
   charNewHappiness(char)=charNewHappiness(char)+1;PursueValue(charHappiness(char),100,0)
   randy=Rnd(0,1)
   If randy=0
    ResetEvent(no_events)
    no_events=no_events-1
   EndIf
  EndIf
  ;move elsewhere
  If event=23 
   Repeat
    newbie=Rnd(1,6) : satisfied=1
    randy=Rnd(0,2)
    If (randy>0 And fedSize(newbie)>32) Or fedSize(newbie)=>optRosterLim Then satisfied=0
    If newbie=fed Then satisfied=0
   Until satisfied=1
   MoveChar(char,newbie)
   GenerateContract(char)
   charNewAttitude(char)=charNewAttitude(char)+Rnd(-1,1)
   If charNewHappiness(char)<50 Then charNewHappiness(char)=50
   charNewHappiness(char)=charNewHappiness(char)+PursueValue(charHappiness(char),100,0)
   If charFed(char)=<6 And charFed(char)=charFed(gamChar) And charPartner(gamChar)=0 And gamSchedule(gamDate)=>1 And gamSchedule(gamDate)=<2 And gamMatch(gamDate)=0 And gamPromo(gamDate)=0
    If charRelationship(gamChar,char)>0 Then gamMatch(gamDate)=12 : gamPromo(gamDate)=99 : gamPromoVariable(gamDate)=char
   EndIf
  EndIf
  ;released
  If event=24
   MoveChar(char,7)
   GenerateContract(char)
   charNewPopularity(char)=charNewPopularity(char)+PursueValue(charPopularity(char),30,0)
   charNewAttitude(char)=charNewAttitude(char)+PursueValue(charAttitude(char),30,0)
   charNewHappiness(char)=charNewHappiness(char)+PursueValue(charHappiness(char),30,0) 
  EndIf
  ;retired
  If event=25
   MoveChar(char,8)
   GenerateContract(char)
   charNewPopularity(char)=charNewPopularity(char)+1 
   charNewAttitude(char)=charNewAttitude(char)+Rnd(-1,1)
   charNewHappiness(char)=charNewHappiness(char)+PursueValue(charHappiness(char),100,0) 
  EndIf
  ;bought
  If event=26 
   its=0
   Repeat
    satisfied=1 : its=its+1
    char=Rnd(1,no_chars)
    If char=gamChar Or charRole(char)=3 Or char=fedBooker(8) Or char=fedBooker(9) Then satisfied=0
    If charFed(char)=fed Or charFed(char)=9 Or (charFed(char)=<6 And fedSize(charFed(char))=<20) Then satisfied=0
    If charEvent(char)>0 Then satisfied=0
   Until satisfied=1 Or its>1000
   eVariable(no_events)=charFed(char)
   MoveChar(char,fed)
   GenerateContract(char)
   charSalary(char)=charSalary(char)*2
   ResetNewValues(char)
   charNewPopularity(char)=charNewPopularity(char)+1 
   charNewAttitude(char)=charNewAttitude(char)+Rnd(-1,1)
   If charNewHappiness(char)<50 Then charNewHappiness(char)=50
   charNewHappiness(char)=charNewHappiness(char)+PursueValue(charHappiness(char),100,0) 
   CheckNewValues(char)
   eChar(no_events)=char
   If charFed(char)=<6 And charFed(char)=charFed(gamChar) And charPartner(gamChar)=0 And gamSchedule(gamDate)=>1 And gamSchedule(gamDate)=<2 And gamMatch(gamDate)=0 And gamPromo(gamDate)=0
    If charRelationship(gamChar,char)>0 Then gamMatch(gamDate)=12 : gamPromo(gamDate)=99 : gamPromoVariable(gamDate)=char
    If eVariable(no_events)=8 Then gamMatch(gamDate)=2 : gamOpponent(gamDate)=char : gamPromo(gamDate)=101
   EndIf
  EndIf
  ;happy about move
  If event=28
   eVariable(no_events)=charTradeReaction(char)
   charNewAttitude(char)=charNewAttitude(char)+1
   charNewHappiness(char)=charNewHappiness(char)+1
  EndIf
  ;unhappy about move
  If event=29
   eVariable(no_events)=MakePositive#(charTradeReaction(char))
   charNewAttitude(char)=charNewAttitude(char)-1
   charNewHappiness(char)=charNewHappiness(char)-1
  EndIf
  ;generate new character
  If event=31
   gamNewcomer=0
   Repeat
    randy=Rnd(1,5)
    If randy=<3 And gamFatality=0 Then gamNewcomer=fedRoster(9,Rnd(1,fedSize(9)))
    If randy=4 Then gamNewcomer=fedRoster(8,Rnd(1,fedSize(8)))
    If randy=5 Then gamNewcomer=fedRoster(7,Rnd(1,fedSize(7)))
   Until gamNewcomer>0 And gamNewcomer<>fedBooker(charFed(gamNewcomer)) And gamNewcomer<>gamChar And gamNewcomer<>charPartner(gamChar)
   eVariable(no_events)=charFed(gamNewcomer)
   eChar(no_events)=gamNewcomer : charEvent(gamNewcomer)=event
   GenerateCharacter(gamNewcomer)
   MoveChar(gamNewcomer,fed)
   GenerateContract(gamNewcomer) 
   charPhoto(gamNewcomer)=CopyImage(charPhoto(0))
   SaveImage(charPhoto(gamNewcomer),filer$+"Portraits/Photo"+Dig$(gamNewcomer,100)+".bmp")
   charPhotoR(gamNewcomer)=24 : charPhotoG(gamNewcomer)=4 : charPhotoB(gamNewcomer)=0
   MaskImage charPhoto(gamNewcomer),charPhotoR(gamNewcomer),charPhotoG(gamNewcomer),charPhotoB(gamNewcomer)
  EndIf
  ;STATUS ISSUES
  ;stripped of title
  If event=>40 And event=<42
   If fedChampWorld(fed)=char Then fedChampWorld(fed)=0
   If fedChampInter(fed)=char Then fedChampInter(fed)=0
;new belts
If fedChampWomens(fed)=char Then fedChampWomens(fed)=0
If fedChampUS(fed)=char Then fedChampUS(fed)=0
If fedChampTV(fed)=char Then fedChampTV(fed)=0
;end

   If fedChampTag(fed,1)=char Or fedChampTag(fed,2)=char Then fedChampTag(fed,1)=0 : fedChampTag(fed,2)=0
   charNewPopularity(char)=charNewPopularity(char)-1
   charNewHappiness(char)=charNewHappiness(char)+PursueValue(charHappiness(char),30,0)
  EndIf
  ;title winner
  If event=>43 And event=<45
   charNewPopularity(char)=charNewPopularity(char)+1
   charNewHappiness(char)=charNewHappiness(char)+1
   charTitles(char,fed,event-42)=charTitles(char,fed,event-42)+1
   If event=45 Then charTitles(charPartner(char),fed,3)=charTitles(charPartner(char),fed,3)+1
   WriteHistory(fed,event-42)
  EndIf
  ;tournament winner
  If event=46
   If cupReward(cupSlot)=0 Then fedCupHolder(charFed(char))=char : charTitles(char,fed,4)=charTitles(char,fed,4)+1 : WriteHistory(fed,4)
   If cupReward(cupSlot)=1 Then fedChampWorld(charFed(char))=char : charTitles(char,fed,1)=charTitles(char,fed,1)+1 : WriteHistory(fed,1)
   If cupReward(cupSlot)=2 Then fedChampInter(charFed(char))=char : charTitles(char,fed,2)=charTitles(char,fed,2)+1 : WriteHistory(fed,2)
;new belts
If cupReward(cupSlot)=5 Then fedChampWomens(charFed(char))=char : charTitles(char,fed,5)=charTitles(char,fed,5)+1 : WriteHistory(fed,5)
If cupReward(cupSlot)=6 Then fedChampUS(charFed(char))=char : charTitles(char,fed,6)=charTitles(char,fed,6)+1 : WriteHistory(fed,6)
If cupReward(cupSlot)=7 Then fedChampTV(charFed(char))=char : charTitles(char,fed,7)=charTitles(char,fed,7)+1 : WriteHistory(fed,7)
;end
   If cupReward(cupSlot)=3
    fedChampTag(charFed(char),1)=char : fedChampTag(charFed(char),2)=cupCharPartner(cupSlot,char)
    charTitles(char,fed,3)=charTitles(char,fed,3)+1 : charTitles(fedChampTag(charFed(char),2),fed,3)=charTitles(fedChampTag(charFed(char),2),fed,3)+1
    WriteHistory(fed,3)
   EndIf
   charNewPopularity(char)=charNewPopularity(char)+1
   charNewHappiness(char)=charNewHappiness(char)+1
   cupSize(cupSlot)=0
  EndIf
  ;MISC EVENTS
  ;injury gets worse
  If event=60
   charInjured(char,eVariable(no_events))=charInjured(char,eVariable(no_events))*2
   charNewStrength(char)=charNewStrength(char)-Rnd(0,1)
   charNewSkill(char)=charNewSkill(char)-Rnd(0,1)
   charNewAgility(char)=charNewAgility(char)-Rnd(0,1)
   charNewStamina(char)=charNewStamina(char)-Rnd(0,1)
   charNewToughness(char)=charNewToughness(char)-Rnd(0,1)
   charNewHappiness(char)=charNewHappiness(char)-1
   charHappiness(charManager(char))=charHappiness(charManager(char))-1
  EndIf
  ;injury gets better
  If event=61
   charInjured(char,eVariable(no_events))=charInjured(char,eVariable(no_events))/2
   charNewHappiness(char)=charNewHappiness(char)+1
   charHappiness(charManager(char))=charHappiness(charManager(char))+1
  EndIf
  ;feeling ill/good
  If event=62 Then charNewHealth(char)=0 : charNewHappiness(char)=charNewHappiness(char)-1
  If event=63 Then charNewHealth(char)=100 : charNewHappiness(char)=charNewHappiness(char)+1
  ;gain weight
  If event=64
   charWeight(char)=charWeight(char)+25
   charNewStrength(char)=charNewStrength(char)+1
   charNewSkill(char)=charNewSkill(char)-1
   charNewAgility(char)=charNewAgility(char)-1
   charNewStamina(char)=charNewStamina(char)-1
   charNewToughness(char)=charNewToughness(char)+1 
   charNewHappiness(char)=charNewHappiness(char)-1
  EndIf
  ;lose weight
  If event=65
   charWeight(char)=charWeight(char)-25
   charNewStrength(char)=charNewStrength(char)-1
   charNewSkill(char)=charNewSkill(char)+1
   charNewAgility(char)=charNewAgility(char)+1
   charNewStamina(char)=charNewStamina(char)+1
   charNewToughness(char)=charNewToughness(char)-1
   charNewHappiness(char)=charNewHappiness(char)+1 
  EndIf
  ;grow taller
  If event=66
   charHeight(char)=charHeight(char)+1
   charNewStrength(char)=charNewStrength(char)+1
   charNewSkill(char)=charNewSkill(char)-1
   charNewAgility(char)=charNewAgility(char)-1
   charNewStamina(char)=charNewStamina(char)-1
   charNewToughness(char)=charNewToughness(char)+1
   charNewHappiness(char)=charNewHappiness(char)+1 
  EndIf
  ;reached peak
  If event=67
   charPeaked(char)=charAge(char)
   charNewHappiness(char)=charNewHappiness(char)-1
   charHappiness(charManager(char))=charHappiness(charManager(char))-1 
  EndIf
  ;spontaneous stat improvements
  If event=>68 And event=<74 Then charNewHappiness(char)=charNewHappiness(char)+1
  If event=68 Then charNewPopularity(char)=charNewPopularity(char)+PursueValue(charPopularity(char),100,0)
  If event=69 Then charNewStrength(char)=charNewStrength(char)+PursueValue(charStrength(char),100,0) : charWeightChange(char)=charWeightChange(char)+Rnd(1,3)
  If event=70 Then charNewSkill(char)=charNewSkill(char)+PursueValue(charSkill(char),100,0) : charWeightChange(char)=charWeightChange(char)+1
  If event=71 Then charNewAgility(char)=charNewAgility(char)+PursueValue(charAgility(char),100,0) : charWeightChange(char)=charWeightChange(char)-Rnd(1,3)
  If event=72 Then charNewStamina(char)=charNewStamina(char)+PursueValue(charStamina(char),100,0) : charWeightChange(char)=charWeightChange(char)-1
  If event=73 Then charNewToughness(char)=charNewToughness(char)+PursueValue(charToughness(char),100,0) : charWeightChange(char)=charWeightChange(char)+1
  If event=74 Then charNewAttitude(char)=charNewAttitude(char)+PursueValue(charAttitude(char),100,0)
  If event=75 Then charNewHappiness(char)=charNewHappiness(char)+PursueValue(charHappiness(char),100,0)
  ;spontaneous stat deterioration
  If event=>76 And event=<82 Then charNewHappiness(char)=charNewHappiness(char)-1
  If event=76 Then charNewPopularity(char)=charNewPopularity(char)+PursueValue(charPopularity(char),30,0)
  If event=77 Then charNewStrength(char)=charNewStrength(char)+PursueValue(charStrength(char),30,0) : charWeightChange(char)=charWeightChange(char)-Rnd(1,3)
  If event=78 Then charNewSkill(char)=charNewSkill(char)+PursueValue(charSkill(char),30,0) : charWeightChange(char)=charWeightChange(char)-1
  If event=79 Then charNewAgility(char)=charNewAgility(char)+PursueValue(charAgility(char),30,0) : charWeightChange(char)=charWeightChange(char)+Rnd(1,3)
  If event=80 Then charNewStamina(char)=charNewStamina(char)+PursueValue(charStamina(char),30,0) : charWeightChange(char)=charWeightChange(char)+1
  If event=81 Then charNewToughness(char)=charNewToughness(char)+PursueValue(charToughness(char),30,0) : charWeightChange(char)=charWeightChange(char)-1
  If event=82 Then charNewAttitude(char)=charNewAttitude(char)+PursueValue(charAttitude(char),30,0)
  If event=83 Then charNewHappiness(char)=charNewHappiness(char)+PursueValue(charHappiness(char),30,0)
  ;blamed for injuries
  If event=84 Or event=85
   If gamOpponent(gamDate-1)=char Then v=gamChar Else v=gamOpponent(gamDate-1)
   If event=85 Or InjuryStatus(v)>4 Then limit=0 Else limit=1
   charPopularity(v)=charPopularity(v)+1
   charNewPopularity(char)=charNewPopularity(char)+PursueValue(charPopularity(char),30,limit) 
   charNewAttitude(char)=charNewAttitude(char)+PursueValue(charAttitude(char),30,0)
   charNewHappiness(char)=charNewHappiness(char)+PursueValue(charHappiness(char),30,limit)
   charHappiness(charManager(char))=charHappiness(charManager(char))+PursueValue(charHappiness(charManager(char)),30,limit)
  EndIf
  ;bad reaction to drugs
  If event=86
   charNewHealth(char)=0 : charInjured(char,0)=1
   charNewAttitude(char)=charNewAttitude(char)+PursueValue(charAttitude(char),30,0)
   charNewHappiness(char)=charNewHappiness(char)+PursueValue(charHappiness(char),30,0)
   charHappiness(charManager(char))=charHappiness(charManager(char))+PursueValue(charHappiness(charManager(char)),30,0)
   If fed=<6 And char=gamChar And gamPromo(gamDate+1)=0 Then gamPromo(gamDate+1)=97
  EndIf
  ;exposed as drug user
  If event=87
   charNewPopularity(char)=charNewPopularity(char)+PursueValue(charPopularity(char),30,0)
   charNewAttitude(char)=charNewAttitude(char)+PursueValue(charAttitude(char),30,0)
   charNewHappiness(char)=charNewHappiness(char)+PursueValue(charHappiness(char),30,0)
   charHappiness(charManager(char))=charHappiness(charManager(char))+PursueValue(charHappiness(charManager(char)),30,0)
   If fed=<6 And char=gamChar And gamPromo(gamDate)=0 Then gamPromo(gamDate)=97
  EndIf
  ;lack of exposure
  If event=88
   charNewPopularity(char)=charNewPopularity(char)-1
   charNewHappiness(char)=charNewHappiness(char)-1
   charHappiness(charManager(char))=charHappiness(charManager(char))-1
  EndIf
  ;RELATIONSHIP ISSUES
  ;acquire manager
  If event=100
   its=0
   Repeat 
    satisfied=1 : its=its+1
    newbie=fedRoster(fed,Rnd(1,fedSize(fed)))
    randy=Rnd(0,1)
    If randy=0 And (GetValue(newbie)>GetValue(char) Or charRole(newbie)<>2 Or newbie=fedBooker(fed)) Then satisfied=0
    randy=Rnd(0,1)
    If (randy=0 And CountClients(newbie)>0) Or CountClients(newbie)>4 Then satisfied=0
    randy=Rnd(0,1)
    If (randy=0 And charRelationship(char,newbie)=<0) Or charRelationship(char,newbie)<0 Then satisfied=0
    randy=Rnd(0,2)
    If randy=<1 And charHeel(newbie)<>charHeel(char) Then satisfied=0
    If newbie=char Or newbie=gamChar Or charRole(newbie)=3 Then satisfied=0
   Until satisfied=1 Or its>1000
   charManager(char)=newbie
   ChangeRelationship(char,newbie,1)
   If charHeel(char)=0 Then charNewAttitude(char)=charNewAttitude(char)+1
   If charHeel(char)=1 Then charNewAttitude(char)=charNewAttitude(char)-1
   charNewHappiness(char)=charNewHappiness(char)+1
  EndIf
  ;amicable separation from manager
  If event=101
   eVariable(no_events)=charManager(char)
   charManager(char)=0
   charNewAttitude(char)=charNewAttitude(char)+1
   charNewHappiness(char)=charNewHappiness(char)+1
  EndIf
  ;bitter separation from manager
  If event=102
   eVariable(no_events)=charManager(char)
   ChangeRelationship(char,charManager(char),-1)
   charManager(char)=0
   charNewAttitude(char)=charNewAttitude(char)-1
   charNewHappiness(char)=charNewHappiness(char)-1
  EndIf
  ;relationship boosts profile
  If event=103 Or event=108
   charNewPopularity(char)=charNewPopularity(char)+1
   charNewAttitude(char)=charNewAttitude(char)+1
   charNewHappiness(char)=charNewHappiness(char)+1
  EndIf
  ;relationship damages profile
  If event=104 Or event=109
   charNewPopularity(char)=charNewPopularity(char)-1
   charNewAttitude(char)=charNewAttitude(char)-1
   charNewHappiness(char)=charNewHappiness(char)-1
  EndIf
  ;acquire partner
  If event=105
   its=0
   Repeat 
    satisfied=1 : its=its+1
    newbie=fedRoster(fed,Rnd(1,fedSize(fed)))
    randy=Rnd(0,1)
    If randy=0 And (charRole(newbie)<>1 Or newbie=fedBooker(fed)) Then satisfied=0
    randy=Rnd(0,1)
    If (randy=0 And charRelationship(char,newbie)=<0) Or charRelationship(char,newbie)<0 Then satisfied=0
    randy=Rnd(0,2)
    If randy=<1 And charHeel(newbie)<>charHeel(char) Then satisfied=0
    If newbie=char Or newbie=gamChar Or newbie=charPartner(gamChar) Or charRole(newbie)=3 Then satisfied=0
   Until satisfied=1 Or its>1000
   FormTeam(char,newbie)
   If charHeel(char)=0 Then charNewAttitude(char)=charNewAttitude(char)+1
   If charHeel(char)=1 Then charNewAttitude(char)=charNewAttitude(char)-1
   charNewHappiness(char)=charNewHappiness(char)+1
  EndIf
  ;amicable separation from partner
  If event=106
   eVariable(no_events)=charPartner(char)
   charPartner(char)=0
   charNewAttitude(char)=charNewAttitude(char)+1
   charNewHappiness(char)=charNewHappiness(char)+1
  EndIf
  ;bitter separation from partner
  If event=107
   eVariable(no_events)=charPartner(char)
   ChangeRelationship(char,charPartner(char),-1)
   If charHeel(charPartner(char))=0 Then ChangeAllegiance(char,1)
   If charHeel(charPartner(char))=1 Then ChangeAllegiance(char,0)
   charPartner(char)=0
   charNewAttitude(char)=charNewAttitude(char)-1
   charNewHappiness(char)=charNewHappiness(char)-1
  EndIf
  ;internal friendship
  If event=110
   its=0
   Repeat 
    newbie=fedRoster(fed,Rnd(1,fedSize(fed))) : its=its+1
   Until (newbie<>char And newbie<>gamChar And charRelationship(char,newbie)=<0) Or its>100
   eVariable(no_events)=newbie
   ChangeRelationship(char,newbie,1)
   ChangeAllegiance(char,charHeel(newbie))
   charNewAttitude(char)=charNewAttitude(char)+1
   charNewHappiness(char)=charNewHappiness(char)+1
  EndIf
  ;internal rivalry
  If event=111
   its=0
   Repeat 
    newbie=fedRoster(fed,Rnd(1,fedSize(fed))) : its=its+1
   Until (newbie<>char And newbie<>gamChar And charRelationship(char,newbie)=>0) Or its>100
   eVariable(no_events)=newbie
   ChangeRelationship(char,newbie,-1)
   If charHeel(newbie)=0 Then ChangeAllegiance(char,1)
   If charHeel(newbie)=1 Then ChangeAllegiance(char,0)
   charNewAttitude(char)=charNewAttitude(char)-1
   charNewHappiness(char)=charNewHappiness(char)-1
  EndIf
  ;external friendship
  If event=112
   Repeat 
    newbie=Rnd(1,no_chars)
   Until newbie<>char And newbie<>gamChar And charFed(newbie)<>fed And charFed(newbie)=<8
   eVariable(no_events)=newbie
   ChangeRelationship(char,newbie,1)
   charNewAttitude(char)=charNewAttitude(char)+1
   charNewHappiness(char)=charNewHappiness(char)+1
  EndIf
  ;external rivalry
  If event=113
   Repeat 
    newbie=Rnd(1,no_chars)
   Until newbie<>char And newbie<>gamChar And charFed(newbie)<>fed And charFed(newbie)=<8
   eVariable(no_events)=newbie
   ChangeRelationship(char,newbie,-1)
   charNewAttitude(char)=charNewAttitude(char)-1
   charNewHappiness(char)=charNewHappiness(char)-1
  EndIf
  ;face turn
  If event=114
   charHeel(char)=0
   charNewPopularity(char)=charNewPopularity(char)+1
   charNewAttitude(char)=charNewAttitude(char)+PursueValue(charAttitude(char),100,0)
   charNewHappiness(char)=charNewHappiness(char)+1
  EndIf
  ;heel turn
  If event=115
   charHeel(char)=1
   charNewPopularity(char)=charNewPopularity(char)-1
   charNewAttitude(char)=charNewAttitude(char)+PursueValue(charAttitude(char),30,0)
   charNewHappiness(char)=charNewHappiness(char)-1
  EndIf
  ;not accepted as face
  If event=116
   charHeel(char)=1
   charNewPopularity(char)=charNewPopularity(char)-1
   charNewAttitude(char)=charNewAttitude(char)-1
   charNewHappiness(char)=charNewHappiness(char)-1
  EndIf 
  ;not accepted as heel
  If event=117
   charHeel(char)=0
   charNewPopularity(char)=charNewPopularity(char)+1
   charNewAttitude(char)=charNewAttitude(char)+1
   charNewHappiness(char)=charNewHappiness(char)+1
  EndIf
  ;settle differences with enemy
  If event=118
   its=0
   Repeat
    its=its+1 : satisfied=1
    randy=Rnd(0,2)
    If randy=<1 Then newbie=fedRoster(fed,Rnd(1,fedSize(fed)))
    If randy=2 Then newbie=Rnd(1,no_chars)
    If charRelationship(char,newbie)=>0 Then satisfied=0
    If newbie=gamChar Or charFed(char)=9 Then satisfied=0
   Until satisfied=1 Or its>1000
   eVariable(no_events)=newbie
   ChangeRelationship(char,newbie,0)
   charNewAttitude(char)=charNewAttitude(char)+1
   charNewHappiness(char)=charNewHappiness(char)+1
  EndIf
  ;feud with you loses momentum
  If event=89 Or event=119 
   eVariable(no_events)=MakePositive#(charRelationship(char,gamChar))
   ChangeRelationship(char,gamChar,0)
  EndIf
  ;SCHEDULE ISSUES
  ;opponent cancelled
  If event=120 Then gamOpponent(eVariable(no_events))=0 
  ;left off card
  If event=121 
   eVariable(no_events)=gamSchedule(gamDate)
   gamSchedule(gamDate)=0
   charNewPopularity(char)=charNewPopularity(char)-Rnd(0,1)
   charNewAttitude(char)=charNewAttitude(char)-Rnd(0,1)
   charNewHappiness(char)=charNewHappiness(char)-1
  EndIf
  ;booked in advance
  If event=122
   date=eVariable(no_events) 
   If date<1 Then date=1
   gamOpponent(date)=AssignOpponent(gamChar,date,0)
   If gamOpponent(date)=0 Then gamOpponent(date)=Rnd(1,no_chars)
   eChar(no_events)=gamOpponent(date)
   AssignMatch(date)
  EndIf
  ;PROMOTION ISSUES
  ;new booker
  If event=200 
   level=60 : its=0
   Repeat
    its=its+1 : satisfied=1
    newbie=fedRoster(fed,Rnd(1,fedSize(fed)))
    randy=Rnd(0,2)
    If randy>0 And charAge(newbie)<level Then satisfied=0 
    If newbie=fedBooker(fed) Or TitleHolder(newbie,0)>0 Then satisfied=0
    If newbie=gamChar Or newbie=charPartner(gamChar) Or newbie=charManager(gamChar) Then satisfied=0
    If its>100 And level>10 Then level=level-10 : its=0 
   Until satisfied=1 Or its>1000
   char=newbie : eChar(no_events)=newbie
   fedBooker(fed)=char : WriteHistory(fed,0)
   charNewPopularity(char)=charNewPopularity(char)+PursueValue(charNewPopularity(char),100,0)
   charNewAttitude(char)=charNewAttitude(char)+Rnd(-1,1)
   charNewHappiness(char)=charNewHappiness(char)+PursueValue(charNewHappiness(char),100,0)
   If fed=charFed(gamChar) Then gamMission=0
  EndIf
  ;new world champ
  If event=201 
   level=90 : its=0
   Repeat
    its=its+1 : satisfied=1
    newbie=fedRoster(fed,Rnd(1,fedSize(fed)))
    If charPopularity(newbie)<level Then satisfied=0 
    If TitleHolder(newbie,0)>0 Or InjuryStatus(newbie)>0 Or charRole(newbie)<>1 Then satisfied=0
    If newbie=gamChar Or newbie=charPartner(gamChar) Or newbie=charManager(gamChar) Then satisfied=0
    If its>100 And level>50 Then level=level-10 : its=0 
   Until satisfied=1 Or its>1000
   char=newbie : eChar(no_events)=newbie
   fedChampWorld(fed)=char : charTitles(char,fed,1)=charTitles(char,fed,1)+1 : WriteHistory(fed,1)
   charNewPopularity(char)=charNewPopularity(char)+1
   charNewHappiness(char)=charNewHappiness(char)+1 
  EndIf
  ;new inter champ
  If event=202 
   its=0
   Repeat
    its=its+1 : satisfied=1
    newbie=fedRoster(fed,Rnd(1,fedSize(fed)))
    If TitleHolder(newbie,0)>0 Or InjuryStatus(newbie)>0 Or charRole(newbie)<>1 Then satisfied=0
    If newbie=gamChar Or newbie=charPartner(gamChar) Or newbie=charManager(gamChar) Then satisfied=0
   Until satisfied=1 Or its>1000
   char=newbie : eChar(no_events)=newbie
   fedChampInter(fed)=char : charTitles(char,fed,2)=charTitles(char,fed,2)+1 : WriteHistory(fed,2)
   charNewPopularity(char)=charNewPopularity(char)+1
   charNewHappiness(char)=charNewHappiness(char)+1 
  EndIf
  ;new tag champs
  If event=203 
   its=0
   Repeat
    its=its+1 : satisfied=1
    newbie=fedRoster(fed,Rnd(1,fedSize(fed)))
    randy=Rnd(0,1)
    If randy>0 And charPartner(newbie)=0 Then satisfied=0
    If TitleHolder(newbie,0)>0 Or InjuryStatus(newbie)>0 Or charRole(newbie)<>1 Then satisfied=0
    If newbie=gamChar Or newbie=charPartner(gamChar) Or newbie=charManager(gamChar) Then satisfied=0
   Until satisfied=1 Or its>1000
   char=newbie : eChar(no_events)=newbie
   fedChampTag(fed,1)=char : fedChampTag(fed,2)=AssignPartner(char,-1) : WriteHistory(fed,3)
   FormTeam(fedChampTag(fed,1),fedChampTag(fed,2)) 
   charTitles(char,fed,3)=charTitles(char,fed,3)+1
   charTitles(charPartner(char),fed,3)=charTitles(charPartner(char),fed,3)+1
   charNewPopularity(char)=charNewPopularity(char)+1
   charNewHappiness(char)=charNewHappiness(char)+1 
  EndIf
;New belts

;new womens champ
  If event=204 
   its=0
   Repeat
    its=its+1 : satisfied=1
    newbie=fedRoster(fed,Rnd(1,fedSize(fed)))
    If TitleHolder(newbie,0)>0 Or InjuryStatus(newbie)>0 Or charRole(newbie)<>1 Then satisfied=0
    If newbie=gamChar Or newbie=charPartner(gamChar) Or newbie=charManager(gamChar) Then satisfied=0
   Until satisfied=1 Or its>1000
   char=newbie : eChar(no_events)=newbie
   fedChampWomens(fed)=char : charTitles(char,fed,5)=charTitles(char,fed,5)+1 : WriteHistory(fed,5)
   charNewPopularity(char)=charNewPopularity(char)+1
   charNewHappiness(char)=charNewHappiness(char)+1 
  EndIf
;new US champ
  If event=205 
   its=0
   Repeat
    its=its+1 : satisfied=1
    newbie=fedRoster(fed,Rnd(1,fedSize(fed)))
    If TitleHolder(newbie,0)>0 Or InjuryStatus(newbie)>0 Or charRole(newbie)<>1 Then satisfied=0
    If newbie=gamChar Or newbie=charPartner(gamChar) Or newbie=charManager(gamChar) Then satisfied=0
   Until satisfied=1 Or its>1000
   char=newbie : eChar(no_events)=newbie
   fedChampUS(fed)=char : charTitles(char,fed,6)=charTitles(char,fed,6)+1 : WriteHistory(fed,6)
   charNewPopularity(char)=charNewPopularity(char)+1
   charNewHappiness(char)=charNewHappiness(char)+1 
  EndIf

;new TV champ
  If event=206 
   its=0
   Repeat
    its=its+1 : satisfied=1
    newbie=fedRoster(fed,Rnd(1,fedSize(fed)))
    If TitleHolder(newbie,0)>0 Or InjuryStatus(newbie)>0 Or charRole(newbie)<>1 Then satisfied=0
    If newbie=gamChar Or newbie=charPartner(gamChar) Or newbie=charManager(gamChar) Then satisfied=0
   Until satisfied=1 Or its>1000
   char=newbie : eChar(no_events)=newbie
   fedChampTV(fed)=char : charTitles(char,fed,7)=charTitles(char,fed,7)+1 : WriteHistory(fed,7)
   charNewPopularity(char)=charNewPopularity(char)+1
   charNewHappiness(char)=charNewHappiness(char)+1 
  EndIf


;end
  ;become leading wrestling show
  If event=210 And fedRanked(fed)=1
   fedNewPopularity(fed)=fedNewPopularity(fed)+1
   fedNewReputation(fed)=fedNewReputation(fed)+1 
   eFoc(no_events)=1
  EndIf
  ;champ affects popularity
  If event=211 Then fedNewPopularity(fed)=fedNewPopularity(fed)-1 : eFoc(no_events)=1 
  If event=212 Then fedNewPopularity(fed)=fedNewPopularity(fed)+1 : eFoc(no_events)=1 
  ;whole fed gets more popular
  If event=213
   For count=1 To fedSize(fed)
    charPopularity(fedRoster(fed,count))=charPopularity(fedRoster(fed,count))+1
    charHappiness(fedRoster(fed,count))=charHappiness(fedRoster(fed,count))+1
   Next
   fedNewPopularity(fed)=fedNewPopularity(fed)+PursueValue(fedPopularity(fed),100,0)
   eFoc(no_events)=1
  EndIf
  ;whole fed gets less popular
  If event=214 
   For count=1 To fedSize(fed)
    charPopularity(fedRoster(fed,count))=charPopularity(fedRoster(fed,count))-1
    charHappiness(fedRoster(fed,count))=charHappiness(fedRoster(fed,count))-1
   Next
   fedNewPopularity(fed)=fedNewPopularity(fed)+PursueValue(fedPopularity(fed),50,0)
   eFoc(no_events)=1
  EndIf
  ;reputation changes
  If event=215 Then fedNewReputation(fed)=fedNewReputation(fed)+PursueValue(fedReputation(fed),100,0) : eFoc(no_events)=1 ;reputation increase
  If event=216 Then fedNewReputation(fed)=fedNewReputation(fed)+PursueValue(fedReputation(fed),30,0) : eFoc(no_events)=1 ;reputation decrease
  ;whole industry gets more popular
  If event=217
   charNewPopularity(char)=charNewPopularity(char)+1
   charNewHappiness(char)=charNewHappiness(char)+1
   For v=1 To no_chars
    If charFed(v)=<7 And v<>char
     charPopularity(v)=charPopularity(v)+1
     charHappiness(v)=charHappiness(v)+1
    EndIf
   Next  
   For count=1 To 6
    fedPopularity(count)=fedPopularity(count)+1
   Next
  EndIf
  ;whole industry gets less popular
  If event=218 
   charNewPopularity(char)=charNewPopularity(char)-1
   charNewHappiness(char)=charNewHappiness(char)-1
   For v=1 To no_chars
    If charFed(v)=<7 And v<>char
     charPopularity(v)=charPopularity(v)-1
     charHappiness(v)=charHappiness(v)-1
    EndIf
   Next  
   For count=1 To 6
    fedPopularity(count)=fedPopularity(count)-1
   Next
  EndIf
  ;schedule issues
  If event=230 Then gamSchedule(gamDate)=0 ;last minute cancellation
  If event=231 Then gamSchedule(gamDate+1)=0 ;new week cancellation
  If event=232 Then gamSchedule(gamDate+4)=0 ;planned break
  ;royal visit
  If event=233 
   date=gamDate+Rnd(1,4)
   eVariable(no_events)=date
   gamSchedule(date)=2
   gamMatch(date)=14 : gamPromo(date)=32
   Repeat
    gamOpponent(date)=Rnd(1,no_chars)
   Until charFed(gamOpponent(date))=<6 And charRole(gamOpponent(date))=1 And InjuryStatus(gamOpponent(date))=0
   eChar(no_events)=gamOpponent(date)
  EndIf
  ;memorial show
  If event=234
   gamSchedule(gamDate+1)=6 : gamOpponent(gamDate+1)=0
   If gamDate=48 Then gamSchedule(gamDate)=6 : gamOpponent(gamDate)=0
  EndIf
  ;PPV tour
  If event=235
   gamSchedule(gamDate+4)=2 : gamSchedule(gamDate+5)=2 : gamSchedule(gamDate+6)=2
   If gamSchedule(gamDate+3)=2 Then gamSchedule(gamDate+3)=1
   If gamSchedule(gamDate+7)=2 Then gamSchedule(gamDate+7)=1
  EndIf
  ;tournaments
  If charPartner(gamChar)>0 Then teams=1 Else teams=0
  If event=236 
   size=8
   If teams=1 And fedSize(fed)<20 Then size=4
   If fedSize(fed)<10 Then size=4 
   GenerateTournament(fed,teams,0,size,1,gamDate+4,1)
  EndIf
  If event=237 
   If teams=1
    If fedSize(fed)<20 Then size=4 Else size=8
   Else
    If fedSize(fed)<20 Then size=8 Else size=16
   EndIf
   GenerateTournament(fed,teams,0,size,1,gamDate+4,0)
  EndIf 
  If event=238 Then GenerateTournament(0,teams,0,32,1,gamDate+4,0)
  ;2-way inter-promotional
  If event=239
   Repeat
    rival=Rnd(1,6)
   Until rival<>fed
   eVariable(no_events)=rival
   gamSchedule(gamDate+4)=4 : gamRivalFed(gamDate+4)=rival
  EndIf 
  ;charity event
  If event=240 Then gamSchedule(gamDate+4)=5 : gamOpponent(gamDate+4)=0
  ;6-way inter-promotional
  If event=241 Then gamSchedule(gamDate+4)=4 : gamRivalFed(gamDate+4)=0
  ;PREPARE TO INDICATE CHANGES
  If char>0
   CheckNewValues(char)
   If charNewHappiness(char)<>charHappiness(char) Then charOldHappiness(char)=charHappiness(char) : eFoc(no_events)=1 
   If charNewAttitude(char)<>charAttitude(char) Then charOldAttitude(char)=charAttitude(char) : eFoc(no_events)=1
   If charNewToughness(char)<>charToughness(char) Then charOldToughness(char)=charToughness(char) : eFoc(no_events)=1
   If charNewStamina(char)<>charStamina(char) Then charOldStamina(char)=charStamina(char) : eFoc(no_events)=1
   If charNewAgility(char)<>charAgility(char) Then charOldAgility(char)=charAgility(char) : eFoc(no_events)=1
   If charNewSkill(char)<>charSkill(char) Then charOldSkill(char)=charSkill(char) : eFoc(no_events)=1
   If charNewStrength(char)<>charStrength(char) Then charOldStrength(char)=charStrength(char) : eFoc(no_events)=1
   If charNewPopularity(char)<>charPopularity(char) Then charOldPopularity(char)=charPopularity(char) : eFoc(no_events)=1
   If charNewHealth(char)<>charHealth(char) Then eFoc(no_events)=1
  EndIf
 EndIf
End Function

;RESET EVENT
Function ResetEvent(event)
 eEvent(event)=0 : eChar(event)=0 : eFoc(event)=10
 eFed(event)=0 : eVariable(event)=0 : eCharged(event)=0
End Function

;----------------------------------------------------------------------
;/////////////////////// 23. NEWS PROCESS /////////////////////////////
;----------------------------------------------------------------------
Function NewsReports()
;prepare events
FindEvents()
;frame rating
timer=CreateTimer(30)
;MAIN LOOP
foc=eFoc(1) : cyc=1
go=0 : gotim=-10 : keytim=0
While go=0
 
 Cls
 frames=WaitTimer(timer)
 For framer=1 To frames
	
	;counters
	keytim=keytim-1
	If keytim<1 Then keytim=0 
	
	;PROCESS
    gotim=gotim+1
	If gotim>30 
	 ;alter states
	 If eChar(cyc)=0 Then AlterFedStats(eFed(cyc)) 
	 If eChar(cyc)>0 Then AlterStats(eChar(cyc))
	 ;browse events
	 If KeyDown(1) Or KeyDown(28) Or ButtonPressed() Or MouseDown(1)
	  If keytim=0
	   If foc=10 
	    PlaySound sMenuBrowse : gotim=-10
	    cyc=cyc+1 : foc=eFoc(cyc) : keytim=10
	    If eChar(cyc)>0 And charUpdated(eChar(cyc))>0 Then foc=10
	   EndIf
	   If cyc>no_events Then cyc=no_events : foc=10 : go=1
	  Else
	   If foc<10 And keytim>1 Then keytim=1
	  EndIf
	 EndIf 
	EndIf 
	;paper impact
	If gotim=0 Then ProduceSound(0,sPaper,0,0.25)
	;music
	ManageMusic(-1) 
	
 UpdateWorld
 Next 
 RenderWorld 1

 ;DISPLAY
 DrawImage gBackground,rX#(400),rY#(300)
 If eFed(cyc)>0
  DrawImage gFed(eFed(cyc)),rX#(400),rY#(60)  
 Else
  DrawImage gLogo(2),rX#(400),rY#(60)  
 EndIf
 ;show profiles
 char=eChar(cyc)
 If char>0 
  If gotim>20 Then HighlightStats(char)
  If gotim>10 Then DrawProfile(char,-1,-1,0)
 Else
  If eFed(cyc)>0
   If gotim>20 Then HighlightFedStats(eFed(cyc))
   If gotim>10 Then DrawFedProfile(eFed(cyc),-1,-1)
  EndIf
 EndIf
 ;get context
 showChar=0 : refChar=0
 If char>0 Then showChar=char Else showChar=fedBooker(eFed(cyc))
 g=charGender(char)
 ;CONSTRUCT NEWSPAPER
 If gotim>0
  ;graphics
  x=rX#(400) : y=rY#(370)
  DrawImage gNewspaper,x,y
  DrawImage gNewsIdentity(eNewspaper(cyc)),x-158,y-165
  DrawImage gNewsAdvert(eAdvert(cyc,1)),x+42,y-165
  DrawImage gNewsAdvert(eAdvert(cyc,2)),x+201,y-165
  DrawImage gNewsScene(eBackground(cyc)),x+146,y+40 
  ;smallprint
  SetFont fontNews(0) : Color 110,110,110
  Text x-268,y-107,"The Rag",0,1
  Text x+40,y-107,DescribeDate$(gamDate,gamYear),1,1
  Text x+235,y-107,"Cheaper Then Toilet Paper",0,1
  ;default message
  SetFont fontNews(3) : Color 0,0,0
  If eEvent(cyc)=0
   headline$="CALM BEFORE THE STORM"
   Text x+5,y+150,"Nothing has happened in the world of wrestling...",1,1
  EndIf
  ;health issues
  If eEvent(cyc)=1 And IdentifyInjury(char)=0
   headline$="RISKY BUSINESS"
   Text x+5,y+138,charName$(char)+" incurred a minor injury in a recent match",1,1
   Text x+5,y+166,"and will be out of action for the next "+InjuryStatus(char)+" weeks...",1,1
  EndIf
  If eEvent(cyc)=1 And IdentifyInjury(char)>0
   headline$="RISKY BUSINESS"
   Text x+5,y+138,charName$(char)+" incurred a slight "+textInjury$(IdentifyInjury(char))+" injury in a recent",1,1
   Text x+5,y+166,"match and will be suffering with it for the next "+charInjured(char,IdentifyInjury(char))+" weeks...",1,1
  EndIf
  If eEvent(cyc)=2 And IdentifyInjury(char)=0
   headline$="RISKY BUSINESS"
   Text x+5,y+125,charName$(char)+" sustained a serious injury in a recent",1,1
   Text x+5,y+150,"match and will be hospitalized for the next "+InjuryStatus(char)+" weeks.",1,1
   Text x+5,y+175,"Even then, "+Lower$(He$(g))+" may be a shadow of "+Lower$(His$(g))+" former self...",1,1
  EndIf
  If eEvent(cyc)=2 And IdentifyInjury(char)>0
   headline$="RISKY BUSINESS"
   Text x+5,y+125,charName$(char)+" sustained a serious "+textInjury$(IdentifyInjury(char))+" injury in a recent",1,1
   Text x+5,y+150,"match and will be suffering with it for the next "+charInjured(char,IdentifyInjury(char))+" weeks.",1,1
   Text x+5,y+175,"Even then, "+Lower$(He$(g))+" may be a shadow of "+Lower$(His$(g))+" former self...",1,1
  EndIf
  If eEvent(cyc)=3
   headline$="CASUALTY OF WAR"
   Text x+5,y+125,charName$(char)+" sustained a serious injury in a recent match.",1,1
   Text x+5,y+150,He$(g)+" was rushed to hospital, where it's feared "+Lower$(He$(g))+" may be paralyzed!",1,1
   Text x+5,y+175,His$(g)+" wrestling career is almost certainly over...",1,1
   ChannelPitch chTheme,PercentOf#(chThemePitch,90)
  EndIf
  If eEvent(cyc)=4
   headline$="RING OF DEATH" 
   Text x+5,y+125,charName$(char)+" sustained a serious injury in a recent match.",1,1
   Text x+5,y+150,"Despite their best efforts, medics were not able to resuscitate "+Lower$(Him$(g))+".",1,1
   Text x+5,y+175,charName$(char)+" has died as a result of "+Lower$(His$(g))+" injuries. "+He$(g)+" was "+charAge(char)+"...",1,1                
   ChannelPitch chTheme,PercentOf#(chThemePitch,90)
  EndIf
  If eEvent(cyc)=5 And IdentifyInjury(char)=0
   headline$="BROKEN BONES"
   Text x+5,y+138,charName$(char)+" incurred a minor injury in an accident",1,1
   Text x+5,y+166,"and will be out of action for the next "+InjuryStatus(char)+" weeks...",1,1
  EndIf
  If eEvent(cyc)=5 And IdentifyInjury(char)>0
   headline$="RISKY BUSINESS"
   Text x+5,y+138,charName$(char)+" incurred a slight "+textInjury$(IdentifyInjury(char))+" injury in an",1,1
   Text x+5,y+166,"accident and will be suffering with it for the next "+charInjured(char,IdentifyInjury(char))+" weeks...",1,1
  EndIf
  If eEvent(cyc)=6 And IdentifyInjury(char)=0
   headline$="BROKEN BONES"
   Text x+5,y+125,charName$(char)+" sustained a serious injury in an accident",1,1
   Text x+5,y+150,"and will be hospitalized for the next "+InjuryStatus(char)+" weeks.",1,1
   Text x+5,y+175,"Even then, "+Lower$(He$(g))+" may be a shadow of "+Lower$(His$(g))+" former self...",1,1
  EndIf
  If eEvent(cyc)=6 And IdentifyInjury(char)>0
   headline$="BROKEN BONES"
   Text x+5,y+125,charName$(char)+" sustained a serious "+textInjury$(IdentifyInjury(char))+" injury in an",1,1
   Text x+5,y+150,"accident and will be suffering with it for the next "+charInjured(char,IdentifyInjury(char))+" weeks.",1,1
   Text x+5,y+175,"Even then, "+Lower$(He$(g))+" may be a shadow of "+Lower$(His$(g))+" former self...",1,1
  EndIf
  If eEvent(cyc)=7
   headline$="WRESTLER PARALYZED"
   Text x+5,y+125,charName$(char)+" was involved in a horrific accident",1,1
   Text x+5,y+150,"this week and doctors fear "+Lower$(He$(g))+" may never walk again!",1,1
   Text x+5,y+175,His$(g)+" wrestling career is almost certainly over...",1,1
   ChannelPitch chTheme,PercentOf#(chThemePitch,90)
  EndIf
  If eEvent(cyc)=8
   headline$="WRESTLER KILLED"
   Text x+5,y+125,charName$(char)+" was involved in a horrific accident",1,1
   Text x+5,y+150,"this week and later died in hospital. The "+charAge(char)+" year",1,1
   Text x+5,y+175,"old will be greatly missed by the world of wrestling...",1,1                   
   ChannelPitch chTheme,PercentOf#(chThemePitch,90)
  EndIf
  If eEvent(cyc)=9
   headline$="OVERDOSE TRAGEDY"
   Text x+5,y+125,charName$(char)+" was found dead at "+Lower$(His$(g))+" home last night.",1,1
   Text x+5,y+150,"The hedonistic "+charAge(char)+" year old's life is thought to have",1,1
   Text x+5,y+175,"been claimed by a lethal cocktail of drink and drugs...",1,1
   ChannelPitch chTheme,PercentOf#(chThemePitch,90)
  EndIf
  If eEvent(cyc)=10
   headline$="FIGHT TO THE DEATH"
   Text x+5,y+125,charName$(char)+" was found dead at "+Lower$(His$(g))+" home last night.",1,1
   Text x+5,y+150,"His death is thought to be linked to a dispute with",1,1
   Text x+5,y+175,charName$(eVariable(cyc))+", who is now under investigation...",1,1
   ChannelPitch chTheme,PercentOf#(chThemePitch,90)
  EndIf
  If eEvent(cyc)=11
   headline$="SUICIDE SHOCK"
   Text x+5,y+125,charName$(char)+" was found dead at "+Lower$(His$(g))+" home last night.",1,1
   Text x+5,y+150,"The troubled "+charAge(char)+" year old is thought to have committed suicide.",1,1
   Text x+5,y+175,His$(g)+" fleeting contribution to wrestling will be cherished...",1,1
   ChannelPitch chTheme,PercentOf#(chThemePitch,90)
  EndIf
  If eEvent(cyc)=12
   headline$="LEGEND PASSES AWAY"
   Text x+5,y+125,charName$(char)+" was found dead at "+Lower$(His$(g))+" home last night.",1,1
   Text x+5,y+150,"The "+charAge(char)+" year old is thought to have died of natural causes.",1,1
   Text x+5,y+175,His$(g)+" contribution to wrestling will never be forgotten...",1,1
   ChannelPitch chTheme,PercentOf#(chThemePitch,90)
  EndIf
  If eEvent(cyc)=13
   headline$="ROAD TO RECOVERY"
   Text x+5,y+138,charName$(char)+" has recovered from "+Lower$(His$(g))+" injury",1,1
   Text x+5,y+166,"and should now return to active competition...",1,1
  EndIf
  ;contract issues
  If eEvent(cyc)=20
   headline$="THE END IS NEAR"
   Text x+5,y+138,"Your contract with "+fedName$(eFed(cyc))+" expires next week!",1,1
   Text x+5,y+166,"It may be time to re-evaluate your prospects...",1,1
  EndIf
  If eEvent(cyc)=21
   headline$="JUDGMENT DAY"
   Text x+5,y+138,"Your contract with "+fedName$(eFed(cyc))+" has expired!",1,1
   Text x+5,y+166,"There'll be a meeting later to discuss your future...",1,1
  EndIf
  If eEvent(cyc)=22
   headline$="THE SAGA CONTINUES"
   Text x+5,y+138,charName$(char)+" has renewed "+Lower$(His$(g))+" contract with "+fedName$(eFed(cyc)),1,1
   Text x+5,y+166,"and will remain on the roster for the next "+charContract(char)+" weeks...",1,1
  EndIf
  If eEvent(cyc)=23
   headline$="MOVING ON"
   Text x+5,y+138,charName$(char)+" has left "+fedName$(eFed(cyc)),1,1
   Text x+5,y+166,"to pursue a career at "+fedName$(charFed(char))+"...",1,1
  EndIf
  If eEvent(cyc)=24
   headline$="BACK TO BASICS"
   Text x+5,y+138,charName$(char)+" has been released from "+fedName$(eFed(cyc)),1,1
   Text x+5,y+166,"and must make ends meet on the independent circuit...",1,1
  EndIf
  If eEvent(cyc)=25
   headline$="END OF THE ROAD"
   Text x+5,y+138,"At the age of "+charAge(char)+", "+charName$(char)+" has retired from",1,1
   Text x+5,y+166,"the wrestling business to pursue other ambitions...",1,1
  EndIf
  If eEvent(cyc)=26
   headline$="HOT PROPERTY"
   Text x+5,y+138,fedName$(eFed(cyc))+" have broken the bank to",1,1
   Text x+5,y+166,"sign "+charName$(char)+" from "+fedName$(eVariable(cyc))+"!",1,1
  EndIf
  If eEvent(cyc)=27
   headline$="BACK TO BUSINESS"
   Text x+5,y+138,charName$(char)+" has returned from "+Lower$(His$(g))+" absence",1,1
   Text x+5,y+166,"and should now return to active competition...",1,1
  EndIf
  If eEvent(cyc)=28 And charFed(eVariable(cyc))=charFed(char)
   headline$="WE MEET AGAIN"
   Text x+5,y+138,charName$(char)+" is pleased that "+charName$(eVariable(cyc)),1,1
   Text x+5,y+166,"has joined "+fedName$(charFed(char))+"!",1,1
   refChar=eVariable(cyc)
  EndIf
  If eEvent(cyc)=28 And charFed(eVariable(cyc))<>charFed(char)
   headline$="GOOD RIDDANCE"
   Text x+5,y+138,charName$(char)+" is glad that "+charName$(eVariable(cyc)),1,1
   Text x+5,y+166,"has left "+fedName$(charFed(char))+"!",1,1
   refChar=eVariable(cyc)
  EndIf
  If eEvent(cyc)=29 And charFed(eVariable(cyc))=charFed(char)
   headline$="ENEMY AT THE GATE"
   Text x+5,y+138,charName$(char)+" is upset that "+charName$(eVariable(cyc)),1,1
   Text x+5,y+166,"has joined "+fedName$(charFed(char))+"...",1,1
   refChar=eVariable(cyc)
  EndIf
  If eEvent(cyc)=29 And charFed(eVariable(cyc))<>charFed(char)
   headline$="WISH YOU WERE HERE"
   Text x+5,y+138,charName$(char)+" is sad that "+charName$(eVariable(cyc)),1,1
   Text x+5,y+166,"has left "+fedName$(charFed(char))+"...",1,1
   refChar=eVariable(cyc)
  EndIf
  If eEvent(cyc)=31
   headline$="FRESH MEAT"
   If eFed(cyc)=7
    Text x+5,y+125,"A new wrestler called '"+charName$(char)+"' has",1,1
    Text x+5,y+150,"started training on the independent scene.",1,1
    Text x+5,y+175,He$(g)+" will be unveiled to the press later...",1,1
   Else
    Text x+5,y+125,"A new wrestler called '"+charName$(char)+"' has",1,1
    Text x+5,y+150,"risen through the ranks of "+fedName$(eFed(cyc))+".",1,1
    Text x+5,y+175,He$(g)+" will be unveiled to the press later...",1,1
   EndIf
  EndIf
  ;status issues
  If eEvent(cyc)=40
   headline$="CHAMP STRIPPED"
   Text x+5,y+138,charName$(char)+" has been stripped of the World",1,1
   Text x+5,y+166,"Championship due to being unable to defend it!",1,1
  EndIf
  If eEvent(cyc)=41
   headline$="CHAMP STRIPPED"
   Text x+5,y+138,charName$(char)+" has been stripped of the Inter",1,1
   Text x+5,y+166,"Championship due to being unable to defend it!",1,1
  EndIf
  If eEvent(cyc)=42
   headline$="CHAMPS STRIPPED"
   Text x+5,y+138,charTeamName$(char)+" have been stripped of the Tag",1,1
   Text x+5,y+166,"Championships due to being unable to defend them!",1,1
  EndIf
  If eEvent(cyc)=43
   If eCharged(cyc)=0 Then ProduceSound(0,sCrowd(9),0,0.5) : eCharged(cyc)=1
   headline$="NEW WORLD CHAMP"
   Text x+5,y+138,charName$(char)+" has defeated "+charName$(fedOldChampWorld(eFed(cyc)))+" to become",1,1
   Text x+5,y+166,"the new World Champion of "+fedName$(eFed(cyc))+"!",1,1
  EndIf
  If eEvent(cyc)=44
   If eCharged(cyc)=0 Then ProduceSound(0,sCrowd(9),0,0.25) : eCharged(cyc)=1
   headline$="NEW INTER CHAMP"
   Text x+5,y+138,charName$(char)+" has defeated "+charName$(fedOldChampInter(eFed(cyc)))+" to become",1,1
   Text x+5,y+166,"the new Inter Champion of "+fedName$(eFed(cyc))+"!",1,1
  EndIf

;new belts
If eEvent(cyc)=242
   If eCharged(cyc)=0 Then ProduceSound(0,sCrowd(9),0,0.25) : eCharged(cyc)=1
   headline$="NEW Womens CHAMP"
   Text x+5,y+138,charName$(char)+" has defeated "+charName$(fedOldChampInter(eFed(cyc)))+" to become",1,1
   Text x+5,y+166,"the new Womens Champion of "+fedName$(eFed(cyc))+"!",1,1
  EndIf

If eEvent(cyc)=243
   If eCharged(cyc)=0 Then ProduceSound(0,sCrowd(9),0,0.25) : eCharged(cyc)=1
   headline$="NEW US CHAMP"
   Text x+5,y+138,charName$(char)+" has defeated "+charName$(fedOldChampInter(eFed(cyc)))+" to become",1,1
   Text x+5,y+166,"the new US Champion of "+fedName$(eFed(cyc))+"!",1,1
  EndIf

If eEvent(cyc)=244
   If eCharged(cyc)=0 Then ProduceSound(0,sCrowd(9),0,0.25) : eCharged(cyc)=1
   headline$="NEW TV CHAMP"
   Text x+5,y+138,charName$(char)+" has defeated "+charName$(fedOldChampInter(eFed(cyc)))+" to become",1,1
   Text x+5,y+166,"the new TV Champion of "+fedName$(eFed(cyc))+"!",1,1
  EndIf


;end


  If eEvent(cyc)=45
   If eCharged(cyc)=0 Then ProduceSound(0,sCrowd(9),0,0.25) : eCharged(cyc)=1
   headline$="NEW TAG CHAMPS"
   Text x+5,y+138,charTeamName$(char)+" have defeated "+charTeamName$(fedOldChampWorld(eFed(cyc)))+" to",1,1
   Text x+5,y+166,"become the new Tag Champions of "+fedName$(eFed(cyc))+"!",1,1
  EndIf
  If eEvent(cyc)=46
   If eCharged(cyc)=0 Then ProduceSound(0,sCrowd(9),0,0.5) : eCharged(cyc)=1
   headline$="BEST OF THE BEST"
   If cupTeams(cupSlot)>0 
    Text x+5,y+138,charTeamName$(char)+" have won the tournament after",1,1
    Text x+5,y+166,"disposing of "+charTeamName$(charOpponent(char))+" in the final!",1,1
   Else
    Text x+5,y+138,charName$(char)+" has won the tournament after",1,1
    Text x+5,y+166,"disposing of "+charName$(charOpponent(char))+" in the final!",1,1
   EndIf
  EndIf
  If eEvent(cyc)=47
   headline$="BLAZE OF GLORY"
   If cupTeams(cupSlot)>0
    Text x+5,y+138,charTeamName$(char)+" are through to the next round of",1,1
    Text x+5,y+166,"the tournament after defeating "+charTeamName$(charOpponent(char))+"...",1,1
   Else
    Text x+5,y+138,charName$(char)+" is through to the next round of",1,1
    Text x+5,y+166,"the tournament after defeating "+charName$(charOpponent(char))+"...",1,1
   EndIf
  EndIf
  If eEvent(cyc)=48
   headline$="END OF THE ROAD"
   If cupTeams(cupSlot)>0
    Text x+5,y+138,charTeamName$(char)+" have crashed out of the",1,1
    Text x+5,y+166,"tournament after losing to "+charTeamName$(charOpponent(char))+"...",1,1
   Else
    Text x+5,y+138,charName$(char)+" has crashed out of the",1,1
    Text x+5,y+166,"tournament after losing to "+charName$(charOpponent(char))+"...",1,1
   EndIf
  EndIf
  ;misc events
  If eEvent(cyc)=60 And eVariable(cyc)=0
   headline$="HEALTH COMPLICATIONS"
   Text x+5,y+138,charName$(char)+"'s injury seems to have worsened",1,1
   Text x+5,y+166,"and will now take "+charInjured(char,eVariable(cyc))+" weeks to treat...",1,1
  EndIf
  If eEvent(cyc)=60 And eVariable(cyc)>0
   headline$="HEALTH COMPLICATIONS"
   Text x+5,y+138,charName$(char)+"'s "+textInjury$(eVariable(cyc))+" injury has gotten worse",1,1
   Text x+5,y+166,"and won't be better for another "+charInjured(char,eVariable(cyc))+" weeks yet...",1,1
  EndIf
  If eEvent(cyc)=61 And eVariable(cyc)=0
   headline$="MIRACLE RECOVERY"
   Text x+5,y+138,charName$(char)+"'s injury isn't as bad as first",1,1
   If charInjured(char,eVariable(cyc))=<1 Then Text x+5,y+166,"thought and should be healed by next week!",1,1 
   If charInjured(char,eVariable(cyc))>1 Then Text x+5,y+166,"thought and should heal in just "+charInjured(char,eVariable(cyc))+" weeks!",1,1
  EndIf
  If eEvent(cyc)=61 And eVariable(cyc)>0
   headline$="MIRACLE RECOVERY"
   Text x+5,y+138,charName$(char)+"'s "+textInjury$(eVariable(cyc))+" injury is healing nicely",1,1
   If charInjured(char,eVariable(cyc))=<1 Then Text x+5,y+166,"and should now be better by next week!",1,1
   If charInjured(char,eVariable(cyc))>1 Then Text x+5,y+166,"and should now be better in just "+charInjured(char,eVariable(cyc))+" weeks!",1,1
  EndIf
  If eEvent(cyc)=62
   headline$="HEALTH SCARE"
   Text x+5,y+138,charName$(char)+"'s health has been sapped",1,1
   Text x+5,y+166,"by the symptoms of a mild illness...",1,1
  EndIf
  If eEvent(cyc)=63
   headline$="FIGHTING FIT"
   Text x+5,y+138,charName$(char)+" seems to be feeling great",1,1
   Text x+5,y+166,"today and is in perfect health!",1,1
  EndIf
  If eEvent(cyc)=64
   headline$="LARGER THAN LIFE"
   Text x+5,y+138,charName$(char)+" seems to have gained weight!",1,1
   Text x+5,y+166,"The "+charAge(char)+" year old now weighs in at "+TranslateWeight(char)+"lbs...",1,1 
  EndIf
  If eEvent(cyc)=65
   headline$="SHEDDING THE POUNDS"
   Text x+5,y+138,charName$(char)+" seems to have lost weight!",1,1
   Text x+5,y+166,"The "+charAge(char)+" year old now weighs in at "+TranslateWeight(char)+"lbs...",1,1
  EndIf
  If eEvent(cyc)=66
   headline$="GROWING PAINS"
   Text x+5,y+138,charName$(char)+" seems to have grown taller!",1,1
   Text x+5,y+166,"The "+charAge(char)+" year old now stands at "+GetHeight$(charHeight(char))+"...",1,1
  EndIf
  If eEvent(cyc)=67
   headline$="OVER THE HILL"
   Text x+5,y+138,"At the age of "+charAge(char)+", "+charName$(char)+" has reached",1,1
   Text x+5,y+166,Lower$(His$(g))+" physical peak. "+He$(g)+"'s as good as "+Lower$(He$(g))+"'ll ever be...",1,1
  EndIf 
  ;stat fluctuations
  If eEvent(cyc)=68
   headline$="RISING STAR"
   Text x+5,y+138,charName$(char)+"'s profile has risen",1,1
   Text x+5,y+166,"after being hyped by the press!",1,1
  EndIf
  If eEvent(cyc)=69
   headline$="MOUNTAIN OF MUSCLE"
   Text x+5,y+138,charName$(char)+"'s strength seems to have",1,1
   Text x+5,y+166,"improved considerably in recent weeks!",1,1
  EndIf
  If eEvent(cyc)=70
   headline$="SAFE HANDS"
   Text x+5,y+138,charName$(char)+"'s technical ability seems to have",1,1
   Text x+5,y+166,"improved considerably in recent weeks!",1,1
  EndIf
  If eEvent(cyc)=71
   headline$="FANCY FOOTWORK"
   Text x+5,y+138,charName$(char)+"'s agility seems to have",1,1
   Text x+5,y+166,"improved considerably in recent weeks!",1,1
  EndIf
  If eEvent(cyc)=72
   headline$="SETTING THE PACE"
   Text x+5,y+138,charName$(char)+"'s fitness seems to have",1,1
   Text x+5,y+166,"improved considerably in recent weeks!",1,1
  EndIf
  If eEvent(cyc)=73
   headline$="IRON MAN"
   Text x+5,y+138,charName$(char)+"'s toughness seems to have",1,1
   Text x+5,y+166,"improved considerably in recent weeks!",1,1
  EndIf
  If eEvent(cyc)=74
   headline$="EMPLOYEE OF THE MONTH"
   Text x+5,y+138,charName$(char)+"'s attitude seems to have",1,1
   Text x+5,y+166,"improved considerably in recent weeks!",1,1
  EndIf
  If eEvent(cyc)=75
   headline$="JOB SATISFACTION"
   Text x+5,y+138,charName$(char)+" has been enjoying "+Lower$(His$(g))+" role",1,1
   Text x+5,y+166,"at "+fedName$(eFed(cyc))+" in recent weeks!",1,1
  EndIf
  If eEvent(cyc)=76
   headline$="FADING STAR"
   Text x+5,y+138,charName$(char)+"'s profile has suffered",1,1
   Text x+5,y+166,"after being criticized by the press...",1,1
  EndIf
  If eEvent(cyc)=77
   headline$="PENCIL NECK GEEK"
   Text x+5,y+138,charName$(char)+"'s strength seems to have",1,1
   Text x+5,y+166,"deteriorated considerably in recent weeks...",1,1
  EndIf
  If eEvent(cyc)=78
   headline$="SLIPPERY FINGERS"
   Text x+5,y+138,charName$(char)+"'s technical ability seems to have",1,1
   Text x+5,y+166,"deteriorated considerably in recent weeks...",1,1
  EndIf
  If eEvent(cyc)=79
   headline$="TWO LEFT FEET"
   Text x+5,y+138,charName$(char)+"'s agility seems to have",1,1
   Text x+5,y+166,"deteriorated considerably in recent weeks...",1,1
  EndIf
  If eEvent(cyc)=80
   headline$="OUT OF SHAPE"
   Text x+5,y+138,charName$(char)+"'s fitness seems to have",1,1
   Text x+5,y+166,"deteriorated considerably in recent weeks...",1,1
  EndIf
  If eEvent(cyc)=81
   headline$="HANDLE WITH CARE"
   Text x+5,y+138,charName$(char)+"'s toughness seems to have",1,1
   Text x+5,y+166,"deteriorated considerably in recent weeks...",1,1
  EndIf
  If eEvent(cyc)=82
   headline$="PROBLEM CHILD"
   Text x+5,y+138,charName$(char)+"'s attitude seems to have",1,1
   Text x+5,y+166,"deteriorated considerably in recent weeks...",1,1
  EndIf
  If eEvent(cyc)=83
   headline$="BAD VIBES"
   Text x+5,y+138,charName$(char)+" seems to be growing weary",1,1
   Text x+5,y+166,"of working for "+fedName$(eFed(cyc))+"...",1,1
  EndIf
  If eEvent(cyc)=84
   If eCharged(cyc)=0 Then ProduceSound(0,sCrowd(3),0,0.5) : eCharged(cyc)=1
   headline$="CAREER KILLER" 
   If gamOpponent(gamDate-1)=char Then v=gamChar Else v=gamOpponent(gamDate-1)
   Text x+5,y+138,charName$(char)+" has been branded negligent for",1,1
   Text x+5,y+166,"injuring "+charName$(v)+" in their recent match!",1,1
   refChar=v
  EndIf
  If eEvent(cyc)=85
   If eCharged(cyc)=0 Then ProduceSound(0,sCrowd(3),0,0.5) : eCharged(cyc)=1
   headline$="IN COLD BLOOD" 
   If gamOpponent(gamDate-1)=char Then v=gamChar Else v=gamOpponent(gamDate-1)
   Text x+5,y+138,"The press have blamed "+charName$(char)+" for",1,1
   Text x+5,y+166,"causing "+charName$(v)+"'s recent death!",1,1
  EndIf 
  If eEvent(cyc)=86
   headline$="THE DRUGS DON'T WORK" 
   Text x+5,y+138,charName$(char)+" has developed a dependency on",1,1
   Text x+5,y+166,"drugs and is suffering from withdrawl symptoms!",1,1
   ChannelPitch chTheme,PercentOf#(chThemePitch,90)
  EndIf 
  If eEvent(cyc)=87
   If eCharged(cyc)=0 Then ProduceSound(0,sCrowd(3),0,0.5) : eCharged(cyc)=1
   headline$="BAD HABIT" 
   Text x+5,y+138,charName$(char)+" has lost the respect of the",1,1
   Text x+5,y+166,"fans after being exposed as a drug user!",1,1
   ChannelPitch chTheme,PercentOf#(chThemePitch,90)
  EndIf 
  If eEvent(cyc)=88
   headline$="KEEPING UP APPEARANCES"
   Text x+5,y+138,charName$(char)+"'s profile has suffered due",1,1
   Text x+5,y+166,"to a lack of exposure in recent weeks...",1,1
  EndIf
  ;relationship issues
  If eEvent(cyc)=100
   headline$="UNDER NEW MANAGEMENT"
   Text x+5,y+138,charName$(char)+" is now wrestling under",1,1
   Text x+5,y+166,"the management of "+charName$(charManager(char))+"...",1,1
  EndIf
  If eEvent(cyc)=101
   headline$="AMICABLE SPLIT"
   Text x+5,y+138,charName$(char)+" has decided to part ways",1,1
   Text x+5,y+166,"with "+Lower$(His$(g))+" manager, "+charName$(eVariable(cyc))+"...",1,1
   refChar=eVariable(cyc)
  EndIf
  If eEvent(cyc)=102
   headline$="MESSY DIVORCE"
   Text x+5,y+138,charName$(char)+" has fallen out with",1,1
   Text x+5,y+166,Lower$(His$(g))+" manager, "+charName$(eVariable(cyc))+"...",1,1
   refChar=eVariable(cyc)
  EndIf
  If eEvent(cyc)=103
   headline$="FRIENDS IN HIGH PLACES"
   Text x+5,y+138,charName$(char)+"'s profile has benefited",1,1
   Text x+5,y+166,"from being associated with "+charName$(charManager(char))+"!",1,1
  EndIf
  If eEvent(cyc)=104
   headline$="BAD COMPANY"
   Text x+5,y+138,charName$(char)+"'s profile has suffered",1,1
   Text x+5,y+166,"from being associated with "+charName$(charManager(char))+"...",1,1
  EndIf
  If eEvent(cyc)=105
   headline$="UNITED FRONT"
   Text x+5,y+138,charName$(char)+" and "+charName$(charPartner(char))+" have",1,1
   Text x+5,y+166,"formed a team called '"+charTeamName$(char)+"'...",1,1
  EndIf
  If eEvent(cyc)=106
   headline$="GROWING APART"
   Text x+5,y+138,charName$(char)+" and "+charName$(eVariable(cyc)),1,1
   Text x+5,y+166,"have agreed to disband their team...",1,1
   refChar=eVariable(cyc)
  EndIf
  If eEvent(cyc)=107
   headline$="DIVIDED WE FALL"
   Text x+5,y+138,charName$(char)+" has fallen out with",1,1
   Text x+5,y+166,Lower$(His$(g))+" team-mate, "+charName$(eVariable(cyc))+"...",1,1
   refChar=eVariable(cyc)
  EndIf 
  If eEvent(cyc)=108
   headline$="FRIENDS IN HIGH PLACES"
   Text x+5,y+138,charName$(char)+"'s profile has benefited",1,1
   Text x+5,y+166,"from being associated with "+charName$(charPartner(char))+"!",1,1
  EndIf
  If eEvent(cyc)=109
   headline$="BAD COMPANY"
   Text x+5,y+138,charName$(char)+"'s profile has suffered",1,1
   Text x+5,y+166,"from being associated with "+charName$(charPartner(char))+"...",1,1
  EndIf
  If eEvent(cyc)=110
   headline$="MUTUAL ADMIRATION"
   Text x+5,y+138,charName$(char)+" has become good friends",1,1
   Text x+5,y+166,"with "+charName$(eVariable(cyc))+"...",1,1
   refChar=eVariable(cyc)
  EndIf
  If eEvent(cyc)=111
   headline$="WAR OF WORDS"
   Text x+5,y+138,charName$(char)+" has embarked on a feud",1,1
   Text x+5,y+166,"with "+charName$(eVariable(cyc))+"...",1,1
   refChar=eVariable(cyc)
  EndIf
  If eEvent(cyc)=112
   headline$="MUTUAL ADMIRATION"
   Text x+5,y+138,charName$(char)+" has developed a good relationship",1,1
   Text x+5,y+166,"with "+charName$(eVariable(cyc))+" of "+fedName$(charFed(eVariable(cyc)))+"...",1,1
   refChar=eVariable(cyc)
  EndIf
  If eEvent(cyc)=113
   headline$="WAR OF WORDS"
   Text x+5,y+138,charName$(char)+" has developed a heated rivalry",1,1
   Text x+5,y+166,"with "+charName$(eVariable(cyc))+" of "+fedName$(charFed(eVariable(cyc)))+"...",1,1
   refChar=eVariable(cyc)
  EndIf
  If eEvent(cyc)=114
   headline$="A NEW LEAF"
   Text x+5,y+138,charName$(char)+" has turned 'Face' and will",1,1
   Text x+5,y+166,"now be portrayed in a positive light...",1,1
  EndIf
  If eEvent(cyc)=115
   headline$="THE DARK SIDE"
   Text x+5,y+138,charName$(char)+" has turned 'Heel' and will",1,1
   Text x+5,y+166,"now be portrayed in a negative light...",1,1
  EndIf
  If eEvent(cyc)=116
   headline$="IDENTITY CRISIS"
   Text x+5,y+138,"The fans insist on treating "+charName$(char),1,1
   Text x+5,y+166,"as a Heel despite "+Lower$(His$(g))+" attempts to play Face!",1,1
  EndIf
  If eEvent(cyc)=117
   headline$="IDENTITY CRISIS"
   Text x+5,y+138,"The fans insist on treating "+charName$(char),1,1
   Text x+5,y+166,"as a Face despite "+Lower$(His$(g))+" attempts to play Heel!",1,1
  EndIf
  If eEvent(cyc)=118
   headline$="BURY THE HATCHET"
   If charFed(eVariable(cyc))=charFed(gamChar)
    Text x+5,y+138,charName$(char)+" has settled "+Lower$(His$(g)),1,1
    Text x+5,y+166,"differences with "+charName$(eVariable(cyc))+"...",1,1
   Else 
    Text x+5,y+138,charName$(char)+" has settled "+Lower$(His$(g))+" differences",1,1
    Text x+5,y+166,"with "+charName$(eVariable(cyc))+" of "+fedName$(charFed(eVariable(cyc)))+"...",1,1
   EndIf
   refChar=eVariable(cyc)
  EndIf 
  If eEvent(cyc)=119
   headline$="TIME HEALS ALL WOUNDS"
   Text x+5,y+138,"After "+eVariable(cyc)+" weeks, your feud with",1,1
   Text x+5,y+166,charName$(char)+" has lost momentum...",1,1
  EndIf
  If eEvent(cyc)=89
   headline$="GROWING APART"
   Text x+5,y+138,"After "+eVariable(cyc)+" weeks, your friendship with",1,1
   Text x+5,y+166,charName$(char)+" has lost its significance...",1,1
  EndIf
  ;schedule issues
  If eEvent(cyc)=120
   headline$="OPPONENT WITHDRAWS" : namer$=charName$(char)
   If charPartner(gamChar)>0 Then headline$="OPPONENTS WITHDRAW" : namer$=charTeamName$(char)
   Text x+5,y+138,"Your match against "+namer$+" on the",1,1
   Text x+5,y+166,DescribeDate$(eVariable(cyc),0)+" has been cancelled...",1,1
  EndIf
  If eEvent(cyc)=121
   headline$="OUT IN THE COLD"
   Text x+5,y+138,charName$(fedBooker(eFed(cyc)))+" has left you off the card",1,1
   Text x+5,y+166,"for tonight's "+textEvent$(eVariable(cyc))+"...",1,1
   ChannelPitch chTheme,PercentOf#(chThemePitch,90)
  EndIf
  If eEvent(cyc)=122
   headline$="KNOW YOUR ENEMY"
   If charPartner(gamChar)>0 Then namer$=charTeamName$(char) Else namer$=charName$(char)
   If gamGimmick(eVariable(cyc))>0
    Text x+5,y+138,"You're booked to face "+namer$+" in a '"+textGimmick$(gamGimmick(eVariable(cyc))),1,1
    Text x+5,y+166,textMatch$(gamMatch(eVariable(cyc)))+"' on the "+DescribeDate$(eVariable(cyc),0)+"!",1,1
   Else
    Text x+5,y+138,"You've been booked to face "+namer$+" in a",1,1
    Text x+5,y+166,"'"+textMatch$(gamMatch(eVariable(cyc)))+"' on the "+DescribeDate$(eVariable(cyc),0)+"!",1,1
   EndIf
  EndIf
  If eEvent(cyc)=123
   headline$="LUCK OF THE DRAW"
   If charPartner(gamChar)>0 Then namer$=charTeamName$(char) Else namer$=charName$(char)
   Text x+5,y+138,"You've been drawn against "+namer$,1,1
   Text x+5,y+166,"in the first round of the tournament!",1,1
  EndIf
  If eEvent(cyc)=124
   headline$="TENSION MOUNTS"
   Text x+5,y+138,"It's a big night for "+fedName$(eFed(cyc)),1,1
   Text x+5,y+166,"next week! Make sure you're ready for it...",1,1
  EndIf
  If eEvent(cyc)=125
   If eCharged(cyc)=0 Then ProduceSound(0,sCrowd(9),0,0.5) : eCharged(cyc)=1
   headline$="HAPPY NEW YEAR!"
   Text x+5,y+138,"A new year is upon us! You are now "+charAge(char)+" years old and",1,1
   Text x+5,y+166,"have been in the wrestling business for "+CountExperience(char,0)+" weeks...",1,1
  EndIf
  If eEvent(cyc)=126
   headline$="THE MOMENT OF TRUTH"
   Text x+5,y+138,"It's a big night for "+fedName$(eFed(cyc))+"!",1,1
   Text x+5,y+166,"Make sure you do the company proud...",1,1
  EndIf
  ;mission reminders
  If eEvent(cyc)=141
   headline$="MISSION IMPOSSIBLE"
   Text x+5,y+138,"Remember you're on a mission to increase your",1,1
   Text x+5,y+166,"strength to "+gamTarget+"% by the "+DescribeDate$(gamDeadline,0)+"!",1,1
  EndIf
  If eEvent(cyc)=142
   headline$="MISSION IMPOSSIBLE"
   Text x+5,y+138,"Remember you're on a mission to increase your",1,1
   Text x+5,y+166,"skill to "+gamTarget+"% by the "+DescribeDate$(gamDeadline,0)+"!",1,1
  EndIf
  If eEvent(cyc)=143
   headline$="MISSION IMPOSSIBLE"
   Text x+5,y+138,"Remember you're on a mission to increase your",1,1
   Text x+5,y+166,"agility to "+gamTarget+"% by the "+DescribeDate$(gamDeadline,0)+"!",1,1
  EndIf
  If eEvent(cyc)=144
   headline$="MISSION IMPOSSIBLE"
   Text x+5,y+138,"Remember you're on a mission to increase your",1,1
   Text x+5,y+166,"stamina to "+gamTarget+"% by the "+DescribeDate$(gamDeadline,0)+"!",1,1
  EndIf
  If eEvent(cyc)=145
   headline$="MISSION IMPOSSIBLE"
   Text x+5,y+138,"Remember you're on a mission to increase your",1,1
   Text x+5,y+166,"toughness to "+gamTarget+"% by the "+DescribeDate$(gamDeadline,0)+"!",1,1
  EndIf
  If eEvent(cyc)=146
   headline$="MISSION IMPOSSIBLE"
   Text x+5,y+138,"Remember you're on a mission to increase your",1,1
   Text x+5,y+166,"popularity to "+gamTarget+"% by the "+DescribeDate$(gamDeadline,0)+"!",1,1
  EndIf
  If eEvent(cyc)=147
   headline$="MISSION IMPOSSIBLE"
   Text x+5,y+138,"Remember you're on a mission to improve your",1,1
   Text x+5,y+166,"attitude to "+gamTarget+"% by the "+DescribeDate$(gamDeadline,0)+"!",1,1
  EndIf
  If eEvent(cyc)=148
   headline$="MISSION IMPOSSIBLE"
   Text x+5,y+138,"Remember you're on a mission to amass a",1,1
   Text x+5,y+166,"fortune of $"+GetFigure$(gamTarget)+" by the "+DescribeDate$(gamDeadline,0)+"!",1,1
  EndIf
  If eEvent(cyc)=149
   headline$="MISSION IMPOSSIBLE"
   Text x+5,y+138,"Remember you're on a mission to get your weight",1,1
   Text x+5,y+166,"down to "+gamTarget+"lbs by the "+DescribeDate$(gamDeadline,0)+"!",1,1
  EndIf
  If eEvent(cyc)=150
   headline$="MISSION IMPOSSIBLE"
   Text x+5,y+138,"Remember you're on a mission to get your weight",1,1
   Text x+5,y+166,"up to "+gamTarget+"lbs by the "+DescribeDate$(gamDeadline,0)+"!",1,1
  EndIf
  If eEvent(cyc)=151
   headline$="MISSION IMPOSSIBLE"
   Text x+5,y+138,"Remember you're on a mission to win",1,1
   Text x+5,y+166,"a title by the "+DescribeDate$(gamDeadline,0)+"!",1,1
  EndIf
  If eEvent(cyc)=152
   headline$="MISSION IMPOSSIBLE"
   Text x+5,y+138,"Remember you're on a mission to sign with",1,1
   Text x+5,y+166,"a major promotion by the "+DescribeDate$(gamDeadline,0)+"!",1,1
  EndIf
  If eEvent(cyc)=153
   headline$="MISSION IMPOSSIBLE"
   Text x+5,y+138,"Remember you're on a mission to get",1,1
   Text x+5,y+166,"out of debt by the "+DescribeDate$(gamDeadline,0)+"!",1,1
  EndIf
  If eEvent(cyc)=154
   headline$="MISSION IMPOSSIBLE"
   Text x+5,y+138,"Remember you're on a mission to get your",1,1
   Text x+5,y+166,"win rate up to "+gamTarget+"% by the "+DescribeDate$(gamDeadline,0)+"!",1,1
  EndIf
  ;knowledge updates
  If eEvent(cyc)=170
   headline$="LEARNING CURVE"
   If gamNewMoves=1 Then moveDescribe$="move" Else moveDescribe$="moves"
   If gamNewAttacks=1 Then attackDescribe$="attack" Else attackDescribe$="attacks"
   If gamNewTaunts=1 Then tauntDescribe$="gesture" Else tauntDescribe$="gestures"
   If gamNewMoves>0 And gamNewAttacks=0 And gamNewTaunts=0
    Text x+5,y+138,charName$(char)+" has learned "+gamNewMoves,1,1
    Text x+5,y+166,"new "+moveDescribe$+" since last week!",1,1
   EndIf
   If gamNewMoves=0 And gamNewAttacks>0 And gamNewTaunts=0
    Text x+5,y+138,charName$(char)+" has learned "+gamNewAttacks,1,1
    Text x+5,y+166,"new "+attackDescribe$+" since last week!",1,1
   EndIf
   If gamNewMoves=0 And gamNewAttacks=0 And gamNewTaunts>0
    Text x+5,y+138,charName$(char)+" has learned "+gamNewTaunts,1,1
    Text x+5,y+166,"new "+tauntDescribe$+" since last week!",1,1
   EndIf
   If gamNewMoves>0 And gamNewAttacks>0 And gamNewTaunts=0
    Text x+5,y+138,charName$(char)+" has learned "+gamNewMoves+" new "+moveDescribe$,1,1
    Text x+5,y+166,"and "+gamNewAttacks+" new "+attackDescribe$+" since last week!",1,1
   EndIf
   If gamNewMoves>0 And gamNewAttacks=0 And gamNewTaunts>0
    Text x+5,y+138,charName$(char)+" has learned "+gamNewMoves+" new "+moveDescribe$,1,1
    Text x+5,y+166,"and "+gamNewTaunts+" new "+tauntDescribe$+" since last week!",1,1
   EndIf
   If gamNewMoves=0 And gamNewAttacks>0 And gamNewTaunts>0
    Text x+5,y+138,charName$(char)+" has learned "+gamNewAttacks+" new "+attackDescribe$,1,1
    Text x+5,y+166,"and "+gamNewTaunts+" new "+tauntDescribe$+" since last week!",1,1
   EndIf
   If gamNewMoves>0 And gamNewAttacks>0 And gamNewTaunts>0
    Text x+5,y+138,charName$(char)+" has learned "+gamNewMoves+" new "+moveDescribe$+",",1,1
    Text x+5,y+166,gamNewAttacks+" new "+attackDescribe$+", and "+gamNewTaunts+" new "+tauntDescribe$+" since last week!",1,1
   EndIf
  EndIf
  If eEvent(cyc)=171
   headline$="FASHION CONSCIOUS"
   If gamNewCostumes=1 Then costumeDescribe$="item" Else costumeDescribe$="items"
   If gamNewMusic=1 Then musicDescribe$="piece" Else musicDescribe$="pieces"
   If gamNewCostumes>0 And gamNewMusic=0
    Text x+5,y+138,charName$(char)+" has discovered "+gamNewCostumes+" new",1,1
    Text x+5,y+166,costumeDescribe$+" of clothing since last week!",1,1
   EndIf
   If gamNewCostumes=0 And gamNewMusic>0
    Text x+5,y+138,charName$(char)+" has discovered "+gamNewMusic+" new",1,1
    Text x+5,y+166,musicDescribe$+" of music since last week!",1,1
   EndIf
   If gamNewCostumes>0 And gamNewMusic>0
    Text x+5,y+138,charName$(char)+" has discovered "+gamNewCostumes+" new "+costumeDescribe$+" of clothing",1,1
    Text x+5,y+166,"and "+gamNewMusic+" new "+musicDescribe$+" of music since last week!",1,1
   EndIf
  EndIf
  ;promotion issues
  If eEvent(cyc)=200
   If eCharged(cyc)=0 Then ProduceSound(0,sCrowd(9),0,0.5) : eCharged(cyc)=1
   headline$="NEW KING IN TOWN"
   Text x+5,y+138,charName$(char)+" has been appointed as the",1,1
   Text x+5,y+166,"new booker of "+fedName$(eFed(cyc))+"!",1,1
  EndIf
  If eEvent(cyc)=201
   If eCharged(cyc)=0 Then ProduceSound(0,sCrowd(9),0,0.5) : eCharged(cyc)=1
   headline$="NEW WORLD CHAMP"
   Text x+5,y+138,charName$(fedChampWorld(eFed(cyc)))+" has been crowned the new",1,1
   Text x+5,y+166,"World Champion of "+fedName$(eFed(cyc))+"!",1,1
  EndIf
  If eEvent(cyc)=202
   If eCharged(cyc)=0 Then ProduceSound(0,sCrowd(9),0,0.5) : eCharged(cyc)=1
   headline$="NEW INTER CHAMP"
   Text x+5,y+138,charName$(fedChampInter(eFed(cyc)))+" has been crowned the new ",1,1
   Text x+5,y+166,"Inter Champion of "+fedName$(eFed(cyc))+"!",1,1
  EndIf
  If eEvent(cyc)=203
   If eCharged(cyc)=0 Then ProduceSound(0,sCrowd(9),0,0.5) : eCharged(cyc)=1
   headline$="NEW TAG CHAMPS"
   Text x+5,y+138,charName$(fedChampTag(eFed(cyc),1))+" and "+charName$(fedChampTag(eFed(cyc),2))+" have been crowned",1,1
   Text x+5,y+166,"the new Tag Champions of "+fedName$(eFed(cyc))+"!",1,1
  EndIf

;new belts
If eEvent(cyc)=204
   If eCharged(cyc)=0 Then ProduceSound(0,sCrowd(9),0,0.5) : eCharged(cyc)=1
   headline$="NEW WOMENS CHAMP"
   Text x+5,y+138,charName$(fedChampWomens(eFed(cyc)))+" has been crowned the new",1,1
   Text x+5,y+166,"Womens Champion of "+fedName$(eFed(cyc))+"!",1,1
  EndIf

If eEvent(cyc)=205
   If eCharged(cyc)=0 Then ProduceSound(0,sCrowd(9),0,0.5) : eCharged(cyc)=1
   headline$="NEW US CHAMP"
   Text x+5,y+138,charName$(fedChampUS(eFed(cyc)))+" has been crowned the new",1,1
   Text x+5,y+166,"US Champion of "+fedName$(eFed(cyc))+"!",1,1
  EndIf

If eEvent(cyc)=206
   If eCharged(cyc)=0 Then ProduceSound(0,sCrowd(9),0,0.5) : eCharged(cyc)=1
   headline$="NEW TV CHAMP"
   Text x+5,y+138,charName$(fedChampTV(eFed(cyc)))+" has been crowned the new",1,1
   Text x+5,y+166,"TV Champion of "+fedName$(eFed(cyc))+"!",1,1
  EndIf

;end

 
  If eEvent(cyc)=210 And fedRanked(eFed(cyc))=1
   headline$="GREATEST SHOW ON EARTH"
   If eCharged(cyc)=0 Then ProduceSound(0,sCrowd(9),0,0.5) : eCharged(cyc)=1
   Text x+5,y+138,fedName$(eFed(cyc))+" have overtaken "+fedName$(fedOldList(fedRanked(eFed(cyc)))),1,1
   Text x+5,y+166,"as the world's leading wrestling promotion!",1,1
  EndIf
  If eEvent(cyc)=210 And fedRanked(eFed(cyc))>1
   headline$="NEW WORLD ORDER"
   Text x+5,y+138,fedName$(eFed(cyc))+" have overtaken "+fedName$(fedOldList(fedRanked(eFed(cyc)))),1,1
   Text x+5,y+166,"as the world's "+textNumber$(fedRanked(eFed(cyc)))+" biggest wrestling promotion...",1,1
  EndIf
  If eEvent(cyc)=211
   headline$="BAD DRAW"
   Text x+5,y+138,fedName$(eFed(cyc))+"'s profile has suffered since",1,1
   Text x+5,y+166,charName$(fedChampWorld(eFed(cyc)))+" became their World Champion...",1,1
   showChar=fedChampWorld(eFed(cyc))
  EndIf
  If eEvent(cyc)=212
   headline$="MOST VALUABLE PLAYER"
   Text x+5,y+138,fedName$(eFed(cyc))+"'s profile has improved since",1,1
   Text x+5,y+166,charName$(fedChampWorld(eFed(cyc)))+" became their World Champion!",1,1
   showChar=fedChampWorld(eFed(cyc))
  EndIf
  If eEvent(cyc)=213
   headline$="THE NEXT BIG THING"
   Text x+5,y+138,fedName$(eFed(cyc))+"'s product has attracted",1,1
   Text x+5,y+166,"a stronger following in recent weeks!",1,1
  EndIf
  If eEvent(cyc)=214
   headline$="OLD NEWS"
   Text x+5,y+138,fedName$(eFed(cyc))+"'s product has suffered",1,1
   Text x+5,y+166,"a dip in popularity in recent weeks...",1,1
  EndIf
  If eEvent(cyc)=215
   headline$="KEEP IT CLEAN"
   Text x+5,y+138,fedName$(eFed(cyc))+" have been striving",1,1
   Text x+5,y+166,"to deliver a more respectable product...",1,1
  EndIf
  If eEvent(cyc)=216
   headline$="DIRTY MOVES"
   Text x+5,y+138,fedName$(eFed(cyc))+" seem to be intent on",1,1
   Text x+5,y+166,"delivering a more controversial product!",1,1
  EndIf
  If eEvent(cyc)=217
   headline$="THE IN THING"
   Text x+5,y+138,"The whole sport of wrestling seems to have",1,1
   Text x+5,y+166,"captured the imagination of the public!",1,1
  EndIf
  If eEvent(cyc)=218
   headline$="OLD NEWS"
   Text x+5,y+138,"The whole sport of wrestling seems be",1,1
   Text x+5,y+166,"losing its appeal to the outside world...",1,1
  EndIf 
  ;promotion scheduling issues
  If eEvent(cyc)=230 And eFed(cyc)=<6
   headline$="NO SHOW"
   Text x+5,y+138,"The show that "+fedName$(eFed(cyc))+" had",1,1
   Text x+5,y+166,"planned for tonight will not be going ahead...",1,1
   If eCharged(cyc)=0 Then ProduceSound(0,sCrowd(3),0,0.5) : eCharged(cyc)=1
   ChannelPitch chTheme,PercentOf#(chThemePitch,90)
  EndIf
  If eEvent(cyc)=231 And eFed(cyc)=<6
   headline$="NO SHOW"
   Text x+5,y+138,"The show that "+fedName$(eFed(cyc))+" had",1,1
   Text x+5,y+166,"planned next week will not be going ahead...",1,1
   If eCharged(cyc)=0 Then ProduceSound(0,sCrowd(3),0,0.5) : eCharged(cyc)=1
  EndIf
  If eEvent(cyc)=232 And eFed(cyc)=<6
   headline$="DAY OF REST"
   Text x+5,y+138,fedName$(eFed(cyc))+" have scheduled a break",1,1
   Text x+5,y+166,"next month to give wrestlers time to recuperate...",1,1
  EndIf
  If eEvent(cyc)=230 And eFed(cyc)=7
   headline$="NO SHOW"
   Text x+5,y+138,charName$(fedBooker(7))+" has decided not to",1,1
   Text x+5,y+166,"host a training session this week...",1,1
   If eCharged(cyc)=0 Then ProduceSound(0,sCrowd(3),0,0.5) : eCharged(cyc)=1
   ChannelPitch chTheme,PercentOf#(chThemePitch,90)
  EndIf
  If eEvent(cyc)=231 And eFed(cyc)=7
   headline$="NO SHOW"
   Text x+5,y+138,charName$(fedBooker(7))+" has decided not to",1,1
   Text x+5,y+166,"host a training session next week...",1,1
   If eCharged(cyc)=0 Then ProduceSound(0,sCrowd(3),0,0.5) : eCharged(cyc)=1
  EndIf
  If eEvent(cyc)=232 And eFed(cyc)=7
   headline$="DAY OF REST"
   Text x+5,y+138,charName$(fedBooker(7))+" has scheduled a break next",1,1
   Text x+5,y+166,"month to give students time to recuperate...",1,1
  EndIf
  If eEvent(cyc)=233
   headline$="ROYAL VISIT"
   Text x+5,y+138,charName$(char)+" of "+fedName$(charFed(char))+" will",1,1
   Text x+5,y+166,"be hosting a training session next week!",1,1
   If eCharged(cyc)=0 Then ProduceSound(0,sCrowd(6),0,0.5) : eCharged(cyc)=1
  EndIf
  If eEvent(cyc)=234
   headline$="IN LOVING MEMORY"
   Text x+5,y+138,fedName$(eFed(cyc))+" will stage a special memorial",1,1
   Text x+5,y+166,"show next week to mark the death of "+charName$(fedFatality(eFed(cyc)))+"...",1,1
   showChar=fedFatality(eFed(cyc))
   If eCharged(cyc)=0 Then ProduceSound(0,sCrowd(9),0,0.5) : eCharged(cyc)=1
  EndIf
  If eEvent(cyc)=235
   headline$="TOUR ANNOUNCED"
   Text x+5,y+138,fedName$(eFed(cyc))+" will be hitting the road",1,1
   Text x+5,y+166,"next month with a whole series of big shows!",1,1
   If eCharged(cyc)=0 Then ProduceSound(0,sCrowd(6),0,0.5) : eCharged(cyc)=1
  EndIf
  If eEvent(cyc)=236
   headline$="BEST OF THE BEST"
   If charPartner(gamChar)>0 Then namer$="team" Else namer$="wrestler"
   Text x+5,y+138,fedName$(eFed(cyc))+" will stage a small tournament",1,1
   Text x+5,y+166,"next month to uncover their hottest prospect!",1,1
   If eCharged(cyc)=0 Then ProduceSound(0,sCrowd(6),0,0.5) : eCharged(cyc)=1
  EndIf 
  If eEvent(cyc)=237
   headline$="BEST OF THE BEST"
   If charPartner(gamChar)>0 Then namer$="team" Else namer$="wrestler"
   Text x+5,y+138,fedName$(eFed(cyc))+" will stage a huge tournament",1,1
   Text x+5,y+166,"next month to crown their very best "+namer$+"!",1,1
   If eCharged(cyc)=0 Then ProduceSound(0,sCrowd(6),0,0.5) : eCharged(cyc)=1
  EndIf 
  If eEvent(cyc)=238
   headline$="BEST OF THE BEST"
   If charPartner(gamChar)>0 Then namer$="team" Else namer$="wrestler"
   Text x+5,y+138,"An industry-wide tournament will take place next",1,1
   Text x+5,y+166,"month to crown the best "+namer$+" in the world!",1,1
   If eCharged(cyc)=0 Then ProduceSound(0,sCrowd(6),0,0.5) : eCharged(cyc)=1
  EndIf 
  If eEvent(cyc)=239
   headline$="WAR OF THE WORLDS"
   Text x+5,y+138,fedName$(eVariable(cyc))+" have challenged "+fedName$(eFed(cyc)),1,1
   Text x+5,y+166,"to an inter-promotional showdown next month!",1,1
   showChar=fedBooker(eVariable(cyc))
   If eCharged(cyc)=0 Then ProduceSound(0,sCrowd(6),0,0.5) : eCharged(cyc)=1
  EndIf
  If eEvent(cyc)=240
   headline$="TOUGH LOVE"
   Text x+5,y+138,"The wrestling industry will stage a special",1,1
   Text x+5,y+166,"event next month to raise money for charity!",1,1
   If eCharged(cyc)=0 Then ProduceSound(0,sCrowd(6),0,0.5) : eCharged(cyc)=1
  EndIf
  If eEvent(cyc)=241
   headline$="WAR OF THE WORLDS"
   Text x+5,y+138,fedName$(eFed(cyc))+" will take on ALL the other",1,1
   Text x+5,y+166,"promotions in a huge showdown next month!",1,1
   If eCharged(cyc)=0 Then ProduceSound(0,sCrowd(6),0,0.5) : eCharged(cyc)=1
  EndIf
  ;display headline
  SetFont fontNews(10) : Color 0,0,0
  Text x+5,y-63,headline$,1,1
  ;photo display
  If showChar>0
   If refChar>0 Then DrawImage charPhoto(refChar),x+70,y+33
   If charManager(showChar)>0 And charManager(showChar)<>refChar
    If charPartner(showChar)=0 Or charPartner(showChar)=charManager(showChar) Then DrawImage charPhoto(charManager(showChar)),x+125,y+33
   EndIf
   If charPartner(showChar)>0 And charPartner(showChar)<>refChar And charManager(showChar)=0 Then DrawImage charPhoto(charPartner(showChar)),x+125,y+33
   If charManager(showChar)>0 And charManager(showChar)<>refChar And charPartner(showChar)>0 And charPartner(showChar)<>refChar And charPartner(showChar)<>charManager(showChar)
    DrawImage charPhoto(charManager(showChar)),x+115,y+32
    DrawImage charPhoto(charPartner(showChar)),x+130,y+33
   EndIf
   DrawImage charPhoto(showChar),x+145,y+34
  EndIf
  ;prompt
  If foc=10 And gotim>20
   SetFont font(2)
   Outline(">>> PRESS ANY COMMAND TO PROCEED >>>",x,y+210,100,100,100,255,255,255)
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
For fed=1 To 9
 fedFatality(fed)=0
Next
gamFatality=0
 PlaySound sMenuGo
 ;introduce new characters
 If gamNewcomer>0
  editChar=gamNewcomer
  screen=51 : screenAgenda=9 : Editor()
 EndIf 
 ;attend meetings 
 Loader("Please Wait","Finding Meetings")
 If gamVenue(gamDate)=0 Then GenerateArena(charFed(gamChar),gamSchedule(gamDate),0)
 arenaAttendance=0
 gamCasted=0
 RiskContractOffers()
 RiskBookerMeetings()
 If screen<>25
  RiskExternalMeetings()
  RiskMatchOffers()
  RiskWrestlerMeetings()
 EndIf
 ;save progress
 Loader("Please Wait","Saving Progress")
 SaveUniverse()
 SaveProgress(slot)
 SaveWorld(slot)
 SaveChars(slot)
 If screen<>25 And screen<>50
  gamScroll=-((GetMonth(gamDate)-1)*125)
  screen=20
 EndIf
End Function

;/////////////////////////////////////////////////////////////////////////////////////
;------------------------------ RELATED FUNCTIONS ------------------------------------
;/////////////////////////////////////////////////////////////////////////////////////

;AVAILABLE FOR SIMULATED FIGHT?
Function FightAvailable(char)
 available=0
 If charFed(char)=<7 And charRole(char)=1 And charHealth(char)>50 And InjuryStatus(char)=0 And charVacant(char)=0 And charOpponent(char)=0 And charFought(char)=0 And TournamentStatus(char)=0
  If char<>gamChar And char<>charPartner(gamChar) And char<>charManager(gamChar) And char<>gamOpponent(gamDate) And char<>charPartner(gamOpponent(gamDate)) And char<>charManager(gamOpponent(gamDate))
   available=1
  EndIf
 EndIf
 Return available
End Function

;SIMULATED WIN EFFECT
Function WinEffect(char,v)
 ;become champion
 If charFed(char)=charFed(v) And TournamentStatus(char)=0 And TournamentStatus(v)=0
  If TitleHolder(v,1) Then fedChampWorld(charFed(char))=char : WriteHistory(charFed(char),1)
  If TitleHolder(v,2) Then fedChampInter(charFed(char))=char : WriteHistory(charFed(char),2)
;new belts
If TitleHolder(v,5) Then fedChampWomens(charFed(char))=char : WriteHistory(charFed(char),5)
If TitleHolder(v,6) Then fedChampUS(charFed(char))=char : WriteHistory(charFed(char),6)
If TitleHolder(v,7) Then fedChampTV(charFed(char))=char : WriteHistory(charFed(char),7)

;end
  If TitleHolder(v,3) 
   If charPartner(char)=0 Then charPartner(char)=AssignPartner(char,-1)  
   fedChampTag(charFed(char),1)=char
   fedChampTag(charFed(char),2)=charPartner(char)
   WriteHistory(charFed(char),3)
  EndIf
 EndIf
 ;boost status of winner
 charWins(char,charFed(char))=charWins(char,charFed(char))+1
 target=charPopularity(v)
 If target<charPopularity(char)+1 Then target=charPopularity(char)+1
 If charPopularity(char)=>90 Then charPopularity(char)=charPopularity(char)+Rnd(0,1) Else charPopularity(char)=charPopularity(char)+1;+PursueValue(charPopularity(char),target,Rnd(1,3))
 If charHappiness(char)=>90 Then charHappiness(char)=charHappiness(char)+Rnd(0,1) Else charHappiness(char)=charHappiness(char)+1;PursueValue(charHappiness(char),100,Rnd(1,5))
 If charPopularity(char)>charOldPopularity(char)
  fedLimit=fedPopularity(charFed(char))
  If fedLimit<60 Then fedLimit=60
  If charPopularity(char)>fedLimit Then charPopularity(char)=charPopularity(char)+1
  If charPopularity(char)>fedLimit+5 Then charPopularity(char)=fedPopularity(charFed(char))+5
  If charPopularity(char)<charOldPopularity(char) Then charPopularity(char)=charOldPopularity(char)
 EndIf 
 charHealth(char)=Rnd(charHealth(char)/2,charHealth(char))
 ;damage status of loser
 target=charPopularity(char)
 If target>charPopularity(v)-1 Then target=charPopularity(v)-1
 If charPopularity(v)>50 Then charPopularity(v)=charPopularity(v)-1;+PursueValue(charPopularity(v),target,Rnd(1,3))
 charHappiness(v)=charHappiness(v)-1;+PursueValue(charHappiness(v),30,Rnd(1,5))
 charHealth(v)=Rnd(0,charHealth(v)/2)
End Function 

;QUEUE REACTIONS TO TRADE
Function FindTradeReactions(char,promotion)
 For v=1 To no_chars
  If char<>v And charFed(v)=<7
   ;react to departures
   If charFed(char)=charFed(v) And promotion<>charFed(v)
    If charRelationship(v,char)>0 Then charTradeReaction(v)=-char
    If charRelationship(v,char)<0 Then charTradeReaction(v)=char
   EndIf
   ;react to arrivals
   If charFed(char)<>charFed(v) And promotion=charFed(v)
    If charRelationship(v,char)>0 Then charTradeReaction(v)=char
    If charRelationship(v,char)<0 Then charTradeReaction(v)=-char
   EndIf
  EndIf
 Next
End Function