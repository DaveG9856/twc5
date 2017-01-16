;//////////////////////////////////////////////////////////////////////////////
;------------------------- WRESTLING MPIRE 2008: MEETINGS ---------------------
;//////////////////////////////////////////////////////////////////////////////

;----------------------------------------------------------------
;//////////////////// RISK BOOKER MEETINGS //////////////////////
;----------------------------------------------------------------
Function RiskBookerMeetings()
 negTopic=0 : fed=charFed(gamChar) : negChar=fedBooker(fed) 
 ;allegiance issues
 chance=20
 If charHeel(gamChar)=1 And AllegianceRatio(fed,0)<AllegianceRatio(fed,1)-2 Then chance=chance/2
 If charHeel(gamChar)=0 And AllegianceRatio(fed,1)<AllegianceRatio(fed,0)-2 Then chance=chance/2
 If charHeel(gamChar)=1 And AllegianceRatio(fed,0)>AllegianceRatio(fed,1)+2 Then chance=chance*2
 If charHeel(gamChar)=0 And AllegianceRatio(fed,1)>AllegianceRatio(fed,0)+2 Then chance=chance*2
 randy=Rnd(0,chance)
 If randy=<1 And fed=<6 And gamAgreement(8)=0 Then negTopic=2+charHeel(gamChar) ;change allegiance
 ;team issues
 If TournamentStatus(gamChar)=0
  randy=Rnd(0,20)
  If randy=<1 And TitleHolder(gamChar,0)=0 And charPartner(gamChar)=0 
   its=0
   Repeat
    negVariable=fedRoster(fed,Rnd(1,fedSize(fed))) : its=its+1
    If its>1000 Then negVariable=0
   Until (negVariable<>gamChar And negVariable<>fedBooker(fed) And charRole(negVariable)=0 And charRelationship(gamChar,negVariable)>0 And TitleHolder(negVariable,0)=0) Or negVariable=0
   If negVariable>0 Then negTopic=9 ;form team
  EndIf
  If randy=2 And charPartner(gamChar)>0 Then negTopic=10 ;disband team
 EndIf
 ;title pushes
 If fed=<6 And TitleHolder(gamChar,0)=0 And LastResult()=3 And gamSchedule(gamDate)=>1 And gamSchedule(gamDate)=<2 And InjuryDate(gamDate)=0 And gamOpponent(gamDate)=0 And gamPromo(gamDate)=0
  chance=25
  If charRelationship(fedBooker(fed),gamChar)<0 Then chance=chance*2
  If charRelationship(fedBooker(fed),gamChar)>0 Then chance=chance/2
  randy=Rnd(0,chance)
  If randy=0 And charPartner(gamChar)=0 And fedChampWorld(fed)>0
   If charPopularity(gamChar)>fedPopularity(fed)-5 Or charPopularity(gamChar)>charPopularity(fedChampWorld(fed)) Or CountTitles(gamChar,fed)>0 
    negTopic=57 ;world title push
   EndIf
  EndIf
  If randy=1 And charPartner(gamChar)=0 And fedChampInter(fed)>0 Then negTopic=58 ;inter title push
  If randy=2 And charPartner(gamChar)>0 And fedChampTag(fed,1)>0 Then negTopic=59 ;tag title push
 EndIf
 ;forced to work with enemy
 randy=Rnd(0,12)
 If randy=0 And charPartner(gamChar)=0 And gamSchedule(gamDate)=>1 And gamSchedule(gamDate)=<2 And InjuryDate(gamDate)=0
  If gamOpponent(gamDate)=0 Or charRelationship(gamChar,gamOpponent(gamDate))<0
   its=0
   Repeat
    negVariable=fedRoster(fed,Rnd(1,fedSize(fed))) : its=its+1
    If its>1000 Then negVariable=0
   Until (negVariable<>gamChar And negVariable<>fedBooker(fed) And charRelationship(gamChar,negVariable)<0) Or negVariable=0
   If gamOpponent(gamDate)>0 And charRelationship(gamChar,gamOpponent(gamDate))<0 Then negVariable=gamOpponent(gamDate)
   If negVariable>0 Then negTopic=60
  EndIf
 EndIf
 ;gimmick suggestions
 chance=30
 If fed=<6 And charExperience(gamChar,fed)=<2 Then chance=chance/2
 If fed=7 Then chance=chance*2
 randy=Rnd(0,chance)
 If randy=<1 And gamAgreement(3)=0 Then negTopic=27 ;new costume
 If randy=2 And gamAgreement(2)=0 Then negTopic=28 ;new hairstyle
 If randy=3 And gamAgreement(1)=0 Then negTopic=29 ;new name
 If charPartner(gamChar)>0 And gamAgreement(1)=0
  If randy=4 Or (randy=5 And charTeamName$(gamChar)=charName$(gamChar)+"'s Team") Then negTopic=30 ;new team name
 EndIf
 If randy=6 And fed=<6 And gamAgreement(4)=0 Then negTopic=31 ;new entrance
 If randy=7 And gamAgreement(5)=0 Then negTopic=32 ;new attacks
 If randy=8 And gamAgreement(6)=0 Then negTopic=33 ;new moves
 If randy=9 And gamAgreement(7)=0 Then negTopic=34 ;new gestures
 ;health issues
 If InjuryStatus(gamChar)>0
  chance=8
  If charRelationship(fedBooker(fed),gamChar)<0 Then chance=chance/2
  If charRelationship(fedBooker(fed),gamChar)>0 Then chance=chance*2
  randy=Rnd(0,chance)
  If randy=0 And fed=<6 Then negTopic=38 ;asked to work through injury
  If randy=1 And fed=<6 Then negTopic=39 ;forced to work through injury
 Else
  chance=20
  If charRelationship(fedBooker(fed),gamChar)>0 Then chance=chance/2
  If charRelationship(fedBooker(fed),gamChar)<0 Then chance=chance*2
  randy=Rnd(0,chance)
  If (randy=0 And charHealth(gamChar)=<50) Or (randy=<1 And charHealth(gamChar)=<25) Then negTopic=52 ;night off
 EndIf 
 ;drug scandal
 If gamAgreement(13)>0 Or gamAgreement(14)>0
  chance=15
  If charRelationship(fedBooker(fed),gamChar)<0 Then chance=chance/2
  If charRelationship(fedBooker(fed),gamChar)>0 Then chance=chance*2
  randy=Rnd(0,chance)
  If randy=<1 Then negTopic=42
 EndIf
 ;contract issues
 chance=50
 If charRelationship(fedBooker(fed),gamChar)<0 Then chance=chance/2
 If charRelationship(fedBooker(fed),gamChar)>0 Then chance=chance*2
 randy=Rnd(0,chance) 
 If fed=<6 And charContract(gamChar)>0 And charSalary(gamChar)>100 And fedBank(fed)<1000000
  If randy=0 And fedSize(fed)>16 Then negTopic=53 ;release offer
  If randy=1 Then negTopic=54 ;pay-cut suggestion 
 EndIf
 If fed=<6 And (charPopularity(gamChar)>fedPopularity(fed)-10 Or TitleHolder(gamChar,0)>0)
  If randy=>2 And randy=<3 And fedSize(fed)>16 Then negTopic=55 ;opinion on internal
  If randy=>4 And randy=<5 And fedSize(fed)<optRosterLim Then negTopic=56 ;opinion on external
 EndIf 
 ;corruption
 If charBank(gamChar)>0 And gamSchedule(gamDate)=>1 And gamSchedule(gamDate)=<2 And InjuryDate(gamDate)=0 And optReferees>0 And charRelationship(fedBooker(fed),gamChar)=>0
  chance=25
  If charRelationship(fedBooker(fed),gamChar)<0 Then chance=chance*2
  If charRelationship(fedBooker(fed),gamChar)>0 Then chance=chance/2
  randy=Rnd(0,chance) 
  If randy=<1 Then negTopic=45 ;offered favourable ref
 EndIf
 ;venue issues
 If gamSchedule(gamDate)>0 And InjuryDate(gamDate)=0
  randy=Rnd(0,50)
  If randy=0 Then negTopic=65 ;power failure
  If randy=1 And fed=<6 Then negTopic=66 ;no furniture
  If randy=2 And fed=<6 Then negTopic=67 ;no crowd
 EndIf
 ;mission assignments
 If gamMission=0 And gamDate<40 And charExperience(gamChar,fed)>4 And InjuryStatus(gamChar)=0
  chance=50
  If charRelationship(fedBooker(fed),gamChar)<0 Then chance=chance/2
  If charRelationship(fedBooker(fed),gamChar)>0 Then chance=chance*2
  randy=Rnd(0,chance)
  If charPeaked(gamChar)=0
   If randy=1 And charStrength(gamChar)<80 Then negTopic=101 ;improve strength
   If randy=2 And charSkill(gamChar)<80 Then negTopic=102 ;improve skill
   If randy=3 And charAgility(gamChar)<80 Then negTopic=103 ;improve agility
   If randy=4 And charStamina(gamChar)<80 Then negTopic=104 ;improve stamina
   If randy=5 And charToughness(gamChar)<80 Then negTopic=105 ;improve toughness
  EndIf
  If randy=6 And charPopularity(gamChar)<80 And charPopularity(gamChar)<fedPopularity(fed)-5 Then negTopic=106 ;improve popularity
  If randy=7 And charAttitude(gamChar)<80 Then negTopic=107 ;improve attitude
  If randy=8 And charBank(gamChar)<100000 And fed=<6 Then negTopic=108 ;improve bank balance
  If randy=9 And (charWeight(gamChar)>200 Or (charGender(gamChar)=1 And charWeight(gamChar)>110))
   negTopic=109 ;lose weight
  EndIf
  If randy=10 And (charWeight(gamChar)<200 Or (charGender(gamChar)=1 And charWeight(gamChar)<110))
   negTopic=110 ;gain weight
  EndIf
  If randy=11 And fed=<6 And TitleHolder(gamChar,0)=0 And CountTitles(gamChar,fed)=0
   negTopic=111 ;win a title
  EndIf
  If randy=12 And GetWinRate(gamChar,0)<80 Then negTopic=114 ;improve win rate
  If randy=>15 And randy=<20 And fed=7 And charExperience(gamChar,fed)>12 Then negTopic=112 ;get a deal!
  If charBank(gamChar)=<0 Then negTopic=113 ;get out of debt!
 EndIf
 ;mission failure
 If gamMission>0 And gamDate=>gamDeadline 
  If fed=7 Then negTopic=131 Else negTopic=130
 EndIf
 ;mission completions
 If gamMission=1 And charStrength(gamChar)=>gamTarget Then negTopic=120 ;improved strength
 If gamMission=2 And charSkill(gamChar)=>gamTarget Then negTopic=120 ;improved strength
 If gamMission=3 And charAgility(gamChar)=>gamTarget Then negTopic=120 ;improved agility
 If gamMission=4 And charStamina(gamChar)=>gamTarget Then negTopic=120 ;improved stamina
 If gamMission=5 And charToughness(gamChar)=>gamTarget Then negTopic=120 ;improved toughness
 If gamMission=6 And charPopularity(gamChar)=>gamTarget Then negTopic=120 ;improved popularity 
 If gamMission=7 And charAttitude(gamChar)=>gamTarget Then negTopic=120 ;improved attitude 
 If gamMission=8 And charBank(gamChar)=>gamTarget Then negTopic=121 ;improved bank balance 
 If gamMission=9 And TranslateWeight(gamChar)<gamTarget Then negTopic=120 ;lost weight
 If gamMission=10 And TranslateWeight(gamChar)>gamTarget Then negTopic=120 ;gained weight 
 If gamMission=11 And (TitleHolder(gamChar,0)>0 Or gamChar=fedCupHolder(fed)) Then negTopic=120 ;won a title
 If gamMission=12 And fed<>7 Then negTopic=120 ;got a deal
 If gamMission=13 And charBank(gamChar)>0 Then negTopic=121 ;got out of debt 
 If gamMission=14 And GetWinRate(gamChar,0)=>gamTarget Then negTopic=120 ;improved win rate
 ;execute
 If negTopic>0 And negChar>0 Then screen=53 : Meeting() 
End Function

;----------------------------------------------------------------
;//////////////////// RISK EXTERNAL MEETINGS //////////////////////
;----------------------------------------------------------------
Function RiskExternalMeetings()
 negTopic=0 : negChar=0
 ;media offers
 If fed=<6 And charPopularity(gamChar)=>60 And InjuryStatus(gamChar)=0 And gamDate=<44 And gamSchedule(gamDate)>0
  chance=110-charPopularity(gamChar)
  If TitleHolder(gamChar,0)>0 Then chance=chance/2
  If chance<10 Then chance=10
  randy=Rnd(0,chance)
  If randy=<1 And gamAgreement(17)=0 Then negTopic=61 : negVariable=Rnd(1,weapList) : negChar=fedBooker(8) ;product endorsement
  If randy=2 Then negTopic=62 : negChar=fedBooker(8) ;interview request
  If randy=3 And charPopularity(gamChar)=>70 Then negTopic=63 : negChar=fedBooker(8) ;game offer
  If randy=4 And charPopularity(gamChar)=>80 Then negTopic=64 : negChar=fedBooker(8) ;movie offer
 EndIf
 ;medical offers
 If charBank(gamChar)>0
  randy=Rnd(0,20)
  If randy=0 And charHealth(gamChar)<75 And InjuryStatus(gamChar)=0 And gamAgreement(14)=0 Then negTopic=73 : negChar=fedBooker(9) ;pain relief
  If randy=1 And AverageStats(gamChar)<80 And InjuryStatus(gamChar)=0 And gamAgreement(13)=0 Then negTopic=74 : negChar=fedBooker(9) ;steroids
  randy=Rnd(0,10)
  If randy=<1 And InjuryStatus(gamChar)>2 Then negTopic=75 : negChar=fedBooker(9) ;treat injury
  For limb=1 To 50
   chance=50
   If MajorLimb(limb) Then chance=chance/5
   randy=Rnd(0,chance)
   If randy=<1 And charLimb(gamChar,limb)=0 Then negTopic=76 : negChar=fedBooker(9) ;correct disability
  Next
 EndIf 
 ;promised contract offers
 If charFed(gamChar)=7 And gamSchedule(gamDate)>0 And InjuryStatus(gamChar)=0
  fed=Rnd(1,6)
  chance=(100-GetValue(gamChar))/2
  If chance<10 Then chance=10
  randy=Rnd(0,chance)
  If randy=<1 And charExperience(gamChar,fed)=0 And charRelationship(fedBooker(fed),gamChar)=>0
   negTopic=82 : negChar=fedBooker(fed)
  EndIf
 EndIf
 ;execute
 If negTopic>0 And negChar>0 Then fed=charFed(negChar) : screen=53 : Meeting() 
End Function

;----------------------------------------------------------------
;/////////////////// RISK WRESTLER MEETINGS /////////////////////
;----------------------------------------------------------------
Function RiskWrestlerMeetings()
 ;INTERNAL ISSUES
 fed=charFed(gamChar) 
 For count=1 To fedSize(fed)
  negTopic=0 : negChar=fedRoster(fed,count)
  If negChar<>gamChar And negChar<>fedBooker(fed) And charRole(negChar)<>3
   ;allegiance issues
   chance=750
   If charHeel(gamChar)=1 And AllegianceRatio(fed,0)<AllegianceRatio(fed,1)-2 Then chance=chance/2
   If charHeel(gamChar)=0 And AllegianceRatio(fed,1)<AllegianceRatio(fed,0)-2 Then chance=chance/2
   If charHeel(gamChar)=1 And AllegianceRatio(fed,0)>AllegianceRatio(fed,1)+2 Then chance=chance*2
   If charHeel(gamChar)=0 And AllegianceRatio(fed,1)>AllegianceRatio(fed,0)+2 Then chance=chance*2
   randy=Rnd(0,chance)
   If randy=<1 And charHeel(gamChar)<>charHeel(negChar) And charRelationship(negChar,gamChar)=>0 Then negTopic=4+charHeel(gamChar) 
   ;relationship issues
   randy=Rnd(0,750)
   If randy=1 And charRelationship(negChar,gamChar)<0 And charHistory(negChar,gamChar)<>0 Then negTopic=12 ;enemy calls truce
   If randy=2 And charRelationship(negChar,gamChar)=0 And charHistory(negChar,gamChar)=0 And charBank(gamChar)>0 Then negTopic=13 ;socialize with stranger
   If randy=3 And charRelationship(negChar,gamChar)=<0 And charTeamHistory(gamChar,negChar)>0 Then negTopic=69 ;make up with old friend
   If randy=4 And charRelationship(negChar,gamChar)>0 And TitleHolder(negChar,0)=0 And gamOpponent(gamDate)=0 And gamSchedule(gamDate)=>1 And gamSchedule(gamDate)=<2
    If GetValue(negChar)<GetValue(gamChar) Then negTopic=47 ;inferior friend resents
    If GetValue(negChar)>GetValue(gamChar) Then negTopic=48 ;superior friend resents
   EndIf
   If randy=5 And charRelationship(gamChar,negChar)>0 And charBank(gamChar)>0 Then negTopic=79 ;friend asks for money
   ;marriage proposal!
   If gamSchedule(gamDate)=>1 And gamSchedule(gamDate)=<2 And InjuryDate(gamDate)=0 And gamOpponent(gamDate)=0 And gamPromo(gamDate)=0
    If charRelationship(negChar,gamChar)>0 And negChar<>charPartner(gamChar) And negChar<>charManager(gamChar) And TitleHolder(gamChar,0)=0 And TitleHolder(negChar,0)=0 
     chance=7500
     If charGender(negChar)<>charGender(gamChar) Then chance=chance/5
     randy=Rnd(0,chance)
     If randy=<1 Then negTopic=49 
    EndIf
   EndIf
   ;team issues
   If TitleHolder(gamChar,0)=0 And TitleHolder(negChar,0)=0 And negChar<>charManager(gamChar) And TournamentStatus(gamChar)=0
    randy=Rnd(0,750)
    If randy=0 And charPartner(gamChar)=0 And charRelationship(negChar,gamChar)<0 Then negTopic=11 ;form team with enemy 
    If randy=<1 And charPartner(gamChar)=0 And charRelationship(negChar,gamChar)>0 Then negTopic=6 ;form team with friend 
    If randy=2 And charPartner(gamChar)=0 And charTeamHistory(gamChar,negChar)=2 Then negTopic=68 ;reunite with old friend 
    If randy=3 And charPartner(gamChar)>0 And charRelationship(negChar,gamChar)=>0 Then negTopic=7 ;replace partner
    If LastResult()<3 Then randy=Rnd(0,(charHappiness(negChar)-25)/4) Else randy=Rnd(0,(charHappiness(negChar)-25)/2)
    If randy=0 And negChar=charPartner(gamChar) Then negTopic=8 ;disband team
   EndIf
   If negChar=charPartner(gamChar)
    randy=Rnd(0,20)
    If randy=0 Or (randy=<1 And charTeamName$(gamChar)=charName$(gamChar)+"'s Team")
     If gamAgreement(1)=0 Then negTopic=36 ;change team name
    EndIf
    If charLegs(gamChar,1)<>charLegs(negChar,1) And gamAgreement(3)=0
     If randy=2 Then negTopic=35 ;synchronize costumes (you copy them)
     If randy=3 Then negTopic=83 ;synchronize costumes (they copy you)
    EndIf
   EndIf
   ;management issues
   If charManager(gamChar)=0 And GetValue(negChar)<GetValue(gamChar) And TitleHolder(negChar,0)=0 And negChar<>charPartner(gamChar) And charRelationship(negChar,gamChar)=>0
    chance=(100-GetValue(gamChar))*10
    If charHeel(negChar)<>charHeel(gamChar) Then chance=chance*2
    If charRole(negChar)<>2 Then chance=chance*2
    randy=Rnd(0,chance)
    If randy=0 Then negTopic=14 ;new management offer
    If randy=1 And charTeamHistory(gamChar,negChar)=3 Then negTopic=70 ;old manager crawls back
   EndIf
   If LastResult()<3 Then randy=Rnd(0,(charHappiness(negChar)-25)/4) Else randy=Rnd(0,(charHappiness(negChar)-25)/2)
   If randy=0 And negChar=charManager(gamChar) And charBank(gamChar)>0 Then negTopic=15 ;manager wants to leave
   ;brawls
   If InjuryStatus(negChar)=0
    chance=(charAttitude(negChar)-25)*10
    If charHeel(gamChar)=charHeel(negChar) Then chance=chance*2
    randy=Rnd(0,chance)
    If randy=0 And charRelationship(negChar,gamChar)=>0 And gamSchedule(gamDate)>0 And InjuryDate(gamDate)=0 And charBank(gamChar)>0
     If gamOpponent(gamDate)=0 Then gamOpponent(gamDate)=AssignOpponent(gamChar,gamDate,0)
     negTopic=78 ;sabotage offer
    EndIf
    If randy=1 And charRelationship(negChar,gamChar)<0 Then negTopic=21 ;enemy wants to brawl
    If randy=2 And charRelationship(negChar,gamChar)=<0 And gamSchedule(gamDate)>0 And InjuryDate(gamDate)=0 And charBank(gamChar)>0 
     negTopic=19 ;money demand
    EndIf
    ;give up friend
    If randy=>3 And randy=<4
     its=0
     Repeat
      negVariable=fedRoster(fed,Rnd(1,fedSize(fed))) : its=its+1
      If its>1000 Then negVariable=0
     Until negVariable=0 Or (negVariable<>gamChar And negVariable<>negChar And charRelationship(gamChar,negVariable)>0 And charRelationship(negChar,negVariable)<0)
     If negVariable>0 Then negTopic=20  
    EndIf
    ;racist confrontation
    chance=(charAttitude(negChar)-25)*15
    If charRelationship(negChar,gamChar)<0 Then chance=chance/2
    randy=Rnd(0,chance)
    If randy=<1 And GetRace(gamChar)<>GetRace(negChar) And gamSchedule(gamDate)>0 And charRelationship(negChar,gamChar)=<0
     negTopic=77 
    EndIf
   EndIf
   ;hired gun
   chance=(AverageStats(negChar)-25)*15
   randy=Rnd(0,chance)
   If randy=<1 And AverageStats(negChar)<AverageStats(gamChar) And charRelationship(negChar,gamChar)=>0 
    its=0
    Repeat
     negVariable=fedRoster(fed,Rnd(1,fedSize(fed))) : its=its+1
     If its>1000 Then negVariable=0
    Until negVariable=0 Or (negVariable<>gamChar And negVariable<>negChar And charRelationship(negChar,negVariable)<0)
    If negVariable>0 Then negTopic=22 
   EndIf
   ;foreign invasion!
   randy=Rnd(0,2000)
   If randy=<1 And fed=<6 And gamSchedule(gamDate)=>1 And gamSchedule(gamDate)=<2 And InjuryDate(gamDate)=0
    Repeat
     negVariable=Rnd(1,6)
    Until negVariable<>fed
    negTopic=23
   EndIf
   ;riot for better pay
   chance=((charAttitude(negChar)-25)+(GetValue(negChar)-25))*15
   randy=Rnd(0,chance)
   If randy=0 And charSalary(gamChar)>0 And charSalary(gamChar)=<charWorth(gamChar) And charRelationship(negChar,gamChar)=>0
    negTopic=24
   EndIf
   ;trading opportunities
   If charBank(gamChar)>0 And charRelationship(negChar,gamChar)=>0
    randy=Rnd(0,1500)
    If randy=0 And charHealth(gamChar)<75 And InjuryStatus(gamChar)=0 And gamAgreement(14)=0 Then negTopic=40 ;buy pain relief
    If randy=1 And AverageStats(gamChar)<80 And InjuryStatus(gamChar)=0 And gamAgreement(13)=0 Then negTopic=41 ;buy steroids
    If randy=2 Then negTopic=80 ;buy moves
    If randy=3 Then negTopic=81 ;buy clothes
   EndIf 
   ;match fixing
   If gamOpponent(gamDate)=0 Or negChar=gamOpponent(gamDate)
    If gamSchedule(gamDate)=>1 And gamSchedule(gamDate)=<2 And InjuryDate(gamDate)=0 And gamAgreement(11)=0 And gamAgreement(12)=0 
     randy=Rnd(0,750)
     If randy=0 Or (randy=<1 And GetValue(negChar)<GetValue(gamChar)) Then negTopic=44 ;opponent asks you to lie down
     If randy=2 And charBank(gamChar)>0 And TitleHolder(negChar,0)=0 Then negTopic=43 ;opponent offers to lie down
    EndIf
   EndIf
   ;encouraged to shoot
   If fed=<6 And gamSchedule(gamDate)=>1 And gamSchedule(gamDate)=<2 And InjuryDate(gamDate)=0 And charRelationship(negChar,gamChar)=>0 
    chance=(charAttitude(negChar)-25)*75
    randy=Rnd(0,chance)
    If randy=<1 Then negTopic=46
   EndIf
   ;enemy requests match
   If InjuryStatus(negChar)=0 And charRelationship(negChar,gamChar)<0 And gamDate=<47 And gamOpponent(gamDate+1)=0 And gamPromo(gamDate+1)=0 And gamSchedule(gamDate+1)=>1 And gamSchedule(gamDate+1)=<2
    randy=Rnd(0,750)
    If randy=0 And charHairStyle(gamChar,1)>5 Then negTopic=50 ;hair versus hair
    If randy=1 And fed=<6 And charContract(gamChar)>4 Then negTopic=51 ;loser leaves town
   EndIf
  EndIf
  ;execute
  If negTopic>0 And negChar>0 And screen<>25 And screen<>50 Then screen=53 : Meeting() 
 Next
End Function

;----------------------------------------------------------------
;////////////////// RISK POST-MATCH MEETINGS ////////////////////
;----------------------------------------------------------------
Function RiskLateMeetings()
 ;nothing by default
 negTopic=0 : negChar=0 : fed=charFed(gamChar)
 ;MATCH INCIDENTS
 For cyc=1 To no_plays
  ;team issues
  If matchTeams>0 And cyc=<no_wrestlers And pTeam(cyc)=pTeam(matchPlayer) And pChar(cyc)<>gamChar  
   ;break up after loss
   If pChar(cyc)=charPartner(gamChar) And gamResult(gamDate)<3 And TitleHolder(gamChar,0)=0
    randy=Rnd(0,(charHappiness(pChar(cyc))-25)/4)
    If randy=0 Then negTopic=8 : negChar=pChar(cyc)
    If randy=1 Then negTopic=72 : negChar=pChar(cyc)
   EndIf
   ;make temporary team permanent
   If matchWinner>0 And pTeam(matchPlayer)=pTeam(matchWinner) And charPartner(gamChar)=0 And TitleHolder(gamChar,0)=0 And TitleHolder(pChar(cyc),0)=0
    randy=Rnd(0,10)
    If randy=<1 Then negTopic=71 : negChar=pChar(cyc)
   EndIf
  EndIf
  ;inter-promotional aftermath
  randy=Rnd(0,1)
  If randy=0 And optContent>0 And cyc=<no_wrestlers And gamSchedule(gamDate)=4 And charFed(pChar(cyc))=gamRivalFed(gamDate)
   negTopic=25 : negChar=pChar(cyc)
  EndIf
  ;invasion leads to inter-promotional
  If (matchIntruder>0 And pChar(cyc)=pChar(matchIntruder)) Or (matchIntruder<0 And pChar(cyc)=pChar(MakePositive#(matchIntruder)))
   If fed=<6 And charFed(pChar(cyc))=<6 And charFed(pChar(cyc))<>fed And gamSchedule(gamDate)=<2 And gamDate=<44 And gamSchedule(gamDate+4)=<2 And InjuryStatus(gamChar)=0
    conflict=0
    For date=gamDate-2 To gamDate+4
     If date>0
      If gamSchedule(date)=4 Then conflict=1
     EndIf
    Next
    If conflict=0 Then negTopic=26 : negChar=pChar(cyc)
   EndIf
  EndIf
 Next
 ;RANDOM ISSUES
 If negTopic=0
  ;overtime
  chance=20
  If fed=7 And gamSchedule(gamDate)=<1 Then chance=chance*2
  randy=Rnd(0,chance)
  its=0
  Repeat
   negVariable=fedRoster(fed,Rnd(1,fedSize(fed))) : its=its+1
   If its>1000 Then negVariable=0
  Until negVariable=0 Or (FindCharacter(negVariable)=0 And negVariable<>gamChar And negVariable<>charPartner(gamChar) And negVariable<>charManager(gamChar) And negVariable<>fedBooker(fed))
  If randy=0 And gamSchedule(gamDate)=<2 And negVariable>0 Then negTopic=16 : negChar=negVariable ;manage someone
  If randy=1 And gamSchedule(gamDate)=<2 Then negTopic=17 : negChar=fedBooker(fed) ;referee a match
  If randy=2 And gamSchedule(gamDate)=<2 Then negTopic=18 : negChar=fedBooker(fed) ;fight another match
  ;pay for damages
  chance=2500
  If matchRules=0 Then chance=chance*2
  randy=Rnd(0,chance)
  If matchDamage=>100 And randy=<matchDamage And charBank(gamChar)>0 And gamDate=<47 And gamSchedule(gamDate+1)>0 And InjuryStatus(gamChar)=0
   negTopic=37 : negChar=fedBooker(fed) 
  EndIf
 EndIf
 ;execute
 If negTopic>0 And negChar>0 Then screen=53 : Meeting()  
End Function

;---------------------------------------------------------------
;///////////////// LOAD MEETING SETTING ////////////////////////
;---------------------------------------------------------------
Function PrepareMeeting()
 ;LOCATION
 ResetTextures()
 ;office locations
 If negSetting=>1 And negSetting=<5 
  world=LoadAnimMesh("World/Office/Office.3ds")
  If charFed(negChar)=>1 And charFed(negChar)=<6
   tempLogo=LoadTexture("World/Videos/Promotion0"+charFed(negChar)+".JPG")
   tempFlag=LoadTexture("World/Videos/Flag0"+charFed(negChar)+".JPG")
  Else
   If charFed(negChar)=0 Then tempLogo=LoadTexture("World/Videos/Video02.JPG")
   If charFed(negChar)>0 Then tempLogo=LoadTexture("Graphics/Promotions/Promotion0"+charFed(negChar)+".png")
   tempFlag=LoadTexture("World/Videos/Flag02.JPG") 
  EndIf
  EntityTexture FindChild(world,"Poster01"),tempLogo
  FreeTexture tempLogo
  EntityTexture FindChild(world,"Poster02"),tempFlag
  FreeTexture tempFlag
  EntityAlpha FindChild(world,"Window"),0.5 
  If negSetting=1 And camFoc=1 ;seated (aiming for guest)
   randy=Rnd(1,2)
   If randy=1 Then camX#=-80 : camY#=50 : camZ#=90
   If randy=2 Then camX#=100 : camY#=50 : camZ#=-80
  EndIf
  If negSetting=1 And camFoc=2 ;seated (aiming for host)
   randy=Rnd(1,2)
   If randy=1 Then camX#=-90 : camY#=50 : camZ#=-90
   If randy=2 Then camX#=90 : camY#=50 : camZ#=-90
  EndIf
  If negSetting=2 ;north east
   camX#=-80 : camY#=Rnd(25,50) : camZ#=-80
   meetX#=65 : meetZ#=80 : meetA#=135
  EndIf
  If negSetting=3 ;south east
   camX#=-90 : camY#=Rnd(10,50) : camZ#=90
   meetX#=60 : meetZ#=-70 : meetA#=45
  EndIf
  If negSetting=4 ;south west
   camX#=90 : camY#=Rnd(10,50) : camZ#=90
   meetX#=-55 : meetZ#=-60 : meetA#=315
  EndIf
  If negSetting=5 ;north west
   camX#=80 : camY#=Rnd(25,50) : camZ#=-80
   meetX#=-70 : meetZ#=80 : meetA#=225
  EndIf
  lightX#=0 : lightY#=65 : lightZ#=0
 EndIf
 ;locker room locations
 If negSetting=>6 And negSetting=<9 
  GetArenaSettings(gamVenue(gamDate))
  world=LoadAnimMesh("World/Arena/Hall.3ds")
  DecorateArena()
  If negSetting=6 ;north east
   randy=Rnd(1,2)
   If randy=1 Then camX#=-80 : camY#=Rnd(10,50) : camZ#=870
   If randy=2 Then camX#=220 : camY#=Rnd(10,50) : camZ#=650
   lightX#=110 : lightY#=90 : lightZ#=805
   meetX#=175 : meetZ#=820 : meetA#=135
  EndIf
  If negSetting=7 ;south east
   randy=Rnd(1,2)
   If randy=1 Then camX#=220 : camY#=Rnd(10,50) : camZ#=860
   If randy=2 Then camX#=-40 : camY#=Rnd(10,50) : camZ#=630
   lightX#=110 : lightY#=90 : lightZ#=695
   meetX#=145 : meetZ#=680 : meetA#=45
  EndIf
  If negSetting=8 ;south west
   randy=Rnd(1,2)
   If randy=1 Then camX#=-100 : camY#=Rnd(10,50) : camZ#=880
   If randy=2 Then camX#=40 : camY#=Rnd(10,50) : camZ#=620
   lightX#=-100 : lightY#=90 : lightZ#=695
   meetX#=-100 : meetZ#=690 : meetA#=315
  EndIf
  If negSetting=9 ;north west
   randy=Rnd(1,2)
   If randy=1 Then camX#=-90 : camY#=Rnd(10,50) : camZ#=640
   If randy=2 Then camX#=66 : camY#=Rnd(10,50) : camZ#=875
   lightX#=-100 : lightY#=90 : lightZ#=805
   meetX#=-165 : meetZ#=835 : meetA#=225
  EndIf
 EndIf
 ;lounge locations
 If negSetting=>10 And negSetting=<13 
  GetArenaSettings(gamVenue(gamDate))
  world=LoadAnimMesh("World/Arena/Hall.3ds")
  DecorateArena()
  If negSetting=10 ;north east
   randy=Rnd(1,2)
   If randy=1 Then camX#=-65 : camY#=Rnd(10,50) : camZ#=-620
   If randy=2 Then camX#=130 : camY#=Rnd(10,50) : camZ#=-780
   lightX#=55 : lightY#=70 : lightZ#=-705
   meetX#=100 : meetZ#=-650 : meetA#=135
  EndIf
  If negSetting=11 ;south east
   randy=Rnd(1,2) 
   If randy=1 Then camX#=120 : camY#=Rnd(10,50) : camZ#=-625
   If randy=2 Then camX#=-110 : camY#=Rnd(10,50) : camZ#=-755
   lightX#=55 : lightY#=70 : lightZ#=-705
   meetX#=105 : meetZ#=-755 : meetA#=45
  EndIf
  If negSetting=12 ;south west
   randy=Rnd(1,2)
   If randy=1 Then camX#=-120 : camY#=Rnd(10,50) : camZ#=-620
   If randy=2 Then camX#=120 : camY#=Rnd(10,50) : camZ#=-770
   lightX#=-52 : lightY#=70 : lightZ#=-705
   meetX#=-90 : meetZ#=-740 : meetA#=315
  EndIf
  If negSetting=13 ;north west
   randy=Rnd(1,2)
   If randy=1 Then camX#=-40 : camY#=Rnd(10,50) : camZ#=-780
   If randy=2 Then camX#=90 : camY#=Rnd(10,50) : camZ#=-625
   lightX#=-52 : lightY#=70 : lightZ#=-705
   meetX#=-105 : meetZ#=-650 : meetA#=225
  EndIf
 EndIf
 ;universal scenery
 PrepareScenery()
 ;ATMOSPHERE
 ;camera
 cam=CreateCamera()
 CameraViewport cam,0,0,GraphicsWidth(),GraphicsHeight()
 CameraZoom cam,1.5 
 PositionEntity cam,camX#,camY#,camZ# 
 camType=10 : camOption=1
 ;pivot
 dummy=CreatePivot()
 camPivot=CreatePivot()
 ;lighting
 AmbientLight 220,210,200
 no_lights=1
 light(1)=CreateLight(1) 
 LightColor light(1),250,230,210
 PositionEntity light(1),lightX#,lightY#,lightZ#
 lightPivot=CreatePivot()
 PositionEntity lightPivot,lightX#+Rnd(-100,100),lightY#,lightZ#+Rnd(-110,110) 
 PointEntity light(1),lightPivot
 ;power failure
 If (screen=53 And negTopic=65) Or gamAgreement(18)>0
  AmbientLight 20,20,30
  LightColor light(1),25,25,25
 EndIf
 ;CHARACTERS
 ;calculate cast
 no_plays=4
 For count=1 To optPlayLim
  pChar(count)=0
 Next
 pChar(1)=gamChar
 pChar(2)=negChar
 If negSetting=1
  For cyc=1 To 2
   If fed<>charFed(pChar(cyc)) And FindCharacter(fedBooker(charFed(pChar(cyc))))=0 And pChar(cyc+2)=0
    bookerBlock=0
    If screen=53 And negTopic=>73 And negTopic=<74 Then bookerBlock=1
    If bookerBlock=0 Then pChar(cyc+2)=fedBooker(charFed(pChar(cyc)))
   EndIf
   If charManager(pChar(cyc))>0 And FindCharacter(charManager(pChar(cyc)))=0 And pChar(cyc+2)=0 Then pChar(cyc+2)=charManager(pChar(cyc))
   If charPartner(pChar(cyc))>0 And FindCharacter(charPartner(pChar(cyc)))=0 And pChar(cyc+2)=0 Then pChar(cyc+2)=charPartner(pChar(cyc))
  Next
 EndIf
 ;office positions
 If negSetting=1
  pX#(1)=0 : pY#(1)=5 : pZ#(1)=-4 : pA#(1)=0 : pAnim(1)=Rnd(4,11)
  If pAnim(1)>9 Then pAnim(1)=9
  pX#(2)=0 : pY#(2)=5 : pZ#(2)=67 : pA#(2)=180 : pAnim(2)=Rnd(1,8)
  pX#(3)=20 : pY#(3)=5 : pZ#(3)=-23 : pA#(3)=15 : pAnim(3)=Rnd(10,13)
  pX#(4)=-20 : pY#(4)=5 : pZ#(4)=90 : pA#(4)=195 : pAnim(4)=Rnd(10,13)
 EndIf
 ;standing positions
 If negSetting=>2 And negSetting<20
  PositionEntity dummy,meetX#,0,meetZ#
  RotateEntity dummy,0,meetA#,0
  MoveEntity dummy,12,0,0
  pX#(1)=EntityX(dummy) : pY#(1)=wGround# : pZ#(1)=EntityZ(dummy) : pA#(1)=meetA#+60 : pAnim(1)=Rnd(10,13)
  PositionEntity dummy,meetX#,0,meetZ#
  RotateEntity dummy,0,meetA#,0
  MoveEntity dummy,-12,0,0
  pX#(2)=EntityX(dummy) : pY#(2)=wGround# : pZ#(2)=EntityZ(dummy) : pA#(2)=meetA#-60 : pAnim(2)=Rnd(10,13)
  If negSetting=>2 And negSetting=<5 Then pY#(1)=5 : pY#(2)=5
 EndIf
 ;load models
 For cyc=1 To no_plays
  If pChar(cyc)>0
   LoadMeeter(cyc,pChar(cyc))
  EndIf
 Next
 ;restore textures
 RestoreTextures()
 ;point camera at camfoc
 camPivX#=pX#(camFoc) : camPivY#=pY#(camFoc)+30 : camPivZ#=pZ#(camFoc)
 PositionEntity camPivot,camPivX#,camPivY#,camPivZ#
End Function

;---------------------------------------------------------------
;//////////////// LOAD MEETING CHARACTER ///////////////////////
;---------------------------------------------------------------
Function LoadMeeter(cyc,char)
 ;generate model
 p(cyc)=LoadAnimMesh("Characters/Models/Model0"+GetModel(char)+".3ds")
 StripModel(cyc)
 ;apply costume
 pCostume(cyc)=2
 If screen=53
  If (negTopic=27 And cyc=1) Or (negTopic=35 And cyc=<2) Or (negTopic=83 And cyc=<2) Then pCostume(cyc)=1
 EndIf
 If screen=55 And cyc=3 And pChar(cyc)<>fedBooker(charFed(char)) Then pCostume(cyc)=3
 ApplyCostume(cyc)
 pEyes(cyc)=2 : pOldEyes(cyc)=-1
 pFoc(cyc)=0
 ;include props
 If TitleHolder(char,0)>0 Then WearBelt(cyc,TitleHolder(char,0))
 If screen=53 And cyc=2
  If (negTopic=>40 And negTopic=<41) Or (negTopic=>73 And negTopic=<74) Then ShowEntity FindChild(p(cyc),"Syringe")
 EndIf
 If screen=53 And negTopic=61 And cyc=2
  ShowEntity FindChild(p(cyc),weapFile$(negVariable))
  If weapTex(negVariable)>0 Then EntityTexture FindChild(p(cyc),weapFile$(negVariable)),weapTex(negVariable)
  If weapFile$(negVariable)="Bottle" Then EntityAlpha FindChild(p(cyc),weapFile$(negVariable)),0.75
  EntityShininess FindChild(p(cyc),weapFile$(negVariable)),weapShine#(negVariable)
  If pAnim(cyc)<10 Then pAnim(cyc)=1 Else pAnim(cyc)=10
 EndIf
 ;post-match scars
 If screen=53 And gamResult(gamDate)<>0 And FindCasted(char)>0
  For limb=0 To 50
   If pLimb(cyc,limb)>0
    If limb=<4 Then EntityTexture pLimb(cyc,limb),tFaceScar(1),0,5
    If limb=5 Then EntityTexture pLimb(cyc,limb),tBodyScar(1),0,5
    If (limb=>6 And limb=<7) Or (limb=>19 And limb=<20) Then EntityTexture pLimb(cyc,limb),tArmScar(1),0,5
    If (limb=>8 And limb=<18) Or (limb=>21 And limb=<31) Then EntityTexture pLimb(cyc,limb),tHandScar(1),0,5
    If limb=>36 And limb=<44 Then EntityTexture pLimb(cyc,limb),tLegScar(1),0,5
    If limb=>45 And limb=<46 Then EntityTexture pLimb(cyc,limb),tEyeScar(1),0,5
   EndIf
  Next
 EndIf
 ;load sequences
 pSeq(cyc,601)=LoadAnimSeq(p(cyc),"Characters/Sequences/Standard01.3ds")
 pSeq(cyc,604)=LoadAnimSeq(p(cyc),"Characters/Sequences/Standard04.3ds")
 pSeq(cyc,605)=LoadAnimSeq(p(cyc),"Characters/Sequences/Standard05.3ds")
 pSeq(cyc,606)=LoadAnimSeq(p(cyc),"Characters/Sequences/Standard06.3ds") 
 pSeq(cyc,610)=LoadAnimSeq(p(cyc),"Characters/Sequences/Standard10.3ds") 
 ;sitting animations
 pSeq(cyc,0)=ExtractAnimSeq(p(cyc),2090,2100,pSeq(cyc,604)) ;dead!
 pSeq(cyc,1)=ExtractAnimSeq(p(cyc),1480,1520,pSeq(cyc,604)) ;sitting at desk (hands spread)
 pSeq(cyc,2)=ExtractAnimSeq(p(cyc),2280,2320,pSeq(cyc,604)) ;sitting at desk (hands overlapped)
 pSeq(cyc,3)=ExtractAnimSeq(p(cyc),2330,2370,pSeq(cyc,604)) ;sitting at desk (hands clasped)
 pSeq(cyc,4)=ExtractAnimSeq(p(cyc),2180,2220,pSeq(cyc,604)) ;sitting rubbing chin
 pSeq(cyc,5)=ExtractAnimSeq(p(cyc),2230,2270,pSeq(cyc,604)) ;sitting arms folded
 pSeq(cyc,6)=ExtractAnimSeq(p(cyc),2480,2520,pSeq(cyc,604)) ;sitting relaxed
 pSeq(cyc,7)=ExtractAnimSeq(p(cyc),2430,2470,pSeq(cyc,604)) ;sitting hands behind head
 pSeq(cyc,8)=ExtractAnimSeq(p(cyc),2380,2420,pSeq(cyc,604)) ;sitting arms on sides
 pSeq(cyc,9)=ExtractAnimSeq(p(cyc),1530,1570,pSeq(cyc,604)) ;sitting cross-legged
 ;standing animations
 pSeq(cyc,10)=ExtractAnimSeq(p(cyc),1730,1760,pSeq(cyc,601)) ;standing straight
 pSeq(cyc,11)=ExtractAnimSeq(p(cyc),2110,2170,pSeq(cyc,604)) ;standing rubbing chin
 pSeq(cyc,12)=ExtractAnimSeq(p(cyc),945,1005,pSeq(cyc,606)) ;standing hands on hips
 pSeq(cyc,13)=ExtractAnimSeq(p(cyc),2020,2080,pSeq(cyc,604)) ;standing folded arms
 pSeq(cyc,14)=ExtractAnimSeq(p(cyc),495,675,pSeq(cyc,610)) ;court speech
 pSeq(cyc,20)=ExtractAnimSeq(p(cyc),70,110,pSeq(cyc,604)) ;injured stance
 pSeq(cyc,21)=ExtractAnimSeq(p(cyc),1625,1665,pSeq(cyc,605)) ;feminine
 If pAnim(cyc)=>10 And charGender(pChar(cyc))=1 Then pAnim(cyc)=21
 ;weary
 weary=0
 If InjuryStatus(char)>0 Or charHealth(char)<25 Then weary=1
 If screen=53 And char=gamChar And (negTopic=40 Or negTopic=73 Or negTopic=76) Then weary=1
 If pAnim(cyc)=>10 And weary=1 Then pAnim(cyc)=20
 ;dead!
 If pAnim(cyc)<10 And charFed(char)=9 And char<>fedBooker(9)
   pAnim(cyc)=0
   For limb=0 To 50
    If pLimb(cyc,limb)>0
     If limb=<4 Then EntityTexture pLimb(cyc,limb),tFaceScar(Rnd(1,4)),0,5
     If limb=5 Then EntityTexture pLimb(cyc,limb),tBodyScar(Rnd(1,4)),0,5
     If limb=>6 And limb=<31 Then EntityTexture pLimb(cyc,limb),tArmScar(Rnd(1,4)),0,5
     If limb=>36 And limb=<44 Then EntityTexture pLimb(cyc,limb),tLegScar(Rnd(1,4)),0,5
    EndIf
   Next
 EndIf
 Animate p(cyc),1,Rnd#(0.1,0.3),pSeq(cyc,pAnim(cyc)),0
 ;orientation
 PositionEntity p(cyc),pX#(cyc),pY#(cyc),pZ#(cyc)
 RotateEntity p(cyc),0,pA#(cyc),0
 scaler#=0.055+(GetPercent#(charHeight(char),24)/10000)
 If pAnim(cyc)<10 Then scaler#=0.06
 ScaleEntity p(cyc),scaler#,scaler#,scaler# 
 ;shadows
 LoadShadows(cyc)
End Function

;----------------------------------------------------------------------------
;//////////////////////// 53. MEETING PROCESS ///////////////////////////////
;----------------------------------------------------------------------------
Function Meeting()
;loading message
Loader("Please Wait","Meeting "+charName$(negChar))
;ChannelVolume chTheme,0.5
;history filter
If negTopic=6 And charTeamHistory(gamChar,negChar)=2 Then negTopic=68 ;team reform
If negTopic=14 And charTeamHistory(gamChar,negChar)=3 Then negTopic=70 ;manager returns
;costume suggestion
If negTopic=27 Or negTopic=35 Or negTopic=83
 If negTopic=83 Then char=negChar Else char=gamChar
 CopyChar(char,0)
 For coz=1 To 3
  Repeat 
   idol=Rnd(1,no_chars)
  Until idol<>char
  If negTopic=35 Then idol=charPartner(gamChar)
  If negTopic=83 Then idol=gamChar
  charBaggy(char,coz)=charBaggy(idol,coz)
  charHatStyle(char,coz)=charHatStyle(idol,coz)
  charHat(char,coz)=charHat(idol,coz)
  charSpecs(char,coz)=charSpecs(idol,coz)
  If charBody(char,coz)>5 Or charBody(idol,coz)>5 Then charBody(char,coz)=charBody(idol,coz)
  charLeftArm(char,coz)=charLeftArm(idol,coz)
  charRightArm(char,coz)=charRightArm(idol,coz) 
  charLeftForearm(char,coz)=charLeftForearm(idol,coz)
  charRightForearm(char,coz)=charRightForearm(idol,coz) 
  charLeftHand(char,coz)=charLeftHand(idol,coz)
  charRightHand(char,coz)=charRightHand(idol,coz)  
  charShorts(char,coz)=charShorts(idol,coz)
  charLegs(char,coz)=charLegs(idol,coz)
  charShins(char,coz)=charShins(idol,coz)
  charShoes(char,coz)=charShoes(idol,coz)
 Next
EndIf
;hairstyle suggestion
If negTopic=28
 CopyChar(gamChar,0)
 its=0
 Repeat 
  idol=Rnd(1,no_chars) : its=its+1
 Until (idol<>gamChar And GetRace(idol)=GetRace(gamChar)) Or its>1000
 charHair(gamChar,1)=charHair(idol,1)
 charHatStyle(gamChar,1)=0
 charHairStyle(gamChar,1)=Rnd(0,no_hairstyles)
 For coz=2 To 3
  charHatStyle(gamChar,coz)=charHatStyle(gamChar,1)
  charHairStyle(gamChar,coz)=charHairStyle(gamChar,1)
  charHair(gamChar,coz)=charHair(gamChar,1)
 Next
EndIf
;get name suggestions
If negTopic=29 Or negTopic=30 Or negTopic=36 
 charName$(0)=GenerateName$(gamChar)
 charTeamName$(0)=textTeamName$(Rnd(1,no_teamnames))
EndIf
;internal opinion
If negTopic=55
 Repeat
  negVariable=Rnd(1,no_chars)
 Until charFed(negVariable)=charFed(gamChar) And negVariable<>gamChar And negVariable<>negChar
EndIf
;external opinion
If negTopic=56
 Repeat
  negVariable=Rnd(1,no_chars)
 Until charFed(negVariable)=<8 And charFed(negVariable)<>charFed(gamChar) And negVariable<>gamChar And negVariable<>negChar
EndIf
;load setting
camFoc=2 : negSetting=Rnd(2,13)
If charRole(negChar)>1 Then negSetting=Rnd(2,5)
If negChar=fedBooker(charFed(negChar)) Then negSetting=1 
If (negTopic=27 Or negTopic=35) And negSetting=1 Then negSetting=Rnd(2,5)
If gamSchedule(gamDate)=<0 And negSetting>1 Then negSetting=Rnd(2,5)
PrepareMeeting()
;reset situation
negStage=0 : negTim=0
negVerdict=0
;bribe amount
negPayOff=charBank(gamChar)/10
negPayOff=Rnd(negPayOff/2,negPayOff*2)
If negTopic=13 Then negPayOff=100 ;socialize cost
If negTopic=37 Then negPayOff=matchDamage*10 ;damages cost
If negTopic=>16 And negTopic=<18 
 negPayOff=PercentOf#(charSalary(gamChar),150) ;overtime
 If negPayOff<charWorth(gamChar)/2 Then negPayOff=charWorth(gamChar)/2
EndIf
If negTopic=38 Then negPayOff=charWorth(gamChar)*2 ;work through injury
If negTopic=61 Or negTopic=62 Then negPayOff=((GetValue(gamChar)-30)*(GetValue(gamChar)-30))*2 ;minor media fee
If negTopic=63 Then negPayOff=((GetValue(gamChar)-30)*(GetValue(gamChar)-30))*5 ;average media fee
If negTopic=64 Then negPayOff=((GetValue(gamChar)-30)*(GetValue(gamChar)-30))*10 ;major media fee
If (negTopic=>73 And negTopic=<76) ;Or (negTopic=>40 And negTopic=<41)
 negPayOff=charBank(gamChar)/5 
 If negPayOff<1000 Then negPayOff=1000 ;medical costs
EndIf
If negTopic=42 ;major fine
 negPayOff=charBank(gamChar)/5
 If negPayOff<1000 Then negPayOff=1000 
EndIf 
negPayOff=RoundOff(negPayOff,100)
If negPayOff<100 Then negPayOff=100
If negPayOff>10000 Then negPayOff=10000
If charFed(gamChar)=7 Then negPayOff=negPayOff/2
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
	
	;CHOICE CONFIRMATION
	If keytim=0 And negStage=1
	 ;highlight option
	 If KeyDown(200) Or JoyYDir()=-1 Then foc=foc-1 : keytim=10
	 If KeyDown(208) Or JoyYDir()=1 Then foc=foc+1 : keytim=10
	 If foc>2 Then foc=2
	 If foc<1 Then foc=1 
	 ;proceed 
     If KeyDown(1) Or KeyDown(28) Or ButtonPressed() Or MouseDown(1)
	  PlaySound sMenuGo : keytim=20
	  negStage=2 : negTim=0
	  If foc=1 Then negVerdict=1
	  If foc=2 Or KeyDown(1) Then negVerdict=-1
	 EndIf
	EndIf
	
	;SPEAKING
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
 ;reset expressions
 For cyc=1 To no_plays
  pSpeaking(cyc)=0
 Next
 ;reset subtitles
 lineA$="" : lineB$=""
 redLineA$="" : redLineB$=""
 greenLineA$="" : greenLineB$=""
 ;------------------------ TOPICS ----------------------------
 ;0. WELCOME MESSAGE
 If negTopic=0
  If negTim>25 And negTim<325
   Speak(2,0,3)
   lineA$="Welcome to the wonderful world of Wrestling MPire 2008,"
   lineB$=charName$(gamChar)+"! I'm glad you accepted our offer..."
  EndIf
  If negTim>375 Then go=1
 EndIf
 ;1. RETIREMENT CONFIRMATION
 If negTopic=1
  optionA$="Yes, retire..." : optionB$="No, keep wrestling!"
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,3)
   lineA$="Hey, "+charName$(gamChar)+", what's all this I hear about"
   lineB$="retirement?! Are you sure you want to call it a day?"
  EndIf
  If negStage=0 And negTim>325 Then camFoc=1
  If negStage=0 And negTim>350 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1 And CountExperience(gamChar,0)<48
   Speak(2,0,1)
   lineA$="If that's what you want, I guess there's nothing left"
   lineB$="to say. Some people just haven't got what it takes..."
  EndIf
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1 And CountExperience(gamChar,0)=>48
   Speak(2,0,3)
   lineA$="Well, I suppose all that remains is to wish you luck."
   lineB$="Perhaps we'll see you behind this desk some time!"
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(2,0,3)
   lineA$="Good, I hate to see people throwing away their careers!"
   lineB$="Remember, you've only ever failed when you give up..."
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;2. BOOKER ASKS IF YOU WANT TO TURN HEEL
 If negTopic=2
  optionA$="Yes, turn Heel!" : optionB$="No, stay Face..."
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,2)
   lineA$="Listen, "+charName$(gamChar)+", I want to talk to you"
   lineB$="about taking your career in a new direction..."
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,3)
   lineA$="How would you feel about turning against the fans?"
   lineB$="It could give your career a whole new perspective!"
  EndIf
  If negStage=0 And negTim>650 Then camFoc=1
  If negStage=0 And negTim>675 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,3)
   If charged=0 
    ChangeAllegiance(gamChar,1) : gamAgreement(8)=4
    If gamPromo(gamDate)=0 And fed=<6 Then gamPromo(gamDate)=26
    AdjustAttitude(gamChar,2) : charged=1
   EndIf
   lineA$="OK, we'll start pushing you as a badass right away!"
   lineB$="The fans will go nuts if you wind them up properly..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1 And charClause(gamChar,1)>0
   Speak(2,0,1)
   If charged=0 Then AdjustAttitude(gamChar,-1) : charged=1
   lineA$="Alright, it was only a suggestion! You better mix"
   lineB$="it up though. Even heroes get boring after a while..."
  EndIf
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1 And charClause(gamChar,1)=0
   Speak(2,0,1)
   If charged=0 
    ChangeAllegiance(gamChar,1) : gamAgreement(8)=4
    If gamPromo(gamDate)=0 And fed=<6 Then gamPromo(gamDate)=26
    AdjustAttitude(gamChar,-2) : charged=1
   EndIf
   lineA$="Sorry, did I make it sound like you had a choice?"
   lineB$="We need you to turn bad, so get used to the idea!"
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;3. BOOKER ASKS IF YOU WANT TO TURN FACE
 If negTopic=3
  optionA$="Yes, turn Face!" : optionB$="No, stay Heel..."
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,2)
   lineA$="Listen, "+charName$(gamChar)+", I want to talk to you"
   lineB$="about taking your career in a new direction..."
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,3)
   lineA$="How would you feel about becoming a fan favourite?"
   lineB$="It could help to make you more popular than ever!"
  EndIf
  If negStage=0 And negTim>650 Then camFoc=1
  If negStage=0 And negTim>675 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,3)
   If charged=0 
    ChangeAllegiance(gamChar,0) : gamAgreement(8)=4
    If gamPromo(gamDate)=0 And fed=<6 Then gamPromo(gamDate)=25
    AdjustAttitude(gamChar,2) : charged=1
   EndIf
   lineA$="OK, we'll start portraying you in a positive light!"
   lineB$="The fans should enjoy the chance to get behind you..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1 And charClause(gamChar,1)>0
   Speak(2,0,1)
   If charged=0 Then AdjustAttitude(gamChar,-1) : charged=1
   lineA$="Alright, it was only a suggestion! You need to mix"
   lineB$="things up to prevent your act from becoming stale..."
  EndIf
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1 And charClause(gamChar,1)=0
   Speak(2,0,1)
   If charged=0 
    ChangeAllegiance(gamChar,0) : gamAgreement(8)=4
    If gamPromo(gamDate)=0 And fed=<6 Then gamPromo(gamDate)=25
    AdjustAttitude(gamChar,-2) : charged=1
   EndIf
   lineA$="Sorry, did I make it sound like you had a choice?"
   lineB$="We need you to turn good, so get used to the idea!"
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;4. WRESTLER TRIES TO TURN YOU HEEL
 If negTopic=4
  optionA$="Yes, turn Heel!" : optionB$="No, stay Face..."
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,1)
   lineA$="Hey, "+charName$(gamChar)+", don't you ever get"
   lineB$="tired of doing everything by the book?"
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,1)
   lineA$="This isn't an office job - it's a fight for survival!"
   lineB$="Nice guys really do finish last in this business..."
  EndIf
  If negStage=0 And negTim>675 And negTim<975
   Speak(2,0,1)
   lineA$="But if you wanna grow a set of balls and be a man,"
   lineB$="come join my crew and live life on the wild side!"
  EndIf
  If negStage=0 And negTim>975 Then camFoc=1
  If negStage=0 And negTim>1000 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,1)
   If charged=0 
    ChangeAllegiance(gamChar,1) : gamAgreement(8)=4
    ChangeRelationship(gamChar,negChar,1)
    If charPartner(gamChar)>0 
     ChangeRelationship(gamChar,charPartner(gamChar),-1)
     ChangeRelationship(negChar,charPartner(gamChar),-1)
     FormTeam(gamChar,negChar)
    EndIf
    If gamPromo(gamDate)=0 And fed=<6
     randy=Rnd(0,2)
     If randy=<1 And charPartner(gamChar)=0 Then gamPromo(gamDate)=26
     If randy=2 Or charPartner(gamChar)>0 Then gamMatch(gamDate)=12 : gamGimmick(gamDate)=0 : gamPromo(gamDate)=64 : gamPromoVariable(gamDate)=negChar
    EndIf
    AdjustAttitude(gamChar,-1) : charged=1
   EndIf
   lineA$="Haha, I knew there was a bad-ass in there somewhere!"
   lineB$="You're gonna love the way we operate, brother..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(2,0,1)
   If charged=0 Then ChangeRelationship(gamChar,negChar,-1) : AdjustAttitude(gamChar,1) : charged=1
   lineA$="Yeah, they told me you were a loser - and now I know!"
   lineB$="You better watch your back, cos we certainly won't be..."
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;5. WRESTLER TRIES TO TURN YOU FACE
 If negTopic=5
  optionA$="Yes, turn Face!" : optionB$="No, stay Heel..."
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,2)
   lineA$="Hey, "+charName$(gamChar)+", why do you go through"
   lineB$="your career with a chip on your shoulder?"
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,3)
   lineA$="Wrestling isn't a prison sentence - it's a SPORT!"
   lineB$="If you acted like an athlete, you'd appreciate that..."
  EndIf
  If negStage=0 And negTim>675 And negTim<975
   Speak(2,0,3)
   lineA$="And so would the fans. They'd love to get behind you!"
   lineB$="Why don't you join us and see for yourself?"
  EndIf
  If negStage=0 And negTim>975 Then camFoc=1
  If negStage=0 And negTim>1000 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,3)
   If charged=0 
    ChangeAllegiance(gamChar,0) : gamAgreement(8)=4
    ChangeRelationship(gamChar,negChar,1)
    If charPartner(gamChar)>0 
     ChangeRelationship(gamChar,charPartner(gamChar),-1)
     ChangeRelationship(negChar,charPartner(gamChar),-1)
     FormTeam(gamChar,negChar)
    EndIf
    If gamPromo(gamDate)=0 And fed=<6
     randy=Rnd(0,2)
     If randy=<1 And charPartner(gamChar)=0 Then gamPromo(gamDate)=25
     If randy=2 Or charPartner(gamChar)>0 Then gamMatch(gamDate)=12 : gamGimmick(gamDate)=0 : gamPromo(gamDate)=64 : gamPromoVariable(gamDate)=negChar
    EndIf
    AdjustAttitude(gamChar,2) : charged=1
   EndIf
   lineA$="I knew there was a heart in there somewhere!"
   lineB$="Your life is about to change for the better..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(2,0,1)
   If charged=0 Then ChangeRelationship(gamChar,negChar,-1) : AdjustAttitude(gamChar,-2) : charged=1
   lineA$="That's a shame. I thought you had potential, but"
   lineB$="I guess you'll waste your life like all the others..."
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;6. FRIEND SUGGESTS TEAM FORMATION
 If negTopic=6
  optionA$="Yes, form team!" : optionB$="No, remain single..."
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,3)
   lineA$="Hey, "+charName$(gamChar)+", we get on well right? So why"
   lineB$="are we wasting our time on the solo circuit?!"
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,3)
   lineA$="We should pour all of our energy into a proper team!"
   lineB$="Then we could both soar up the ranks side-by-side..."
  EndIf
  If negStage=0 And negTim>650 Then camFoc=1
  If negStage=0 And negTim>675 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,3)
   If charged=0 
    FormTeam(gamChar,negChar)
    If charHappiness(negChar)<75 Then charHappiness(negChar)=75
    If gamPromo(gamDate)=0 And fed=<6 Then gamMatch(gamDate)=12 : gamGimmick(gamDate)=0 : gamPromo(gamDate)=3
    AdjustAttitude(gamChar,2) : charged=1
   EndIf
   lineA$="Excellent! This could be the start of something big!"
   lineB$="As long as we stick together, no one can stop us..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(2,0,1)
   If charged=0 Then ChangeRelationship(gamChar,negChar,-1) : AdjustAttitude(gamChar,-2) : charged=1
   lineA$="What, you think you're too good to team with me?"
   lineB$="Fine! Let's see how well you do without my help..."
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;7. NEW FRIEND SUGGESTS REPLACING PARTNER
 If negTopic=7
  optionA$="Yes, replace partner!" : optionB$="No, keep partner..."
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,1) : negVariable=charPartner(gamChar) : ChangeRelationship(negChar,negVariable,-1)
   lineA$="Hey, "+charName$(gamChar)+", why are you wasting your"
   lineB$="career carrying a loser like "+charName$(negVariable)+"?!"
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,2)
   lineA$="You deserve better! Let me be your partner instead"
   lineB$="and I'll show you what a REAL team can achieve..."
  EndIf
  If negStage=0 And negTim>650 Then camFoc=1
  If negStage=0 And negTim>675 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,2)
   If charged=0 
    ChangeRelationship(gamChar,negVariable,-1)
    ChangeRelationship(gamChar,negChar,1)
    FormTeam(gamChar,negChar)
    If charHappiness(negChar)<75 Then charHappiness(negChar)=75
    If gamPromo(gamDate)=0 And fed=<6
     randy=Rnd(0,2)
     If randy=<1 Then gamMatch(gamDate)=12 : gamGimmick(gamDate)=0 : gamPromo(gamDate)=3
     If randy=2 Then gamMatch(gamDate)=12 : gamGimmick(gamDate)=0 : gamPromo(gamDate)=64 : gamPromoVariable(gamDate)=negChar
    EndIf
    AdjustAttitude(gamChar,-1) : charged=1
   EndIf
   lineA$="A wise choice! Forget about "+charName$(negVariable)+"."
   lineB$="Success is the only friend you need now..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(2,0,1)
   If charged=0 Then ChangeRelationship(gamChar,negChar,-1) : AdjustAttitude(gamChar,1) : charged=1
   lineA$="Hey, I was only trying to salvage your career!"
   lineB$="Good luck getting anywhere with "+charName$(negVariable)+"..."
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;8. PARTNER SUGGESTS SPLIT
 If negTopic=8
  optionA$="Yes, disband team..." : optionB$="No, keep team!"
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,1)
   lineA$="I don't know about you, "+charName$(gamChar)+", but"
   lineB$="I'm not sure this team is working out so well..."
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,3)
   lineA$="There's so much more we could experience on our own!"
   lineB$="Don't you think it's time we went our separate ways?"
  EndIf
  If negStage=0 And negTim>650 Then camFoc=1
  If negStage=0 And negTim>675 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,3)
   If charged=0 
    charPartner(charPartner(gamChar))=0 : charPartner(gamChar)=0
    AnnounceBreakup()
    AdjustAttitude(gamChar,1) : charged=1
   EndIf
   lineA$="Good, I'm glad we both feel the same way!"
   lineB$="You'll soon realize this is for the best..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(2,0,2)
   If charged=0 Then AdjustAttitude(gamChar,-1) : charged=1
   lineA$="Alright, if you think there's a future for us"
   lineB$="I guess I could stick it out a little longer..."
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;9. BOOKER SUGGEST TEAM FORMATION
 If negTopic=9
  optionA$="Yes, form team!" : optionB$="No, remain single..."
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,2)
   lineA$="Hey, "+charName$(gamChar)+", I notice that you and"
   lineB$=charName$(negVariable)+" have been getting on well lately?"
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,3)
   lineA$="Well, how would you feel about working as a team?"
   lineB$="I'm sure that chemistry would translate to the ring!"
  EndIf
  If negStage=0 And negTim>650 Then camFoc=1
  If negStage=0 And negTim>675 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,3)
   If charged=0 
    FormTeam(gamChar,negVariable)
    If gamPromo(gamDate)=0 And fed=<6 Then gamMatch(gamDate)=12 : gamGimmick(gamDate)=0 : gamPromo(gamDate)=3
    AdjustAttitude(gamChar,2) : charged=1
   EndIf
   lineA$="OK, we'll start promoting you as a double-act right"
   lineB$="away! This could be the start of something big..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1 And charClause(gamChar,1)>0
   Speak(2,0,2)
   If charged=0 Then AdjustAttitude(gamChar,-1) : charged=1
   lineA$="You don't see it? OK, it was only a suggestion."
   lineB$="Just make sure you shine as a solo wrestler!"
  EndIf
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1 And charClause(gamChar,1)=0
   Speak(2,0,1)
   If charged=0 
    FormTeam(gamChar,negVariable)
    If gamPromo(gamDate)=0 And fed=<6 Then gamMatch(gamDate)=12 : gamGimmick(gamDate)=0 : gamPromo(gamDate)=3
    AdjustAttitude(gamChar,-2) : charged=1
   EndIf
   lineA$="Sorry, did I make it sound like you had a choice?"
   lineB$="You're teaming with "+Him$(charGender(negVariable))+", so get used to the idea!"
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;10. BOOKER SUGGEST ENDING TEAM
 If negTopic=10
  optionA$="Yes, disband team..." : optionB$="No, keep team!"
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,2)
   lineA$="Listen, "+charName$(gamChar)+", I'm not sure that your"
   lineB$="team with "+charName$(charPartner(gamChar))+" is doing you any favours..."
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,3)
   lineA$="How would you feel about striking out on your own?"
   lineB$="A solo career could be so much more rewarding!"
  EndIf
  If negStage=0 And negTim>650 Then camFoc=1
  If negStage=0 And negTim>675 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,3)
   If charged=0 
    charPartner(charPartner(gamChar))=0 : charPartner(gamChar)=0
    AnnounceBreakup()
    AdjustAttitude(gamChar,2) : charged=1
   EndIf
   lineA$="OK, we'll start promoting you as a solo artist!"
   lineB$="This could be just the break your career needs..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1 And charClause(gamChar,1)>0
   Speak(2,0,1)
   If charged=0 Then AdjustAttitude(gamChar,-1) : charged=1
   lineA$="You want to hide behind a team-mate forever?!"
   lineB$="That's not what champions are made of..."
  EndIf
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1 And charClause(gamChar,1)=0
   Speak(2,0,1)
   If charged=0 
    charPartner(charPartner(gamChar))=0 : charPartner(gamChar)=0
    AnnounceBreakup()
    AdjustAttitude(gamChar,-2) : charged=1
   EndIf
   lineA$="Sorry, did I make it sound like you had a choice?"
   lineB$="You're on your own now, so get used to the idea!"
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;11. ENEMY SUGGESTS TEAM FORMATION
 If negTopic=11
  optionA$="Yes, form team!" : optionB$="No, remain single..."
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,1)
   lineA$="Hey, "+charName$(gamChar)+", you know we've taken"
   lineB$="each other to hell and back in recent weeks?"
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,2)
   lineA$="Well, it occurs to me that we've been wasting"
   lineB$="our careers by trying to outdo each other!"
  EndIf
  If negStage=0 And negTim>675 And negTim<975
   Speak(2,0,3)
   lineA$="Legends like us should be working TOGETHER!"
   lineB$="Why don't we join forces and rule this place?"
  EndIf
  If negStage=0 And negTim>975 Then camFoc=1
  If negStage=0 And negTim>1000 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,3)
   If charged=0 
    FormTeam(gamChar,negChar)
    If charHappiness(negChar)<75 Then charHappiness(negChar)=75
    If gamPromo(gamDate)=0 And fed=<6 Then gamMatch(gamDate)=12 : gamGimmick(gamDate)=0 : gamPromo(gamDate)=3
    AdjustAttitude(gamChar,2) : charged=1
   EndIf
   lineA$="Excellent! It'll be good to work alongside you."
   lineB$="I've certainly had enough of working against you!"
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(2,0,1)
   If charged=0 Then ChangeRelationship(gamChar,negChar,-1) : AdjustAttitude(gamChar,-2) : charged=1
   lineA$="Hey, I was only trying to offer you a way out."
   lineB$="Instead of ending a battle, you've started a WAR!"
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;12. ENEMY CALLS TRUCE
 If negTopic=12
  optionA$="Yes, make friends..." : optionB$="No, keep feuding!"
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,1)
   lineA$="Hey, "+charName$(gamChar)+", I know we haven't"
   lineB$="been seeing eye-to-eye in recent weeks..."
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,2)
   lineA$="Well, I for one am tired of all the bickering!"
   lineB$="It's not doing either of our careers any good..."
  EndIf
  If negStage=0 And negTim>675 And negTim<975
   Speak(2,0,3)
   lineA$="Why don't we put it all behind us and get back"
   lineB$="to wrestling? I'm willing to do it if you are..."
  EndIf
  If negStage=0 And negTim>975 Then camFoc=1
  If negStage=0 And negTim>1000 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,3)
   If charged=0 
    ChangeRelationship(gamChar,negChar,1)
    If gamOpponent(gamDate)=0 Then gamOpponent(gamDate)=negChar : gamPromo(gamDate)=54
    AdjustAttitude(gamChar,2) : charged=1
   EndIf
   lineA$="Thank God for that! I thought it would never end."
   lineB$="Who knows? Maybe we'll end up being friends!"
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(2,0,1)
   If charged=0 Then ChangeRelationship(gamChar,negChar,-1) : AdjustAttitude(gamChar,-2) : charged=1
   lineA$="Hey, I was only trying to offer you a way out."
   lineB$="Instead of ending a battle, you've started a WAR!"
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;13. STRANGER WANTS TO SOCIALIZE
 If negTopic=13
  optionA$="Yes, make friends!" : optionB$="No thanks..."
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,2)
   lineA$="Hey, "+charName$(gamChar)+", how are you doing? You've"
   lineB$="been awfully quiet with everybody backstage..."
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,3)
   lineA$="What happens in the ring isn't all that matters."
   lineB$="At the end of the day, we're all on the same team!"
  EndIf
  If negStage=0 And negTim>675 And negTim<975
   Speak(2,0,3)
   lineA$="Why don't you come out to dinner tonight with me"
   lineB$="and the guys? You won't spend more than $"+GetFigure$(negPayOff)+"..."
  EndIf
  If negStage=0 And negTim>975 Then camFoc=1
  If negStage=0 And negTim>1000 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,3)
   If charged=0 
    PlaySound sCash : charBank(gamChar)=charBank(gamChar)-negPayOff
    ChangeRelationship(gamChar,negChar,1)
    AdjustAttitude(gamChar,2) : charged=1
   EndIf
   lineA$="Alright, it'll be good to get to know the real you!"
   lineB$="Meet us after the show and we'll trade war stories..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(2,0,1)
   If charged=0 Then ChangeRelationship(gamChar,negChar,-1) : AdjustAttitude(gamChar,-2) : charged=1
   lineA$="Hey, I was only trying to be friendly! Trust me,"
   lineB$="it doesn't happen very often in this business..."
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;14. MANAGEMENT OFFER
 If negTopic=14
  optionA$="Yes, hire manager!" : optionB$="No thanks..."
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,1)
   lineA$="Hey, "+charName$(gamChar)+", what do you hope to"
   lineB$="achieve without a manager by your side?"
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,2)
   lineA$="A helping hand at ringside can often be the"
   lineB$="difference between success and failure!"
  EndIf
  If negStage=0 And negTim>675 And negTim<975
   Speak(2,0,3)
   lineA$="I'd be happy to help you out myself if you want?"
   lineB$="All I'd ask for in return is the standard 10%..."
  EndIf
  If negStage=0 And negTim>975 Then camFoc=1
  If negStage=0 And negTim>1000 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,3)
   If charged=0 
    PlaySound sCash : charManager(gamChar)=negChar
    ChangeRelationship(gamChar,negChar,1)
    If charHappiness(negChar)<75 Then charHappiness(negChar)=75
    If gamPromo(gamDate)=0 And fed=<6 Then gamPromo(gamDate)=40  
    AdjustAttitude(gamChar,2) : charged=1
   EndIf
   lineA$="Good, I'm glad we could come to an arrangement!"
   lineB$="You'll never have to lose another match again..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(2,0,1)
   If charged=0 Then ChangeRelationship(gamChar,negChar,-1) : AdjustAttitude(gamChar,-2) : charged=1
   lineA$="Hey, I was only trying to help you out! Don't"
   lineB$="blame me the next time you get outnumbered..."
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;15. MANAGEMENT WANTS BRIBE
 If negTopic=15
  optionA$="Yes, pay $"+GetFigure$(negPayOff)+"!" : optionB$="No, release manager..."
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,1)
   lineA$="Listen, "+charName$(gamChar)+", I'm really not happy"
   lineB$="about being your manager at the moment..."
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,2)
   lineA$="I've got plenty of other things to worry about,"
   lineB$="and managing you is the least of my concerns!"
  EndIf
  If negStage=0 And negTim>675 And negTim<975
   Speak(2,0,3)
   lineA$="If you want to keep me on, I'm afraid I'll"
   lineB$="have to ask for a $"+GetFigure$(negPayOff)+" bonus upfront?"
  EndIf
  If negStage=0 And negTim>975 Then camFoc=1
  If negStage=0 And negTim>1000 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,3)
   If charged=0 
    PlaySound sCash : charBank(gamChar)=charBank(gamChar)-negPayOff
    ChangeRelationship(gamChar,negChar,1)
    AdjustAttitude(gamChar,2) : charged=1
   EndIf
   lineA$="Thanks, this should make the job a little easier!"
   lineB$="Maybe there's money to be made from you after all..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(2,0,1)
   If charged=0 
    charManager(gamChar)=0 : AnnounceBreakup()
    ChangeRelationship(gamChar,negChar,-1)
    AdjustAttitude(gamChar,-2) : charged=1
   EndIf
   lineA$="I'm obviously not that valuable to you anyway!"
   lineB$="Perhaps your opponents will appreciate my help..."
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;16. WRESTLER ASKS YOU TO MANAGE
 If negTopic=16
  optionA$="Yes, accept $"+GetFigure$(negPayOff)+"!" : optionB$="No, sorry..."
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,3)
   lineA$="Hey, "+charName$(gamChar)+", would you mind doing"
   lineB$="me a favour before you leave for the night?"
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,2)
   lineA$="I've got the biggest match of my life next,"
   lineB$="but I'm not sure I can make it on my own!"
  EndIf
  If negStage=0 And negTim>675 And negTim<975
   Speak(2,0,3)
   lineA$="Would you consider assisting me at ringside?"
   lineB$="There's $"+GetFigure$(negPayOff)+" in it for you if you do!"
  EndIf
  If negStage=0 And negTim>975 Then camFoc=1
  If negStage=0 And negTim>1000 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,3)
   If charged=0 
    PlaySound sCash : charBank(gamChar)=charBank(gamChar)+negPayOff
    ChangeRelationship(gamChar,negChar,1)
    AdjustAttitude(gamChar,2) : charged=1
   EndIf
   lineA$="You just saved my life! With you by my side I can't"
   lineB$="lose! Now let's get out there and chalk up a win..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(2,0,1)
   If charged=0 Then ChangeRelationship(gamChar,negChar,-1) : AdjustAttitude(gamChar,-1) : charged=1
   lineA$="Damn, you were my only hope! Thanks for nothing!"
   lineB$="Don't come to me the next time you need help..."
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;17. BOOKER WANTS YOU TO REFEREE
 If negTopic=17
  optionA$="Yes, accept $"+GetFigure$(negPayOff)+"!" : optionB$="No thanks..."
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,3)
   lineA$="Hey, "+charName$(gamChar)+", would you mind doing"
   lineB$="me a favour before you leave for the night?"
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,3)
   lineA$="We're short of referees at the moment, and I"
   lineB$="need somebody to take care of the next match!"
  EndIf
  If negStage=0 And negTim>675 And negTim<975
   Speak(2,0,3)
   lineA$="Would you be able to serve as the referee?"
   lineB$="There's $"+GetFigure$(negPayOff)+" in it for you if you do!"
  EndIf
  If negStage=0 And negTim>975 Then camFoc=1
  If negStage=0 And negTim>1000 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,3)
   If charged=0 
    PlaySound sCash : charBank(gamChar)=charBank(gamChar)+negPayOff
    AdjustAttitude(gamChar,2) : charged=1
   EndIf
   lineA$="Great, thanks for doing this! I know you've got"
   lineB$="better things to do, but it shouldn't take long..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1 And charClause(gamChar,1)>0
   Speak(2,0,1)
   If charged=0 Then AdjustAttitude(gamChar,-1) : charged=1
   lineA$="I guess your matches are the only ones that matter?!"
   lineB$="Never mind, I'll just have to take care of it myself..."
  EndIf
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1 And charClause(gamChar,1)=0
   Speak(2,0,1)
   If charged=0 Then AdjustAttitude(gamChar,-2) : charged=1
   lineA$="Sorry, did I make it sound like you had a choice?"
   lineB$="We need you to help out, so get out there and do it!"
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;18. BOOKER WANTS YOU TO WORK A SECOND MATCH
 If negTopic=18
  optionA$="Yes, accept $"+GetFigure$(negPayOff)+"!" : optionB$="No thanks..."
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,3)
   lineA$="Hey, "+charName$(gamChar)+", would you mind doing"
   lineB$="me a favour before you leave for the night?"
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,2)
   lineA$="The roster is such a shambles tonight that I'm"
   lineB$="struggling to put together a full card of matches!"
  EndIf
  If negStage=0 And negTim>675 And negTim<975
   Speak(2,0,3)
   lineA$="Do you feel fit enough to work another match?"
   lineB$="There's $"+GetFigure$(negPayOff)+" in it for you if you do!"
  EndIf
  If negStage=0 And negTim>975 Then camFoc=1
  If negStage=0 And negTim>1000 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,3)
   If charged=0 
    PlaySound sCash : charBank(gamChar)=charBank(gamChar)+negPayOff
    AdjustAttitude(gamChar,1) : charged=1
   EndIf
   lineA$="Thanks for doing this! I know it's not ideal,"
   lineB$="but think of it as another chance to shine..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1 And charClause(gamChar,1)>0
   Speak(2,0,2)
   If charged=0 Then AdjustAttitude(gamChar,-1) : charged=1
   lineA$="OK, I suspected it would be too much to ask."
   lineB$="I guess I'll have to make up the numbers myself!"
  EndIf
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1 And charClause(gamChar,1)=0
   Speak(2,0,1)
   If charged=0 Then AdjustAttitude(gamChar,-2) : charged=1
   lineA$="Sorry, did I make it sound like you had a choice?"
   lineB$="We need you to wrestle, so get out there and do it!"
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;19. BEATING THREAT
 If negTopic=19
  optionA$="Yes, pay $"+GetFigure$(negPayOff)+"..." : optionB$="No, do your worst!"
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,2)
   lineA$="Hey, "+charName$(gamChar)+", I just wanted to"
   lineB$="wish you luck for your match tonight..."
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,1)
   lineA$="Dangerous things happen back here, and it would"
   lineB$="be a shame if you ended up CRAWLING to the ring!"
  EndIf
  If negStage=0 And negTim>675 And negTim<975
   Speak(2,0,1)
   lineA$="But neither of us want that to happen, right?"
   lineB$="Good, so hand over $"+GetFigure$(negPayOff)+" and it won't!"
  EndIf
  If negStage=0 And negTim>975 Then camFoc=1
  If negStage=0 And negTim>1000 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,2)
   If charged=0 
    PlaySound sCash : charBank(gamChar)=charBank(gamChar)-negPayOff
    ChangeRelationship(gamChar,negChar,1)
    AdjustAttitude(gamChar,-2) : charged=1
   EndIf
   lineA$="A wise decision! Although I am disappointed"
   lineB$="that I didn't get to kick somebody's ass..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(2,0,1)
   If charged=0 Then ChangeRelationship(gamChar,negChar,-1) : AdjustAttitude(gamChar,1) : charged=1
   lineA$="Well, you can't say I didn't warn you! The only"
   lineB$="place you're going to is the emergency room..."
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;20. THREATENED TO GIVE UP FRIEND
 If negTopic=20
  optionA$="Yes, forsake friend..." : optionB$="No, do your worst!"
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,1)
   lineA$="Hey, "+charName$(gamChar)+", why are you hanging"
   lineB$="around with losers like "+charName$(negVariable)+"?!"
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,1)
   lineA$="You better reconsider the company you keep!"
   lineB$="That asshole has enemies in high places..."
  EndIf
  If negStage=0 And negTim>675 And negTim<975
   Speak(2,0,1) : g=charGender(negVariable)
   lineA$="So what's it gonna be? Are you gonna give "+Lower$(Him$(g))
   lineB$="up, or do I have to knock some sense into you?!"
  EndIf
  If negStage=0 And negTim>975 Then camFoc=1
  If negStage=0 And negTim>1000 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,2)
   If charged=0 
    ChangeRelationship(gamChar,negVariable,-1)
    ChangeRelationship(gamChar,negChar,1)
    AdjustAttitude(gamChar,-1) : charged=1
   EndIf
   lineA$="Haha, good for you! Being friends with that"
   lineB$="cretin never helped anybody's career..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(2,0,1)
   If charged=0 Then ChangeRelationship(gamChar,negChar,-1) : AdjustAttitude(gamChar,1) : charged=1
   lineA$="You're prepared to take a hit for THAT loser?!"
   lineB$="A couple of idiots like you deserve each other..."
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;21. ENEMY WANTS TO BRAWL
 If negTopic=21
  optionA$="Yes, bring it on!" : optionB$="No, forget it..."
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,1)
   lineA$="Hey, "+charName$(gamChar)+", the beef between"
   lineB$="us has gone far beyond the ring!"
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,1)
   lineA$="I don't care about winning matches anymore."
   lineB$="I just want to kick your ass for real!"
  EndIf
  If negStage=0 And negTim>675 And negTim<975
   Speak(2,0,1)
   lineA$="Are you tough enough to take me on right now, or"
   lineB$="are you a bitch that only likes it in the ring?"
  EndIf
  If negStage=0 And negTim>975 Then camFoc=1
  If negStage=0 And negTim>1000 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,1)
   If charged=0 Then AdjustAttitude(gamChar,-1) : charged=1
   lineA$="Wow, I'm surprised you had the guts to say yes!"
   lineB$="You haven't got what it takes to beat me though..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(2,0,1)
   If charged=0 Then AdjustAttitude(gamChar,1) : charged=1
   lineA$="Like you have a choice! I'm swinging your way"
   lineB$="no matter what! It's up to you if you fight back..."
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;22. HIRED GUN
 If negTopic=22
  g=charGender(negVariable)
  optionA$="Yes, accept $"+GetFigure$(negPayOff)+"!" : optionB$="No, sorry..."
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,3)
   lineA$="Hey, "+charName$(gamChar)+", you've got to help"
   lineB$="me get "+charName$(negVariable)+" off my back!"
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,1)
   lineA$=He$(g)+"'s kicking my ass in AND out of the ring!"
   lineB$="The asshole thinks "+Lower$(He$(g))+" rules this place..."
  EndIf
  If negStage=0 And negTim>675 And negTim<975
   Speak(2,0,2)
   lineA$="But surely you can take "+Him$(g)+" down, right?"
   lineB$="I'll pay you $"+GetFigure$(negPayOff)+" if you knock "+Him$(g)+" out!"
  EndIf
  If negStage=0 And negTim>975 Then camFoc=1
  If negStage=0 And negTim>1000 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,3)
   If charged=0 
    PlaySound sCash : charBank(gamChar)=charBank(gamChar)+negPayOff
    ChangeRelationship(gamChar,negChar,1)
    ChangeRelationship(gamChar,negVariable,-1)
    AdjustAttitude(gamChar,-1) : charged=1
   EndIf
   lineA$="Thanks, you just saved my life! It'll be worth"
   lineB$="every penny to see that thug taught a lesson..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(2,0,1)
   If charged=0 Then ChangeRelationship(gamChar,negChar,-1) : AdjustAttitude(gamChar,1) : charged=1
   lineA$="You're just going to leave me to suffer?! Well"
   lineB$="don't come crying to me when it's YOU next..."
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;23. FOREIGN INVASION
 If negTopic=23
  optionA$="Yes, fight them off!" : optionB$="No, stay away..."
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,3)
   lineA$="Hey, "+charName$(gamChar)+", have you heard that"
   lineB$=fedName$(negVariable)+" invaded the building?!"
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,1)
   lineA$="They forced their way into "+charName$(fedBooker(fed))+"'s"
   lineB$="office and now they're tearing up the arena!"
  EndIf
  If negStage=0 And negTim>675 And negTim<975
   Speak(2,0,2)
   lineA$="Come on, we've got to stop them before they"
   lineB$="ruin tonight's show! Are you gonna help?"
  EndIf
  If negStage=0 And negTim>975 Then camFoc=1
  If negStage=0 And negTim>1000 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,3)
   If charged=0
    ProduceSound(0,sCrowd(6),0,0.5)
    ChangeRelationship(gamChar,negChar,1)
    AdjustAttitude(gamChar,2) : charged=1
   EndIf
   lineA$="Thank God! Now let's get out there and show those"
   lineB$="assholes what "+fedName$(fed)+" is made of!"
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(2,0,1)
   If charged=0
    ProduceSound(0,sCrowd(3),0,0.5)
    gamSchedule(gamDate)=0
    ChangeRelationship(gamChar,negChar,-1)
    AdjustAttitude(gamChar,-2) : charged=1
   EndIf 
   lineA$="NO?! Well that's tonight's show screwed then!"
   lineB$="They're going to tear this place to the ground..."
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;24. RIOT FOR BETTER PAY!
 If negTopic=24
  optionA$="Yes, cause a riot!" : optionB$="No, forget it..."
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,1)
   lineA$="Hey, "+charName$(gamChar)+", don't you feel cheated by"
   lineB$="the payment here at "+fedName$(fed)+"?"
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,1) : g=charGender(fedBooker(fed))
   lineA$="While we risk our lives, "+charName$(fedBooker(fed))
   lineB$="is getting rich by sitting on "+Lower$(His$(g))+" ass!"
  EndIf
  If negStage=0 And negTim>675 And negTim<975
   Speak(2,0,2)
   lineA$="Well, some of us have had enough and we're going"
   lineB$="to riot for better pay! Can we count you in?"
  EndIf
  If negStage=0 And negTim>975 Then camFoc=1
  If negStage=0 And negTim>1000 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,1)
   If charged=0 Then ChangeRelationship(gamChar,negChar,1) : AdjustAttitude(gamChar,-1) : charged=1
   lineA$="Way to go! Come on, let's tear this place up"
   lineB$="until the suits upstairs give us what we want!"
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(2,0,1)
   If charged=0 Then ChangeRelationship(gamChar,negChar,-1) : AdjustAttitude(gamChar,1) : charged=1
   lineA$="I take it you're happy with your $"+GetFigure$(charSalary(gamChar))+" per week?"
   lineB$="If you're that naive, I guess it's what you deserve!"
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;25. INTER-PROMOTIONAL AFTERMATH
 If negTopic=25
  optionA$="Yes, accept challenge!" : optionB$="No, we're done..."
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,1)
   lineA$="Hey, "+charName$(gamChar)+", we're not done with"
   lineB$="you yet! The fans are waiting for more..."
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,1)
   lineA$="What we just had was merely a 'battle'."
   lineB$="The WAR is still left to be fought!"
  EndIf
  If negStage=0 And negTim>675 And negTim<975
   Speak(2,0,1)
   lineA$="Why don't you rally your pathetic troops"
   lineB$="and face us in one final TEAM contest?"
  EndIf
  If negStage=0 And negTim>975 Then camFoc=1
  If negStage=0 And negTim>1000 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,1)
   If charged=0 Then ChangeRelationship(gamChar,negChar,-1) : AdjustAttitude(gamChar,2) : charged=1
   lineA$="Every match so far means NOTHING compared"
   lineB$="to this! Get ready for the main event..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(2,0,1)
   If charged=0
    fedPopularity(fed)=fedPopularity(fed)-1 : fedReputation(fed)=fedReputation(fed)-1
    fedPopularity(charFed(negChar))=fedPopularity(charFed(negChar))+1 : fedReputation(charFed(negChar))=fedReputation(charFed(negChar))+1
    ChangeRelationship(gamChar,negChar,-1)
    AdjustAttitude(gamChar,-2) : charged=1
   EndIf
   lineA$="Yeah, you better run away while you still can!"
   lineB$="Nobody can defeat "+fedName$(charFed(negChar))+"..."
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;26. INVASION LEADS TO INTER-PROMOTIONAL
 If negTopic=26
  optionA$="Yes, accept challenge!" : optionB$="No, forget it..."
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,2)
   lineA$="Hey, "+charName$(gamChar)+", I'm sure you're wondering how"
   lineB$="a "+fedName$(charFed(negChar))+" star got onto your show?"
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,1)
   lineA$="Well that was NOTHING! I came here to challenge"
   lineB$=fedName$(fed)+" to an inter-promotional contest!"
  EndIf
  If negStage=0 And negTim>675 And negTim<975
   Speak(2,0,1)
   lineA$="If you've got any dignity left, you'll step into"
   lineB$="the ring with us next month for a REAL showdown?"
  EndIf
  If negStage=0 And negTim>975 Then camFoc=1
  If negStage=0 And negTim>1000 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,1)
   If charged=0 
    PlaySound sCrowd(6)
    gamSchedule(gamDate+4)=4
    gamRivalFed(gamDate+4)=charFed(negChar)
    ChangeRelationship(gamChar,negChar,-1)
    AdjustAttitude(gamChar,2) : charged=1
   EndIf
   lineA$="Then it's settled! In just a few short weeks,"
   lineB$=fedName$(charFed(negChar))+" will destroy "+fedName$(fed)+"!"
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(2,0,1)
   If charged=0
    fedPopularity(fed)=fedPopularity(fed)-1 : fedReputation(fed)=fedReputation(fed)-1
    fedPopularity(charFed(negChar))=fedPopularity(charFed(negChar))+1 : fedReputation(charFed(negChar))=fedReputation(charFed(negChar))+1
    ChangeRelationship(gamChar,negChar,-1)
    AdjustAttitude(gamChar,-2) : charged=1
   EndIf
   lineA$="Ha, we should have known you wouldn't put up a fight!"
   lineB$="You're not in the same league as "+fedName$(charFed(negChar))+"..."
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf 
 ;27. BOOKER SUGGEST COSTUME CHANGE
 If negTopic=27
  optionA$="Yes, change costume!" : optionB$="No, take it off..."
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,3)
   lineA$="Hey, "+charName$(gamChar)+", what do you think of the"
   lineB$="new costume that I put together for you?"
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,3)
   lineA$="It's just what you need to reinvent yourself!"
   lineB$="How would you feel about wearing it in the ring?"
  EndIf
  If negStage=0 And negTim>650 Then camFoc=1
  If negStage=0 And negTim>675 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,3)
   If charged=0 Then LearnCostumes(gamChar,2) : AdjustAttitude(gamChar,1) : gamAgreement(3)=4 : charged=1
   lineA$="I'm glad you share my taste! If you like that,"
   lineB$="wait until you see the other outfits I got you..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1 And charClause(gamChar,1)>0
   Speak(2,0,1)
   If charged=0 
    CopyChar(0,gamChar)
    AdjustAttitude(gamChar,-1) : charged=1
   EndIf
   lineA$="You obviously don't know anything about fashion!"
   lineB$="The fans would love to see you in this outfit..."
  EndIf
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1 And charClause(gamChar,1)=0
   Speak(2,0,1)
   If charged=0 Then LearnCostumes(gamChar,2) : AdjustAttitude(gamChar,-2) : gamAgreement(3)=4 : charged=1
   lineA$="Sorry, did I make it sound like you had a choice?"
   lineB$="You'll wear what I tell you to, so get used to it!"
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;28. BOOKER SUGGESTS HAIRSTYLE CHANGE
 If negTopic=28
  execute=0
  optionA$="Yes, keep hairstyle!" : optionB$="No, change it back..."
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,3)
   lineA$="Hey, "+charName$(gamChar)+", what do you think of the"
   lineB$="new hairstyle that I put together for you?"
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,3)
   lineA$="It's just what you need to reinvent yourself!"
   lineB$="How would you feel about keeping it like that?"
  EndIf
  If negStage=0 And negTim>650 Then camFoc=1
  If negStage=0 And negTim>675 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,3)
   If charged=0 Then execute=1 : AdjustAttitude(gamChar,1) : charged=1
   lineA$="I'm glad you like my style! Once we get out of the"
   lineB$="wrestling business, we should open our own barber shop..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1 And charClause(gamChar,1)>0
   Speak(2,0,1)
   If charged=0 
    CopyChar(0,gamChar)
    AdjustAttitude(gamChar,-1) : charged=1
   EndIf
   lineA$="You obviously don't know anything about style!"
   lineB$="Everybody will be wearing it like that soon..."
  EndIf
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1 And charClause(gamChar,1)=0
   Speak(2,0,1)
   If charged=0 Then execute=1 : AdjustAttitude(gamChar,-2) : charged=1
   lineA$="Well it's too late for that now, isn't it? You should"
   lineB$="have said something while I was cutting your hair!"
  EndIf
  If negStage=2 And negTim>375 Then go=1 
  ;execute changes
  If execute=1
   For coz=1 To 3
    charHatStyle(gamChar,coz)=charHatStyle(0,coz)
   Next 
   gamAgreement(2)=4
  EndIf
 EndIf
 ;29. BOOKER SUGGESTS SOLO NAME CHANGE
 If negTopic=29
  execute=0
  optionA$="Yes, change name!" : optionB$="No, forget it..."
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,2)
   lineA$="Listen, "+charName$(gamChar)+", have you ever considered"
   lineB$="wrestling under a different name?"
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,3)
   lineA$="How would you feel about something like"
   lineB$="'"+charName$(0)+"'? Doesn't that sound cool?!"
  EndIf
  If negStage=0 And negTim>650 Then camFoc=1
  If negStage=0 And negTim>675 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,3)
   If charged=0 Then execute=1 : AdjustAttitude(gamChar,1) : charged=1
   lineA$="I'm glad you agree! You should have much more"
   lineB$="success under the identity of '"+charName$(0)+"'..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1 And charClause(gamChar,1)>0
   Speak(2,0,1)
   If charged=0 Then AdjustAttitude(gamChar,-1) : charged=1
   lineA$="You obviously don't know much about marketing!"
   lineB$="It's impossible to do business with you morons..."
  EndIf
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1 And charClause(gamChar,1)=0
   Speak(2,0,1)
   If charged=0 Then execute=1 : AdjustAttitude(gamChar,-2) : charged=1
   lineA$="Sorry, did I make it sound like you had a choice?"
   lineB$="Your name is '"+charName$(0)+"' - so get used to it!"
  EndIf
  If negStage=2 And negTim>375 Then go=1 
  ;execute changes
  If execute=1
   charName$(gamChar)=charName$(0) : gamAgreement(1)=4
   charTeamName$(gamChar)=charName$(0)+"'s Team"
  EndIf
 EndIf
 ;30. BOOKER SUGGESTS TEAM NAME CHANGE
 If negTopic=30
  execute=0
  optionA$="Yes, change name!" : optionB$="No, forget it..."
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,2)
   lineA$="Listen, have you and "+charName$(charPartner(gamChar))+" ever"
   lineB$="considered wrestling under a different name?"
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,3)
   lineA$="How would you feel about something like"
   lineB$="'"+charTeamName$(0)+"'? Doesn't that sound cool?!"
  EndIf
  If negStage=0 And negTim>650 Then camFoc=1
  If negStage=0 And negTim>675 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,3)
   If charged=0 Then execute=1 : AdjustAttitude(gamChar,1) : charged=1
   lineA$="I'm glad you agree! You should have much more"
   lineB$="success under the identity of '"+charTeamName$(0)+"'..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1 And charClause(gamChar,1)>0
   Speak(2,0,1)
   If charged=0 Then AdjustAttitude(gamChar,-1) : charged=1
   lineA$="You obviously don't know much about marketing!"
   lineB$="It's impossible to do business with you morons..."
  EndIf
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1 And charClause(gamChar,1)=0
   Speak(2,0,1)
   If charged=0 Then execute=1 : AdjustAttitude(gamChar,-2) : charged=1
   lineA$="Sorry, did I make it sound like you had a choice?"
   lineB$="The name is '"+charTeamName$(0)+"' - so get used to it!"
  EndIf
  If negStage=2 And negTim>375 Then go=1 
  ;execute changes
  If execute=1
   charTeamName$(gamChar)=charTeamName$(0) : gamAgreement(1)=4
   charTeamName$(charPartner(gamChar))=charTeamName$(gamChar)
  EndIf
 EndIf
 ;31. BOOKER SUGGESTS ENTRANCE CHANGE
 If negTopic=31
  execute=0
  optionA$="Yes, change entrance!" : optionB$="No, forget it..."
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,3)
   lineA$="Hey, "+charName$(gamChar)+", I've discovered a great piece"
   lineB$="of music that you could make your entrance to!"
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,3)
   lineA$="Do you want to give it a try in your next match?"
   lineB$="We could even update the lighting effects too!"
  EndIf
  If negStage=0 And negTim>650 Then camFoc=1
  If negStage=0 And negTim>675 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,3)
   If charged=0 Then execute=1 : AdjustAttitude(gamChar,1) : charged=1
   lineA$="I'm glad you're willing to give it a try!"
   lineB$="I'll pass the CD on to the sound engineers..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1 And charClause(gamChar,1)>0
   Speak(2,0,1)
   If charged=0 Then AdjustAttitude(gamChar,-1) : charged=1
   lineA$="You obviously don't know anything about music!"
   lineB$="This tune would have made you down with the kids..."
  EndIf
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1 And charClause(gamChar,1)=0
   Speak(2,0,1)
   If charged=0 Then execute=1 : AdjustAttitude(gamChar,-2) : charged=1
   lineA$="Sorry, did I make it sound like you had a choice?"
   lineB$="The sound engineers are queuing it up as we speak!"
  EndIf
  If negStage=2 And negTim>375 Then go=1 
  ;execute changes
  If execute=1
   charTheme(gamChar)=Rnd(1,no_themes)
   charThemePitch(gamChar)=22050
   charLight(gamChar)=Rnd(1,no_lightshows)
   LearnCostumes(gamChar,2) : gamAgreement(4)=4
  EndIf
 EndIf
 ;32. BOOKER SUGGESTS ATTACKS CHANGE
 If negTopic=32
  execute=0
  optionA$="Yes, change attacks!" : optionB$="No, forget it..."
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,2)
   lineA$="Listen, "+charName$(gamChar)+", I think your act"
   lineB$="is in danger of becoming stale..."
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,3)
   lineA$="How would you feel about using some new attacks?"
   lineB$="It could be just what you need to make an impact!"
  EndIf
  If negStage=0 And negTim>650 Then camFoc=1
  If negStage=0 And negTim>675 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,3)
   If charged=0 Then execute=1 : AdjustAttitude(gamChar,1) : charged=1
   lineA$="Great, I've got just the right moves for you!"
   lineB$="Have a sparring session and see what you think..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1 And charClause(gamChar,1)>0
   Speak(2,0,1)
   If charged=0 Then AdjustAttitude(gamChar,-1) : charged=1
   lineA$="You could have at least given it a try!"
   lineB$="You'll never evolve with that attitude..."
  EndIf
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1 And charClause(gamChar,1)=0
   Speak(2,0,1)
   If charged=0 Then execute=1 : AdjustAttitude(gamChar,-2) : charged=1
   lineA$="Sorry, did I make it sound like you had a choice?"
   lineB$="You've got some new attacks, so get used to them!"
  EndIf
  If negStage=2 And negTim>375 Then go=1 
  ;execute changes
  If execute=1
   Repeat 
    idol=Rnd(1,no_chars)
   Until idol<>gamChar
   For count=1 To 5
    charAttack(gamChar,count)=charAttack(idol,count)
    charCrush(gamChar,count)=charCrush(idol,count)
   Next
   LearnMoves(gamChar,2) : gamAgreement(5)=4
  EndIf
 EndIf
 ;33. BOOKER SUGGESTS NEW MOVES
 If negTopic=33
  execute=0
  optionA$="Yes, change moves!" : optionB$="No, forget it..."
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,2)
   lineA$="Listen, "+charName$(gamChar)+", I think your act"
   lineB$="is in danger of becoming stale..."
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,3)
   lineA$="How would you feel about incorporating some new"
   lineB$="moves? It could be just what your career needs!"
  EndIf
  If negStage=0 And negTim>650 Then camFoc=1
  If negStage=0 And negTim>675 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,3)
   If charged=0 Then execute=1 : AdjustAttitude(gamChar,1) : charged=1
   lineA$="Great, I've got just the right moves for you!"
   lineB$="Have a sparring session and see what you think..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1 And charClause(gamChar,1)>0
   Speak(2,0,1)
   If charged=0 Then AdjustAttitude(gamChar,-1) : charged=1
   lineA$="You could have at least given it a try!"
   lineB$="You'll never evolve with that attitude..."
  EndIf
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1 And charClause(gamChar,1)=0
   Speak(2,0,1)
   If charged=0 Then execute=1 : AdjustAttitude(gamChar,-2) : charged=1
   lineA$="Sorry, did I make it sound like you had a choice?"
   lineB$="You've got some new moves, so get used to them!"
  EndIf
  If negStage=2 And negTim>375 Then go=1 
  ;execute changes
  If execute=1
   Repeat 
    idol=Rnd(1,no_chars)
   Until idol<>gamChar
   For count=1 To 15
    charMove(gamChar,count)=charMove(idol,count)
   Next
   For count=1 To 6
    charGroundMove(gamChar,count)=charGroundMove(idol,count)
   Next
   LearnMoves(gamChar,2) : gamAgreement(6)=4
  EndIf
 EndIf
 ;34. BOOKER SUGGESTS NEW GESTURES
 If negTopic=34
  execute=0
  optionA$="Yes, change style!" : optionB$="No, forget it..."
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,2)
   lineA$="Listen, "+charName$(gamChar)+", have you ever considered"
   lineB$="changing the way you carry yourself in the ring?"
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,3)
   lineA$="You can change your whole outlook with a new"
   lineB$="stance and few new gestures here and there!"
  EndIf
  If negStage=0 And negTim>675 And negTim<975
   Speak(2,0,3)
   lineA$="I've got some ideas about what you could do."
   lineB$="Do you want me to run through them with you?"
  EndIf
  If negStage=0 And negTim>975 Then camFoc=1
  If negStage=0 And negTim>1000 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,3)
   If charged=0 Then execute=1 : AdjustAttitude(gamChar,1) : charged=1
   lineA$="Good for you! You need to keep reinventing yourself"
   lineB$="to remain relevant in any business these days..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1 And charClause(gamChar,1)>0
   Speak(2,0,1)
   If charged=0 Then AdjustAttitude(gamChar,-1) : charged=1
   lineA$="You obviously don't know much about psychology!"
   lineB$="Predictable opponents are the weakest of them all..."
  EndIf
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1 And charClause(gamChar,1)=0
   Speak(2,0,1)
   If charged=0 Then execute=1 : AdjustAttitude(gamChar,-2) : charged=1
   lineA$="Sorry, did I make it sound like you had a choice?"
   lineB$="You'll do what I tell you to, so pay attention!"
  EndIf
  If negStage=2 And negTim>375 Then go=1 
  ;execute changes
  If execute=1
   Repeat 
    idol=Rnd(1,no_chars)
   Until idol<>gamChar
   charStance(gamChar)=Rnd(1,no_stances)
   For count=1 To 4
    charTaunt(gamChar,count)=charTaunt(idol,count)
   Next
   LearnMoves(gamChar,2) : gamAgreement(7)=4
  EndIf
 EndIf
 ;35. PARTNER WANTS TO SYNCHRONIZE COSTUMES
 If negTopic=35
  optionA$="Yes, change costume!" : optionB$="No, take it off..."
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,2)
   lineA$="Hey, "+charName$(gamChar)+", we're supposed to be a"
   lineB$="team - but you wouldn't know it to look at us!"
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,3)
   lineA$="Why don't you change your costume to match mine?"
   lineB$="Perhaps then we'll look as good as we perform!"
  EndIf
  If negStage=0 And negTim>650 Then camFoc=1
  If negStage=0 And negTim>675 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,3)
   If charged=0 Then LearnCostumes(gamChar,2) : AdjustAttitude(gamChar,1) : gamAgreement(3)=4 : charged=1
   lineA$="I'm glad you share my taste! If you like that,"
   lineB$="you should copy the way I dress backstage too..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(2,0,1)
   If charged=0 
    CopyChar(0,gamChar)
    charAttitude(negChar)=charAttitude(negChar)-1
    charHappiness(negChar)=charHappiness(negChar)-1
    AdjustAttitude(gamChar,-1) : charged=1
   EndIf
   lineA$="You obviously don't know anything about fashion!"
   lineB$="Everybody knows I'm the style icon around here..."
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;36. PARTNER SUGGESTS TEAM NAME CHANGE
 If negTopic=36
  optionA$="Yes, change name!" : optionB$="No, forget it..."
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,2)
   lineA$="Listen, I'm not sure '"+charTeamName$(gamChar)+"'"
   lineB$="sums up what this team is really about..."
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,3)
   lineA$="Why don't we change it to something like"
   lineB$="'"+charTeamName$(0)+"'? That'd be much better!"
  EndIf
  If negStage=0 And negTim>650 Then camFoc=1
  If negStage=0 And negTim>675 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,3)
   If charged=0
    charTeamName$(gamChar)=charTeamName$(0) : gamAgreement(1)=4
    charTeamName$(charPartner(gamChar))=charTeamName$(gamChar)
    AdjustAttitude(gamChar,1) : charged=1
   EndIf
   lineA$="I'm glad you agree! We should have much more"
   lineB$="success under the identity of '"+charTeamName$(0)+"'..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(2,0,1)
   If charged=0 
    CopyChar(0,gamChar)
    charAttitude(negChar)=charAttitude(negChar)-1
    charHappiness(negChar)=charHappiness(negChar)-1
    AdjustAttitude(gamChar,-1) : charged=1
   EndIf
   lineA$="You don't know much about marketing! A good name may"
   lineB$="not make you, but a bad one will certainly break you..."
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;37. BOOKER ASKS YOU TO PAY FOR DAMAGES
 If negTopic=37
  optionA$="Yes, pay $"+GetFigure$(negPayOff)+"..." : optionB$="No, forget it!"
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,1)
   lineA$="For the love of God, "+charName$(gamChar)+"!"
   lineB$="You made a hell of a mess out there tonight..."
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,1)
   lineA$="We all like to see an entertaining match, but"
   lineB$=fedName$(charFed(negChar))+" has to pay for that stuff!"
  EndIf
  If negStage=0 And negTim>675 And negTim<975
   Speak(2,0,1)
   lineA$="Before the venue sues us for damages, I figure"
   lineB$="we can put this mess behind us for $"+GetFigure$(negPayOff)+"?"
  EndIf
  If negStage=0 And negTim>975 Then camFoc=1
  If negStage=0 And negTim>1000 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,2)
   If charged=0
    PlaySound sCash : charBank(gamChar)=charBank(gamChar)-negPayOff
    AdjustAttitude(gamChar,1) : charged=1
   EndIf
   lineA$="Thanks, I'll pass this onto the arena staff."
   lineB$="Try to be more responsible in future though..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(2,0,1)
   If charged=0 
    ChangeRelationship(negChar,gamChar,-1)
    gamSchedule(gamDate+1)=0 : gamSchedule(gamDate+2)=0
    AdjustAttitude(gamChar,-2) : charged=1
   EndIf
   lineA$="Well, if you can't be trusted in the ring"
   lineB$="then I'll just have to keep you out of it!"
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;38. BOOKER ASKS YOU TO WORK THROUGH INJURY
 If negTopic=38
  optionA$="Yes, accept $"+GetFigure$(negPayOff)+"!" : optionB$="No, forget it..."
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,3)
   lineA$="Hey, "+charName$(gamChar)+", I know you're nursing"
   lineB$="an injury - but I've got a proposal for you..."
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,3)
   lineA$="The card is a little light for tonight's show, so"
   lineB$="how would you feel about forcing yourself out there?"
  EndIf
  If negStage=0 And negTim>675 And negTim<975
   Speak(2,0,3)
   lineA$="I know it's the last thing on your mind, but"
   lineB$="there's $"+GetFigure$(negPayOff)+" in it for you if you perform?"
  EndIf
  If negStage=0 And negTim>975 Then camFoc=1
  If negStage=0 And negTim>1000 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,3)
   If charged=0
    PlaySound sCash : charBank(gamChar)=charBank(gamChar)+negPayOff
    If gamSchedule(gamDate)<1 Then gamSchedule(gamDate)=1
    gamAgreement(10)=1
    charPopularity(gamChar)=charPopularity(gamChar)+1
    AdjustAttitude(gamChar,1) : charged=1
   EndIf
   lineA$="Thanks, you just saved tonight's show! Nobody's"
   lineB$="expecting much from you. Just show your face..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1 And charClause(gamChar,1)>0
   Speak(2,0,2)
   If charged=0 Then AdjustAttitude(gamChar,-1) : charged=1
   lineA$="Alright, I suspected it was too much to ask."
   lineB$="Take it easy and come back when you're ready..."
  EndIf
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1 And charClause(gamChar,1)=0
   Speak(2,0,2)
   If charged=0
    If gamSchedule(gamDate)<1 Then gamSchedule(gamDate)=1
    gamAgreement(10)=1 
    AdjustAttitude(gamChar,-2) : charged=1
   EndIf
   lineA$="Sorry, did I make it sound like you had a choice?"
   lineB$="We need you to work, so drag your ass out there!"
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;39. BOOKER FORCES YOU TO WORK THROUGH INJURY
 If negTopic=39
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,2)
   lineA$="Poor, injured "+charName$(gamChar)+" - do take a seat."
   lineB$="I think we need to talk about your health..."
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,1)
   lineA$="Let's start with why you're pretending to be hurt?!"
   lineB$="I know when somebody is trying to get out of work!"
  EndIf
  If negStage=0 And negTim>675 And negTim<975
   Speak(2,0,1)
   lineA$="Well, the charade ends tonight! You're wrestling no"
   lineB$="matter what. Then we'll see how 'injured' you are..."
  EndIf
  If negStage=0 And negTim>1025
   charHappiness(gamChar)=charHappiness(gamChar)-1
   gamAgreement(10)=1 : go=1 
  EndIf
 EndIf
 ;40. WRESTLER OFFERS PAIN RELIEF
 If negTopic=40
  optionA$="Yes, use painkillers!" : optionB$="No thanks..."
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,1)
   lineA$="Sorry, "+charName$(gamChar)+", but you look like crap!"
   lineB$="It's obvious you've been working too hard..."
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,2)
   lineA$="This may be a tough business, but you don't"
   lineB$="have to go through it in pain all the time..."
  EndIf
  If negStage=0 And negTim>675 And negTim<975
   Speak(2,0,3)
   lineA$="A few painkillers can make you feel great!"
   lineB$="I can hook you up right now for just $"+GetFigure$(negPayOff)+"?"
  EndIf
  If negStage=0 And negTim>975 Then camFoc=1
  If negStage=0 And negTim>1000 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,2)
   If charged=0
    PlaySound sCash : charBank(gamChar)=charBank(gamChar)-negPayOff
    ChangeRelationship(gamChar,negChar,1) 
    charHealth(gamChar)=100 : gamAgreement(14)=2
    randy=Rnd(0,2)
    If randy=0 And fed=<6 And gamPromo(gamDate)=0 Then gamPromo(gamDate)=97 
    AdjustAttitude(gamChar,-1) : charged=1
   EndIf
   lineA$="Good for you! Just stick this needle in your"
   lineB$="arm and you'll be wrestling all night..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(2,0,1)
   If charged=0 Then AdjustAttitude(gamChar,1) : charged=1
   lineA$="Hey, it's your choice - but how do you intend to"
   lineB$="compete with wrestlers that ARE using this stuff?!"
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;41. WRESTLER OFFERS STEROIDS
 If negTopic=41
  optionA$="Yes, use steroids!" : optionB$="No thanks..."
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,1)
   lineA$="Hey, "+charName$(gamChar)+", aren't you tired of busting"
   lineB$="your ass in the gym and getting nowhere?"
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,2)
   lineA$="So was I until I discovered performance enhancing"
   lineB$="drugs! That's what all the top athletes are using..."
  EndIf
  If negStage=0 And negTim>675 And negTim<975
   Speak(2,0,3)
   lineA$="I can hook you up with some steroids if you want?"
   lineB$="For just $"+GetFigure$(negPayOff)+", you could become better than ever!"
  EndIf
  If negStage=0 And negTim>975 Then camFoc=1
  If negStage=0 And negTim>1000 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,2)
   If charged=0
    PlaySound sCash : charBank(gamChar)=charBank(gamChar)-negPayOff
    ChangeRelationship(gamChar,negChar,1) 
    charStrength(gamChar)=charStrength(gamChar)+1
    charSkill(gamChar)=charSkill(gamChar)+1
    charAgility(gamChar)=charAgility(gamChar)+1
    charStamina(gamChar)=charStamina(gamChar)+1
    charToughness(gamChar)=charToughness(gamChar)+1
    charWeightChange(gamChar)=charWeightChange(gamChar)+Rnd(1,5)
    gamAgreement(13)=2
    randy=Rnd(0,2)
    If randy=0 And fed=<6 And gamPromo(gamDate)=0 Then gamPromo(gamDate)=97 
    AdjustAttitude(gamChar,-1) : charged=1
   EndIf
   lineA$="Good for you! You should feel the results"
   lineB$="in no time... and so will your opponents!"
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(2,0,1)
   If charged=0 Then AdjustAttitude(gamChar,1) : charged=1
   lineA$="Hey, it's your choice - but how do you intend to"
   lineB$="compete with wrestlers that ARE using this stuff?!"
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;42. BOOKER CONFRONTS ABOUT DRUGS
 If negTopic=42
  optionA$="Pay $"+GetFigure$(negPayOff)+" fine!" : optionB$="Serve suspension..."
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,2)
   lineA$="Listen, "+charName$(gamChar)+", it's been brought to"
   lineB$="my attention that you've been using drugs?"
   If negTim>150 Then ChannelPitch chTheme,PercentOf#(chThemePitch,90)
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,1)
   lineA$="I know this life is hard, but our policy does"
   lineB$="not tolerate the use of drugs of any kind!"
  EndIf
  If negStage=0 And negTim>675 And negTim<975
   Speak(2,0,1)
   lineA$="Before this gets blown out of proportion, I"
   lineB$="have to be seen to be doing something about it..."
  EndIf
  If negStage=0 And negTim>1000 And negTim<1300
   Speak(2,0,2)
   lineA$="I'm supposed to suspend you for a month, but I"
   lineB$="may be able to let you off with a $"+GetFigure$(negPayOff)+" fine?"
  EndIf
  If negStage=0 And negTim>1300 Then camFoc=1
  If negStage=0 And negTim>1325 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,1)
   If charged=0
    PlaySound sCash : charBank(gamChar)=charBank(gamChar)-negPayOff
    charPopularity(gamChar)=charPopularity(gamChar)-1 
    If fed=<6 And gamPromo(gamDate)=0 Then gamPromo(gamDate)=97 
    AdjustAttitude(gamChar,-2) : charged=1
   EndIf
   lineA$="OK, get your wallet out and we'll make this problem"
   lineB$="go away - but it won't buy you an awful lot of respect!"
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(2,0,2)
   If charged=0 
    gamSchedule(gamDate)=0 : gamSchedule(gamDate+1)=0
    gamSchedule(gamDate+2)=0 : gamSchedule(gamDate+3)=0
    AdjustAttitude(gamChar,1) : charged=1
   EndIf
   lineA$="OK, that's probably for the best. The fans will"
   lineB$="soon forgive you if you're seen to be suffering..."
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;43. OPPONENT OFFERS TO LAY DOWN
 If negTopic=43
  optionA$="Yes, pay $"+GetFigure$(negPayOff)+"!" : optionB$="No, forget it..."
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,2)
   lineA$="Hey, "+charName$(gamChar)+", did you know that"
   lineB$="I'm booked to wrestle you tonight?"
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,3)
   lineA$="Well, fortunately for you, I need money - so I"
   lineB$="could be persuaded to lose if the price was right?"
  EndIf
  If negStage=0 And negTim>675 And negTim<975
   Speak(2,0,3)
   lineA$="Just give me $"+GetFigure$(negPayOff)+" and the match is yours."
   lineB$="It's a small price to pay for an easy win!"
  EndIf
  If negStage=0 And negTim>975 Then camFoc=1
  If negStage=0 And negTim>1000 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,3)
   If charged=0
    PlaySound sCash : charBank(gamChar)=charBank(gamChar)-negPayOff
    ChangeRelationship(gamChar,negChar,1) : gamAgreement(11)=1
    gamMatch(gamDate)=2 : gamGimmick(gamDate)=0 : gamOpponent(gamDate)=negChar
    AdjustAttitude(gamChar,-1) : charged=1
   EndIf
   lineA$="Excellent! This should be the easiest money I've"
   lineB$="ever earned and the easiest match you've ever had!"
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(2,0,1)
   If charged=0
    ChangeRelationship(gamChar,negChar,-1)
    gamMatch(gamDate)=2 : gamGimmick(gamDate)=0 : gamOpponent(gamDate)=negChar
    AdjustAttitude(gamChar,1) : charged=1
   EndIf
   lineA$="Fine, we'll see what happens when I give you my all!"
   lineB$="Instead of costing you money, I'll cost you your career..."
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;44. OPPONENT ASKS YOU TO LAY DOWN
 If negTopic=44
  optionA$="Yes, accept $"+GetFigure$(negPayOff)+"!" : optionB$="No, forget it..."
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,2)
   lineA$="Hey, "+charName$(gamChar)+", did you know that"
   lineB$="I'm booked to wrestle you tonight?"
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,3)
   lineA$="Well, I'm going through a bad patch right now"
   lineB$="and I could really use an easy victory!"
  EndIf
  If negStage=0 And negTim>675 And negTim<975
   Speak(2,0,3)
   lineA$="I'd be willing to pay $"+GetFigure$(negPayOff)+" if you lie down"
   lineB$="and make sure the match goes my way?"
  EndIf
  If negStage=0 And negTim>975 Then camFoc=1
  If negStage=0 And negTim>1000 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,1)
   If charged=0
    PlaySound sCash : charBank(gamChar)=charBank(gamChar)+negPayOff
    ChangeRelationship(gamChar,negChar,1) : gamAgreement(12)=1
    gamMatch(gamDate)=2 : gamGimmick(gamDate)=0 : gamOpponent(gamDate)=negChar
    AdjustAttitude(gamChar,-1) : charged=1
   EndIf
   lineA$="Excellent! This should be easy money for you."
   lineB$="Just make sure your pride doesn't get in the way!"
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(2,0,1)
   If charged=0
    ChangeRelationship(gamChar,negChar,-1)
    gamMatch(gamDate)=2 : gamGimmick(gamDate)=0 : gamOpponent(gamDate)=negChar
    AdjustAttitude(gamChar,1) : charged=1
   EndIf
   lineA$="That could've been the easiest money you ever earned!"
   lineB$="An idiot like you can't be that hard to beat anyway..."
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;45. BOOKER OFFERS FAVOURABLE REF
 If negTopic=45
  optionA$="Yes, pay $"+GetFigure$(negPayOff)+"!" : optionB$="No, forget it..."
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,2)
   lineA$="Listen, "+charName$(gamChar)+", you must understand"
   lineB$="that I don't really care who 'wins' or 'loses'..."
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,3)
   lineA$="All I care about is keeping the money pumping"
   lineB$="through this company. Then we're ALL winners!"
  EndIf
  If negStage=0 And negTim>675 And negTim<975
   Speak(2,0,3)
   lineA$="For instance, you could slip me $"+GetFigure$(negPayOff)
   lineB$="and find yourself with a favourable referee?"
  EndIf
  If negStage=0 And negTim>975 Then camFoc=1
  If negStage=0 And negTim>1000 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,3)
   If charged=0
    PlaySound sCash : charBank(gamChar)=charBank(gamChar)-negPayOff
    ChangeRelationship(gamChar,negChar,1) : gamAgreement(15)=1
    AdjustAttitude(gamChar,-1) : charged=1
   EndIf
   lineA$="Thank you! That's a very generous donation!"
   lineB$="I'll make sure your referee returns the favour..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(2,0,1)
   If charged=0
    ChangeRelationship(gamChar,negChar,-1) : gamAgreement(16)=1
    AdjustAttitude(gamChar,1) : charged=1
   EndIf
   lineA$="Fine, have it your way! If you won't appreciate"
   lineB$="a favourable referee, perhaps your opponent will..."
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;46. ENCOURAGED TO SHOOT
 If negTopic=46
  optionA$="Yes, do a shoot promo!" : optionB$="No, follow the script..."
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,1)
   lineA$="Hey, "+charName$(gamChar)+", aren't you tired of"
   lineB$="reading "+charName$(fedBooker(fed))+"'s pathetic scripts?!"
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,2)
   lineA$="Remember the spotlight is YOURS once that microphone"
   lineB$="is in your hands! You could say anything you want..."
  EndIf
  If negStage=0 And negTim>675 And negTim<975
   Speak(2,0,3)
   lineA$="Why don't you say what you really think tonight?"
   lineB$="The controversy will make you more popular than ever!"
  EndIf
  If negStage=0 And negTim>975 Then camFoc=1
  If negStage=0 And negTim>1000 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,3)
   If charged=0
    gamPromo(gamDate)=47 : friend=0 : enemy=0
    For v=1 To no_chars
     If charFed(v)=<8 And charFed(v)<>charFed(gamChar)
      If charRealRelationship(gamChar,v)>0 Then friend=1
      If charRealRelationship(gamChar,v)<0 Then enemy=1
     EndIf
    Next
    randy=Rnd(0,2)
    If randy=1 And enemy>0 Then gamPromo(gamDate)=48
    If randy=2 And friend>0 Then gamPromo(gamDate)=49
    ChangeRelationship(gamChar,negChar,1)
    AdjustAttitude(gamChar,-1) : charged=1
   EndIf
   lineA$="I look forward to hearing what you have to say!"
   lineB$="This could be the defining moment of your career..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(2,0,1)
   If charged=0 Then AdjustAttitude(gamChar,1) : charged=1
   lineA$="I knew you wouldn't have the balls to be yourself!"
   lineB$="Enjoy spewing out those meaningless words tonight..."
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;47. INFERIOR FRIEND WANTS TO FIGHT
 If negTopic=47
  optionA$="Yes, fight friend!" : optionB$="No, forget it..."
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,1)
   lineA$="Hey, "+charName$(gamChar)+", have you heard what"
   lineB$="everybody has been saying about us?!"
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,1)
   lineA$="They say I'm a little poodle that follows you around"
   lineB$="and worships at your feet like some sort of God!"
  EndIf
  If negStage=0 And negTim>675 And negTim<975
   Speak(2,0,1)
   lineA$="Well, maybe they're right. I'm sick of walking"
   lineB$="in YOUR shadow! I need to make a name for myself..."
  EndIf
  If negStage=0 And negTim>1000 And negTim<1300
   Speak(2,0,1)
   lineA$="Take me on tonight, you son of a bitch! Then"
   lineB$="we'll see who the laughing stock is around here..."
  EndIf
  If negStage=0 And negTim>1300 Then camFoc=1
  If negStage=0 And negTim>1325 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,1)
   If charged=0
    gamMatch(gamDate)=2 : gamGimmick(gamDate)=0
    gamOpponent(gamDate)=negChar
    If fed=<6 Then gamPromo(gamDate)=70
    ChangeRelationship(negChar,gamChar,-1)
    AdjustAttitude(gamChar,-2) : charged=1
   EndIf
   lineA$="That's right - get used to following MY demands!"
   lineB$="Tonight I'm gonna prove that I'm better than you..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(2,0,1)
   If charged=0 Then ChangeRelationship(negChar,gamChar,-1) : AdjustAttitude(gamChar,1) : charged=1
   lineA$="You chicken! You know you could never beat me!"
   lineB$="You can't hide forever though. My time will come..."
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;48. SUPERIOR FRIEND WANTS TO FIGHT
 If negTopic=48
  optionA$="Yes, fight friend!" : optionB$="No, forget it..."
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,1)
   lineA$="Hey, "+charName$(gamChar)+", have you heard what"
   lineB$="everybody has been saying about us?!"
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,1)
   lineA$="They say I threw my career down the drain"
   lineB$="by letting a loser like YOU into my life!"
  EndIf
  If negStage=0 And negTim>675 And negTim<975
   Speak(2,0,1)
   lineA$="Well, maybe they're right. You've been nothing but"
   lineB$="trouble for me! I need to get back to the top..."
  EndIf
  If negStage=0 And negTim>1000 And negTim<1300
   Speak(2,0,1)
   lineA$="Since you ruined my career, the least you could do"
   lineB$="is let me use your ass as a stepping stone tonight?"
  EndIf
  If negStage=0 And negTim>1300 Then camFoc=1
  If negStage=0 And negTim>1325 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,1)
   If charged=0
    gamMatch(gamDate)=2 : gamGimmick(gamDate)=0
    gamOpponent(gamDate)=negChar
    If fed=<6 Then gamPromo(gamDate)=37
    ChangeRelationship(negChar,gamChar,-1)
    AdjustAttitude(gamChar,-2) : charged=1
   EndIf
   lineA$="That's right - get used to following MY demands!"
   lineB$="As of tonight, my name is going back on the map..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(2,0,1)
   If charged=0 Then ChangeRelationship(negChar,gamChar,-1) : AdjustAttitude(gamChar,1) : charged=1
   lineA$="Hey, you OWE me the right to kick your ass!"
   lineB$="Now I'll have to do it when you least suspect it..."
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;49. FRIEND SUGGESTS MARRIAGE
 If negTopic=49
  optionA$="Yes, get married!" : optionB$="No, remain single..."
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,3)
   lineA$="Hey, "+charName$(gamChar)+", I've really enjoyed your"
   lineB$="company here at "+fedName$(fed)+"!"
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,3)
   lineA$="I've never got on so well with anyone in my life!"
   lineB$="You're more than just a friend to me. I love you..."
  EndIf
  If negStage=0 And negTim>675 And negTim<975
   Speak(2,0,3)
   lineA$="And I'm pretty sure you love me too, so why don't"
   lineB$="we make it official and get married here tonight?"
  EndIf
  If negStage=0 And negTim>975 Then camFoc=1
  If negStage=0 And negTim>1000 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,3)
   If charged=0 
    FormTeam(gamChar,negChar)
    If charHappiness(negChar)<75 Then charHappiness(negChar)=75
    gamMatch(gamDate)=2 : gamGimmick(gamDate)=0
    gamOpponent(gamDate)=negChar : gamPromo(gamDate)=30
    AdjustAttitude(gamChar,2) : charged=1
   EndIf
   lineA$="Wonderful! I'm glad you feel the same way!"
   lineB$="Let's walk through this business side by side..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(2,0,1)
   If charged=0 Then ChangeRelationship(gamChar,negChar,-1) : AdjustAttitude(gamChar,-2) : charged=1
   lineA$="Well, if I'm not your lover then I'm nothing to you!"
   lineB$="If you don't want my love, you can have my HATE!"
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;50. ENEMY WANTS HAIR
 If negTopic=50
  optionA$="Yes, book the match!" : optionB$="No, forget it..."
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,1)
   lineA$="Hey, "+charName$(gamChar)+", the beef between"
   lineB$="us has gone far beyond the ring!"
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,1)
   lineA$="I don't just want to kick your ass anymore -"
   lineB$="I want to rip every hair out of your head!"
  EndIf
  If negStage=0 And negTim>675 And negTim<975
   Speak(2,0,1)
   lineA$="What would you say to a 'Hair Vs Hair' match"
   lineB$="next week? That's what everybody wants to see!"
  EndIf
  If negStage=0 And negTim>975 Then camFoc=1
  If negStage=0 And negTim>1000 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,1)
   If charged=0 
    gamMatch(gamDate+1)=2 : gamGimmick(gamDate+1)=9 : gamOpponent(gamDate+1)=negChar
    If fed=<6 Then gamPromo(gamDate+1)=69
    AdjustAttitude(gamChar,2) : charged=1
   EndIf
   lineA$="Haha, I can't wait! Not only do I get to beat"
   lineB$="you, but I get to humiliate you in the process!"
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(2,0,1)
   If charged=0 Then AdjustAttitude(gamChar,-2) : charged=1
   lineA$="Ha, I knew you were nothing but a coward!
   lineB$="I guess you know you wouldn't stand a chance..."
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;51. ENEMY WANTS CAREER
 If negTopic=51
  optionA$="Yes, book the match!" : optionB$="No, forget it..."
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,1)
   lineA$="Hey, "+charName$(gamChar)+", the beef between"
   lineB$="us has gone far beyond the ring!"
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,1)
   lineA$="I don't just want to kick your ass anymore -"
   lineB$="I want to bring an end to your sorry career!"
  EndIf
  If negStage=0 And negTim>675 And negTim<975
   Speak(2,0,1)
   lineA$="What do you say to a 'Loser Leaves Town' match"
   lineB$="next month? Then we'll never see each other again!"
  EndIf
  If negStage=0 And negTim>975 Then camFoc=1
  If negStage=0 And negTim>1000 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,1)
   If charged=0 
    gamMatch(gamDate+1)=2 : gamGimmick(gamDate+1)=10 : gamOpponent(gamDate+1)=negChar
    If fed=<6 Then gamPromo(gamDate+1)=68
    AdjustAttitude(gamChar,2) : charged=1
   EndIf
   lineA$="I guess you're not the coward I thought you were!"
   lineB$="Shame you just made the biggest mistake of your life..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(2,0,1)
   If charged=0 Then AdjustAttitude(gamChar,-2) : charged=1
   lineA$="Well, this show isn't big enough for the both of us!"
   lineB$="If you won't leave, I'll just have to hound you out..."
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;52. BOOKER OFFERS NIGHT OFF
 If negTopic=52
  optionA$="Yes, take a break..." : optionB$="No, keep working!"
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,1)
   lineA$="Hey, "+charName$(gamChar)+", you don't look at all well!"
   lineB$="Are you sure you're fit to wrestle tonight?"
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,3)
   lineA$="You're no good to anybody if you're tired."
   lineB$="Feel free to take the night off if you want?"
  EndIf
  If negStage=0 And negTim>650 Then camFoc=1
  If negStage=0 And negTim>675 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,2)
   If charged=0 Then gamSchedule(gamDate)=0 : AdjustAttitude(gamChar,-1) : charged=1
   lineA$="Alright, I'll find somebody to take your place."
   lineB$="All you have to worry about is getting better..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1 And charClause(gamChar,1)>0
   Speak(2,0,3)
   If charged=0 Then AdjustAttitude(gamChar,1) : charged=1
   lineA$="Are you sure? I admire your commitment, but"
   lineB$="I do hope it doesn't come back to haunt you!"
  EndIf
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1 And charClause(gamChar,1)=0
   Speak(2,0,1)
   If charged=0 Then gamSchedule(gamDate)=0 : AdjustAttitude(gamChar,1) : charged=1
   lineA$="Sorry, did I make it sound like you had a choice?"
   lineB$="I'm not having you stinking up my ring, so go home!"
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;53. BOOKER OFFERS CONTRACT TERMINATION
 If negTopic=53
  optionA$="Yes, terminate contract..." : optionB$="No, honour contract!"
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,1)
   lineA$="Listen, "+charName$(gamChar)+", I asked you in here"
   lineB$="today to discuss terminating your contract..."
   If negTim>150 Then ChannelPitch chTheme,PercentOf#(chThemePitch,90)
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,3)
   lineA$="It's not personal! It's just that business is"
   lineB$="down and we desperately need to cut costs..."
  EndIf 
  If negStage=0 And negTim>675 And negTim<975
   Speak(2,0,2)
   lineA$="I can't force you out - but if you'd like to"
   lineB$="leave on your terms, now would be a good time?"
  EndIf
  If negStage=0 And negTim>975 Then camFoc=1
  If negStage=0 And negTim>1000 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,3)
   If charged=0
    MoveChar(gamChar,7)
    ChangeRelationship(gamChar,negChar,1)
    AdjustAttitude(gamChar,1) : charged=1
   EndIf
   lineA$="Thanks for understanding the situation! Sorry"
   lineB$="it had to end this way, but it's for the best..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(2,0,1)
   If charged=0
    ChangeRelationship(gamChar,negChar,-1)
    If gamPromo(gamDate)=0 And fed=<6 Then gamPromo(gamDate)=7 
    AdjustAttitude(gamChar,-1) : charged=1
   EndIf
   lineA$="I knew you wouldn't understand! All you"
   lineB$="morons care about is milking me dry..."
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;54. BOOKER ASKS YOU TO TAKE PAY CUT
 If negTopic=54
  optionA$="Yes, take pay cut..." : optionB$="No, honour contract!"
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,2)
   lineA$="Listen, "+charName$(gamChar)+", I brought you in here"
   lineB$="today to ask you to consider taking a pay cut?"
   If negTim>150 Then ChannelPitch chTheme,PercentOf#(chThemePitch,90)
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,3)
   lineA$="It's not personal! It's just that business is"
   lineB$="down and we desperately need to cut costs..."
  EndIf 
  If negStage=0 And negTim>675 And negTim<975
   Speak(2,0,3)
   lineA$="I know it's a lot to ask, but we're going to need"
   lineB$="everybody to pitch in to keep this company afloat?"
  EndIf
  If negStage=0 And negTim>975 Then camFoc=1
  If negStage=0 And negTim>1000 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,3)
   If charged=0 
    PlaySound sCash : charSalary(gamChar)=RoundOff(charSalary(gamChar)/2,10)
    ChangeRelationship(gamChar,negChar,1)
    AdjustAttitude(gamChar,1) : charged=1
   EndIf
   lineA$="Thanks for being so understanding! You can rest"
   lineB$="assured that your sacrifice won't be forgotten..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(2,0,1)
   If charged=0
    ChangeRelationship(gamChar,negChar,-1)
    If gamPromo(gamDate)=0 And fed=<6 Then gamPromo(gamDate)=7 
    AdjustAttitude(gamChar,-1) : charged=1
   EndIf
   lineA$="I knew you wouldn't understand! All you"
   lineB$="morons care about is milking me dry..."
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;55. BOOKER ASKS YOUR OPINION ON INTERNAL WRESTLER
 If negTopic=55
  g=charGender(negVariable)
  optionA$="Yes, get rid of "+Him$(g)+"!" : optionB$="No, "+Lower$(He$(g))+"'s worth keeping..."
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,2)
   lineA$="Hi, "+charName$(gamChar)+", I asked you in here"
   lineB$="today to help me with a business decision..."
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,3)
   lineA$="What do you make of "+charName$(negVariable)+"?"
   lineB$="You must have come across "+Him$(g)+" backstage?"
  EndIf 
  If negStage=0 And negTim>675 And negTim<975
   Speak(2,0,2)
   lineA$="Well, we can't decide whether to keep "+Him$(g)+" or not."
   lineB$="What's your professional opinion on the matter?"
  EndIf
  If negStage=0 And negTim>975 Then camFoc=1
  If negStage=0 And negTim>1000 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,3)
   If charged=0 
    MoveChar(negVariable,7)
    ChangeRelationship(gamChar,negVariable,-1)
    AdjustAttitude(gamChar,-1) : charged=1
   EndIf
   lineA$="Really? Well, you'd know better than any of us!"
   lineB$=He$(g)+"'ll be gone by next week. Thanks for your input..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(2,0,1)
   If charged=0
    charSalary(negVariable)=RoundOff(PercentOf#(charSalary(negVariable),125),10)
    charContract(negVariable)=charContract(negVariable)+10
    ChangeRelationship(gamChar,negVariable,1) 
    AdjustAttitude(gamChar,2) : charged=1
   EndIf
   lineA$="Wow, "+Lower$(He$(g))+" must be good if a fellow wrestler says so!"
   lineB$="Most guys will tread over anybody to get to the top..."
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;56. BOOKER ASKS YOUR OPINION ON EXTERNAL WRESTLER
 If negTopic=56
  g=charGender(negVariable)
  optionA$="Yes, hire "+Him$(g)+"!" : optionB$="No, forget "+Him$(g)+"..."
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,2)
   lineA$="Hi, "+charName$(gamChar)+", I asked you in here"
   lineB$="today to help me with a business decision..."
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,3)
   lineA$="Have you ever heard of the wrestler, '"+charName$(negVariable)+"'?"
   lineB$="We could be signing "+Him$(g)+" from "+fedName$(charFed(negVariable))+"!"
  EndIf 
  If negStage=0 And negTim>675 And negTim<975
   Speak(2,0,2)
   lineA$="What's your professional opinion on the matter?"
   lineB$="Would "+Lower$(He$(g))+" fit in at "+fedName$(charFed(gamChar))+"?"
  EndIf
  If negStage=0 And negTim>975 Then camFoc=1
  If negStage=0 And negTim>1000 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,3)
   If charged=0 
    MoveChar(negVariable,charFed(gamChar))
    ChangeRelationship(gamChar,negVariable,1)
    AdjustAttitude(gamChar,2) : charged=1
   EndIf
   lineA$="Yeah, I always thought "+charName$(negVariable)+" belonged here!"
   lineB$="We'll break the bank to sign "+Him$(g)+" as soon as possible..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(2,0,1)
   If charged=0
    ChangeRelationship(gamChar,negVariable,-1) 
    AdjustAttitude(gamChar,-1) : charged=1
   EndIf
   lineA$="Really? Damn, I had a good feeling about that one!"
   lineB$="I guess we'll have to look elsewhere for new talent..."
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;57. BOOKER OFFERS WORLD TITLE PUSH
 If negTopic=57
  optionA$="Yes, pursue the title!" : optionB$="No thanks..."
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,3)
   lineA$="Listen, "+charName$(gamChar)+", I've been following your"
   lineB$="progress and I think you've got a bright future!"
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,3)
   lineA$="How would you like to compete for the World title?"
   lineB$="I think you could really liven up that scene!"
  EndIf 
  If negStage=0 And negTim>675 And negTim<975
   Speak(2,0,2)
   lineA$="It'd be tough working against "+charName$(fedChampWorld(fed))+","
   lineB$="but that's what champions are made of, right?"
  EndIf
  If negStage=0 And negTim>975 Then camFoc=1
  If negStage=0 And negTim>1000 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,3)
   If charged=0 
    ChangeRelationship(gamChar,fedChampWorld(fed),-1) : gamPromo(gamDate)=57
    AdjustAttitude(gamChar,2) : charged=1
   EndIf
   lineA$="Great, this could be the start of something big!"
   lineB$="We'll work to establish you as a title contender..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1 And charClause(gamChar,1)>0
   Speak(2,0,1)
   If charged=0 Then AdjustAttitude(gamChar,-2) : charged=1
   lineA$="You DON'T want to be the World Champion?!"
   lineB$="Then what the hell DO you want to achieve?!"
  EndIf 
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1 And charClause(gamChar,1)=0
   Speak(2,0,1)
   If charged=0
    ChangeRelationship(gamChar,fedChampWorld(fed),-1) : gamPromo(gamDate)=57 
    AdjustAttitude(gamChar,-2) : charged=1
   EndIf 
   lineA$="Sorry, did I make it sound like you had a choice?
   lineB$="The title scene needs you, so get used to the idea!"
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;58. BOOKER OFFERS INTER TITLE PUSH
 If negTopic=58
  optionA$="Yes, pursue the title!" : optionB$="No thanks..."
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,3)
   lineA$="Listen, "+charName$(gamChar)+", I've been following your"
   lineB$="progress and I think you've got a bright future!"
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,3)
   lineA$="How would you like to compete for the Inter title?"
   lineB$="I think you'd work well against "+charName$(fedChampInter(fed))+"!"
  EndIf 
  If negStage=0 And negTim>675 And negTim<975
   Speak(2,0,2)
   lineA$="I know it's not the best title to compete for,"
   lineB$="but surely ANY gold is better than nothing?"
  EndIf
  If negStage=0 And negTim>975 Then camFoc=1
  If negStage=0 And negTim>1000 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,3)
   If charged=0 
    ChangeRelationship(gamChar,fedChampInter(fed),-1) : gamPromo(gamDate)=58
    AdjustAttitude(gamChar,2) : charged=1
   EndIf
   lineA$="Great, this could be just what the division needs!"
   lineB$="We'll put you on the road to the title tonight..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1 And charClause(gamChar,1)>0
   Speak(2,0,1)
   If charged=0 Then AdjustAttitude(gamChar,-2) : charged=1
   lineA$="You don't want to be a champion of ANY kind?!"
   lineB$="Then why the hell do you bother turning up?!"
  EndIf
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1 And charClause(gamChar,1)=0
   Speak(2,0,1)
   If charged=0
    ChangeRelationship(gamChar,fedChampInter(fed),-1) : gamPromo(gamDate)=58 
    AdjustAttitude(gamChar,-2) : charged=1
   EndIf 
   lineA$="Sorry, did I make it sound like you had a choice?
   lineB$="The title scene needs you, so get used to the idea!"
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;59. BOOKER OFFERS TAG TITLE PUSH
 If negTopic=59
  execute=0
  optionA$="Yes, pursue the titles!" : optionB$="No thanks..."
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,3)
   lineA$="Listen, "+charName$(gamChar)+", I've been impressed with"
   lineB$="the way you and "+charName$(charPartner(gamChar))+" are progressing!"
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,3)
   lineA$="How would you like to compete for the Tag titles?"
   lineB$="You two could be just what the division needs!"
  EndIf 
  If negStage=0 And negTim>675 And negTim<975
   Speak(2,0,2)
   lineA$=charName$(fedChampTag(fed,1))+" and "+charName$(fedChampTag(fed,2))+" will work"
   lineB$="you hard, but surely the gold is worth it?"
  EndIf
  If negStage=0 And negTim>975 Then camFoc=1
  If negStage=0 And negTim>1000 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,3)
   If charged=0 Then execute=1 : AdjustAttitude(gamChar,2) : charged=1
   lineA$="Great, this could be the making of you both!"
   lineB$="We'll use tonight to get you in the hunt..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1 And charClause(gamChar,1)>0
   Speak(2,0,1)
   If charged=0 Then AdjustAttitude(gamChar,-2) : charged=1
   lineA$="You DON'T want to be the Tag Champions?!"
   lineB$="Then what the hell did you form a team for?!"
  EndIf
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1 And charClause(gamChar,1)=0
   Speak(2,0,1)
   If charged=0 Then execute=1 : AdjustAttitude(gamChar,-2) : charged=1
   lineA$="Sorry, did I make it sound like you had a choice?
   lineB$="The title scene needs you, so get used to the idea!"
  EndIf
  If negStage=2 And negTim>375 Then go=1 
  ;execute
  If execute=1
   gamPromo(gamDate)=59
   ChangeRelationship(gamChar,fedChampTag(fed,1),-1) 
   ChangeRelationship(gamChar,fedChampTag(fed,2),-1)
   ChangeRelationship(charPartner(gamChar),fedChampTag(fed,1),-1) 
   ChangeRelationship(charPartner(gamChar),fedChampTag(fed,2),-1)
  EndIf
 EndIf
 ;60. FORCED TO TEAM WITH ENEMY
 If negTopic=60
  execute=0 : g=charGender(negVariable)
  optionA$="Yes, team with enemy..." : optionB$="No chance!"
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,1)
   lineA$="Look, "+charName$(gamChar)+", I'm getting sick of"
   lineB$="your petty feud with "+charName$(negVariable)+"!"
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,2)
   lineA$="Since the two of you can't be trusted to face each"
   lineB$="other, I've come up with the perfect solution..."
  EndIf 
  If negStage=0 And negTim>675 And negTim<975
   Speak(2,0,1)
   lineA$="Tonight the two of you will work as a TEAM!"
   lineB$="That should force you to settle your differences..."
  EndIf
  If negStage=0 And negTim>975 Then camFoc=1
  If negStage=0 And negTim>1000 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,3)
   If charged=0 Then execute=1 : AdjustAttitude(gamChar,1) : charged=1
   lineA$="Good, I'm glad you're receptive to a solution."
   lineB$="Who knows? You might even enjoy working together!"
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1 And charClause(gamChar,1)>0
   Speak(2,0,1)
   If charged=0 Then gamSchedule(gamDate)=0 : AdjustAttitude(gamChar,-2) : charged=1
   lineA$="Fine, if you won't accept my solution you can"
   lineB$="take your ass home and dream up something better!"
  EndIf
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1 And charClause(gamChar,1)=0
   Speak(2,0,1)
   If charged=0 Then execute=1 : AdjustAttitude(gamChar,-2) : charged=1
   lineA$="Sorry, did I make it sound like you had a choice?"
   lineB$="You're teaming with "+Him$(g)+", so get used to the idea!"
  EndIf
  If negStage=2 And negTim>375 Then go=1 
  ;execute
  If execute=1
   gamPromo(gamDate)=31 : gamPromoVariable(gamDate)=negVariable
   If gamOpponent(gamDate)=negVariable Then gamOpponent(gamDate)=0
   gamMatch(gamDate)=12 : gamGimmick(gamDate)=0
  EndIf
 EndIf
 ;61. PRODUCT ENDORSEMENT
 If negTopic=61
  optionA$="Yes, accept $"+GetFigure$(negPayOff)+"!" : optionB$="No thanks..."
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,2)
   lineA$="Listen, "+charName$(gamChar)+", I represent the"
   lineB$="company that manufactures this '"+weapName$(negVariable)+"'..."
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,3)
   lineA$="It's going to take the world by storm, and we"
   lineB$="want YOU to be the one that paves the way!"
  EndIf 
  If negStage=0 And negTim>675 And negTim<975
   Speak(2,0,3)
   lineA$="Would you be willing to carry it into your matches and"
   lineB$="spread the word if we paid you $"+GetFigure$(negPayOff)+" for your trouble?"
  EndIf
  If negStage=0 And negTim>975 Then camFoc=1
  If negStage=0 And negTim>1000 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,3)
   If charged=0 
    PlaySound sCash : charBank(gamChar)=charBank(gamChar)+negPayOff
    charWeapon(gamChar)=negVariable : gamPromo(gamDate)=29 : gamAgreement(17)=4
    AdjustAttitude(gamChar,-1) : charged=1
   EndIf
   lineA$="Excellent! This '"+weapName$(negVariable)+"' is yours for now."
   lineB$="You'll need it when we launch the campaign..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(2,0,1)
   If charged=0 Then AdjustAttitude(gamChar,1) : charged=1
   lineA$="That could've been the easiest money you ever made!"
   lineB$="You'll be sorry when everybody has one of these..."
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;62. ASKED TO PARTICIPATE IN ARTICLE
 If negTopic=62
  optionA$="Yes, accept $"+GetFigure$(negPayOff)+"!" : optionB$="No thanks..."
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,3)
   lineA$="Listen, "+charName$(gamChar)+", I represent a show biz"
   lineB$="magazine that's interested in your story!"
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,3)
   lineA$="Would you be willing to spend the day with us"
   lineB$="if we paid you $"+GetFigure$(negPayOff)+" for an interview?"
  EndIf 
  If negStage=0 And negTim>650 Then camFoc=1
  If negStage=0 And negTim>675 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,3)
   If charged=0 
    PlaySound sCash : charBank(gamChar)=charBank(gamChar)+negPayOff
    gamSchedule(gamDate)=0 : charPopularity(gamChar)=charPopularity(gamChar)+1
    AdjustAttitude(gamChar,-1) : charged=1
   EndIf
   lineA$="Wonderful! Sorry to pull you out of the ring,"
   lineB$="but I'm sure your fans will enjoy the article..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(2,0,1)
   If charged=0 Then AdjustAttitude(gamChar,1) : charged=1
   lineA$="Fine, I doubt your story is worth printing anyway!"
   lineB$="Who wants to know what a moronic wrestler thinks?"
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;63. ASKED TO APPEAR IN GAME
 If negTopic=63
  optionA$="Yes, accept $"+GetFigure$(negPayOff)+"!" : optionB$="No thanks..."
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,3)
   lineA$="Listen, "+charName$(gamChar)+", I represent a company that's"
   lineB$="making a videogame about the wrestling industry!"
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,3)
   lineA$="We're looking for professional wrestlers to lend"
   lineB$="their name to the project and oversee its production..."
  EndIf 
  If negStage=0 And negTim>675 And negTim<975
   Speak(2,0,3)
   lineA$="Would you be willing to spend a couple of weeks"
   lineB$="working alongside us if we paid you $"+GetFigure$(negPayOff)+"?"
  EndIf 
  If negStage=0 And negTim>975 Then camFoc=1
  If negStage=0 And negTim>1000 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,3)
   If charged=0 
    PlaySound sCash : charBank(gamChar)=charBank(gamChar)+negPayOff
    gamSchedule(gamDate)=0 : gamSchedule(gamDate+1)=0 
    charPopularity(gamChar)=charPopularity(gamChar)+1
    AdjustAttitude(gamChar,-1) : charged=1
   EndIf
   lineA$="Cool! Sorry to pull you out of the ring,"
   lineB$="but I'm sure your fans will love this game..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(2,0,1)
   If charged=0 Then AdjustAttitude(gamChar,1) : charged=1
   lineA$="Fine, the game doesn't need you anyway!"
   lineB$="We've got plenty of fictitious characters..."
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;64. ASKED TO APPEAR IN MOVIE
 If negTopic=64
  optionA$="Yes, accept $"+GetFigure$(negPayOff)+"!" : optionB$="No thanks..."
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,3)
   lineA$="Listen, "+charName$(gamChar)+", I'm shooting a movie"
   lineB$="soon - and I've got the perfect part for YOU!"
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,3)
   lineA$="It's a great opportunity to raise your profile,"
   lineB$="and you could earn as much as $"+GetFigure$(negPayOff)+"!"
  EndIf 
  If negStage=0 And negTim>675 And negTim<975
   Speak(2,0,2)
   lineA$="Of course, we'd have to pull you out of the ring"
   lineB$="for a month of filming - but that's fine, right?"
  EndIf 
  If negStage=0 And negTim>975 Then camFoc=1
  If negStage=0 And negTim>1000 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,3)
   If charged=0 
    PlaySound sCash : charBank(gamChar)=charBank(gamChar)+negPayOff
    gamSchedule(gamDate)=0 : gamSchedule(gamDate+1)=0  
    gamSchedule(gamDate+2)=0 : gamSchedule(gamDate+3)=0 
    charPopularity(gamChar)=charPopularity(gamChar)+PursueValue(charPopularity(gamChar),100,0)
    charHappiness(gamChar)=charHappiness(gamChar)+PursueValue(charHappiness(gamChar),100,0)
    charAttitude(gamChar)=charAttitude(gamChar)+PursueValue(charAttitude(gamChar),30,0)
    charged=1
   EndIf
   lineA$="Great! Forget about your wrestling commitments!"
   lineB$="You'll soon be bigger than this tawdry sport..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(2,0,1)
   If charged=0 Then AdjustAttitude(gamChar,1) : charged=1
   lineA$="Actually, I'm not sure you suited the role anyway."
   lineB$="We need somebody with ambitions beyond the ring..."
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;65. WORK THROUGH POWER FAILURE
 If negTopic=65
  optionA$="Yes, work through it!" : optionB$="No, leave me out..."
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,1)
   lineA$="Look, "+charName$(gamChar)+", it seems the venue"
   lineB$="for tonight has suffered a power failure!"
   If negTim>150 Then ChannelPitch chTheme,PercentOf#(chThemePitch,90)
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,2)
   lineA$="That obviously leaves the arena with hardly"
   lineB$="any lighting, so things could get dangerous!"
  EndIf 
  If negStage=0 And negTim>675 And negTim<975
   Speak(2,0,3)
   lineA$="It should still be possible for the show to go"
   lineB$="on if you feel you can work through it though?"
  EndIf
  If negStage=0 And negTim>975 Then camFoc=1
  If negStage=0 And negTim>1000 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,3)
   If charged=0 Then AdjustAttitude(gamChar,1) : charged=1
   lineA$="Good, I'm glad you're up to the challenge!"
   lineB$="Who knows? It could make things interesting..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1 And charClause(gamChar,1)>0
   Speak(2,0,1)
   If charged=0 Then gamSchedule(gamDate)=0 : AdjustAttitude(gamChar,-1) : charged=1
   lineA$="Alright, just leave me here with a dark arena"
   lineB$="and an empty locker room! I'll figure it out..."
  EndIf
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1 And charClause(gamChar,1)=0
   Speak(2,0,1)
   If charged=0 Then AdjustAttitude(gamChar,-2) : charged=1
   lineA$="Sorry, did I make it sound like you had a choice?"
   lineB$="The show must go on, so drag your ass out there!"
  EndIf
  If negStage=2 And negTim>375 Then go=1 
  gamAgreement(18)=1
 EndIf
 ;66. WORK THROUGH LACK OF ITEMS
 If negTopic=66
  optionA$="Yes, work through it!" : optionB$="No, leave me out..."
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,1)
   lineA$="Listen, "+charName$(gamChar)+", it seems the ring crew"
   lineB$="have neglected to bring our items to the arena!"
   If negTim>150 Then ChannelPitch chTheme,PercentOf#(chThemePitch,90)
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,2)
   lineA$="All the weapons and furniture we normally use"
   lineB$="are sitting in a truck back at headquarters!"
  EndIf 
  If negStage=0 And negTim>675 And negTim<975
   Speak(2,0,3)
   lineA$="That obviously means the matches will be a little"
   lineB$="tame, but do you think you can make it work?"
  EndIf
  If negStage=0 And negTim>975 Then camFoc=1
  If negStage=0 And negTim>1000 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,3)
   If charged=0 Then AdjustAttitude(gamChar,1) : charged=1
   lineA$="Good, I'm glad you're up to the challenge!"
   lineB$="Maybe it'll bring out the best in you..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1 And charClause(gamChar,1)>0
   Speak(2,0,1)
   If charged=0 Then gamSchedule(gamDate)=0 : AdjustAttitude(gamChar,-1) : charged=1
   lineA$="Fine, if you can't wrestle clean then you won't"
   lineB$="work at all! Go home and break your own furniture..."
  EndIf
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1 And charClause(gamChar,1)=0
   Speak(2,0,1)
   If charged=0 Then AdjustAttitude(gamChar,-2) : charged=1
   lineA$="Sorry, did I make it sound like you had a choice?"
   lineB$="The show must go on, so get used to the idea!"
  EndIf
  If negStage=2 And negTim>375 Then go=1 
  gamAgreement(19)=1
 EndIf
 ;67. WORK WITHOUT CROWD
 If negTopic=67
  optionA$="Yes, work through it!" : optionB$="No, leave me out..."
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,1)
   lineA$="Listen, "+charName$(gamChar)+", I'm sorry to say that"
   lineB$="NOBODY has turned up for tonight's show!"
   If negTim>150 Then ChannelPitch chTheme,PercentOf#(chThemePitch,90)
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,2)
   lineA$="A scheduling mix-up meant they were told the"
   lineB$="wrong date, so all of our fans are at home!"
  EndIf 
  If negStage=0 And negTim>675 And negTim<975
   Speak(2,0,3)
   lineA$="The show will still be broadcast on TV, but"
   lineB$="do you think you can work without a crowd?"
  EndIf
  If negStage=0 And negTim>975 Then camFoc=1
  If negStage=0 And negTim>1000 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,3)
   If charged=0 Then AdjustAttitude(gamChar,1) : charged=1
   lineA$="Good, I'm glad this hasn't put you off!"
   lineB$="Who knows? You might enjoy a little peace..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1 And charClause(gamChar,1)>0
   Speak(2,0,1)
   If charged=0 Then gamSchedule(gamDate)=0 : AdjustAttitude(gamChar,-1) : charged=1
   lineA$="So you care more about adulation than entertainment?"
   lineB$="Fine, go home and stare at yourself in the mirror!"
  EndIf
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1 And charClause(gamChar,1)=0
   Speak(2,0,1)
   If charged=0 Then AdjustAttitude(gamChar,-2) : charged=1
   lineA$="Sorry, did I make it sound like you had a choice?"
   lineB$="The show must go on, so drag your ass out there!"
  EndIf
  If negStage=2 And negTim>375 Then go=1 
  gamAgreement(20)=1
 EndIf
 ;68. OLD PARTNER SUGGESTS REUNION
 If negTopic=68
  optionA$="Yes, reform team!" : optionB$="No, get over it..."
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,3)
   lineA$="Hey, "+charName$(gamChar)+", don't you miss our team?"
   lineB$="It's just not the same without you by my side!"
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,2)
   lineA$="I know things didn't pan out as we'd like the"
   lineB$="last time, but we're different people now..."
  EndIf
  If negStage=0 And negTim>675 And negTim<975
   Speak(2,0,3)
   lineA$="Why don't we put the pieces back together and"
   lineB$="see if we've got what it takes to make it work?"
  EndIf
  If negStage=0 And negTim>975 Then camFoc=1
  If negStage=0 And negTim>1000 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,3)
   If charged=0 
    FormTeam(gamChar,negChar)
    If charHappiness(negChar)<75 Then charHappiness(negChar)=75
    If gamPromo(gamDate)=0 And fed=<6 Then gamMatch(gamDate)=12 : gamGimmick(gamDate)=0 : gamPromo(gamDate)=62
    AdjustAttitude(gamChar,2) : charged=1
   EndIf
   lineA$="I'm glad you agree! After all this time, we're still"
   lineB$="on the same wavelength! That's gotta be a good sign..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(2,0,1)
   If charged=0 Then ChangeRelationship(gamChar,negChar,-1) : AdjustAttitude(gamChar,-2) : charged=1
   lineA$="Oh, you think you're too good to team with me now?"
   lineB$="You were nothing without me and you never will be!"
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;69. OLD FRIEND SUGGESTS REUNION
 If negTopic=69
  optionA$="Yes, make friends!" : optionB$="No, get over it..."
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,2)
   lineA$="Hey, "+charName$(gamChar)+", it's a shame we haven't"
   lineB$="been seeing eye to eye in recent weeks..."
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,3)
   lineA$="We used to be such good friends! I can't even"
   lineB$="remember what caused us to throw that away..."
  EndIf
  If negStage=0 And negTim>675 And negTim<975
   Speak(2,0,3)
   lineA$="We need all the friends we can get in this business,"
   lineB$="so why don't we get back to how things were before?"
  EndIf
  If negStage=0 And negTim>975 Then camFoc=1
  If negStage=0 And negTim>1000 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,3)
   If charged=0 Then ChangeRelationship(gamChar,negChar,1) : AdjustAttitude(gamChar,2) : charged=1
   lineA$="I'm glad you agree! After all this time, we're still"
   lineB$="on the same wavelength! I can't wait to catch up..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(2,0,1)
   If charged=0 Then ChangeRelationship(gamChar,negChar,-1) : AdjustAttitude(gamChar,-2) : charged=1
   lineA$="Now I remember why I fell out with you in the first"
   lineB$="place! I see you've still got a chip on your shoulder..."
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;70. OLD MANAGER WANTS TO RETURN
 If negTopic=70
  optionA$="Yes, reinstate manager!" : optionB$="No, get over it..."
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,3)
   lineA$="Hey, "+charName$(gamChar)+", don't you miss having"
   lineB$="me at ringside? We made a pretty good team!"
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,3)
   lineA$="None of the other wrestlers I've managed have"
   lineB$="matched up to you! I miss working with a pro..."
  EndIf
  If negStage=0 And negTim>675 And negTim<975
   Speak(2,0,3)
   lineA$="We didn't get a chance to see things through,"
   lineB$="so why don't we roll the dice one more time?"
  EndIf
  If negStage=0 And negTim>975 Then camFoc=1
  If negStage=0 And negTim>1000 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,3)
   If charged=0 
    PlaySound sCash : charManager(gamChar)=negChar
    ChangeRelationship(gamChar,negChar,1)
    If charHappiness(negChar)<75 Then charHappiness(negChar)=75
    If gamPromo(gamDate)=0 And fed=<6 Then gamPromo(gamDate)=40  
    AdjustAttitude(gamChar,2) : charged=1
   EndIf
   lineA$="Thanks for taking me back! Now let's make up for"
   lineB$="all those earnings I've been missing out on..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(2,0,1)
   If charged=0 Then ChangeRelationship(gamChar,negChar,-1) : AdjustAttitude(gamChar,-2) : charged=1
   lineA$="That's the thanks I get for coming back to you?!"
   lineB$="I can't believe I ever worked for an asshole you..."
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;71. MAKE TEMPORARY TEAM PERMANENT
 If negTopic=71
  optionA$="Yes, form team!" : optionB$="No, remain single..."
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,3)
   lineA$="Hey, "+charName$(gamChar)+", it was a pleasure to team"
   lineB$="with you! We worked well together out there..."
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,3)
   lineA$="What would you say to making it a permanent thing?"
   lineB$="We've already got the chemistry of a great team!"
  EndIf
  If negStage=0 And negTim>650 Then camFoc=1
  If negStage=0 And negTim>675 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,3)
   If charged=0 
    FormTeam(gamChar,negChar)
    If charHappiness(negChar)<75 Then charHappiness(negChar)=75
    If gamPromo(gamDate+1)=0 And fed=<6 Then gamMatch(gamDate+1)=12 : gamGimmick(gamDate+1)=0 : gamPromo(gamDate+1)=3
    AdjustAttitude(gamChar,2) : charged=1
   EndIf
   lineA$="I'm glad you feel the same way! Tonight was just the"
   lineB$="beginning! There's no limit to what we can achieve..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(2,0,1)
   If charged=0 Then AdjustAttitude(gamChar,-2) : charged=1
   lineA$="How can you throw away an opportunity like this?!"
   lineB$="Most teams would kill for the connection we have..."
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;72. LOSING TEAM MATE SUGGEST COMPENSATION
 If negTopic=72
  optionA$="Yes, pay $"+GetFigure$(negPayOff)+"..." : optionB$="No, forget it!"
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,1)
   lineA$="Hey,  "+charName$(gamChar)+", teaming with a loser like"
   lineB$="you is COSTING me more than it's making me!"
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,1)
   lineA$="Thanks to your antics out there tonight, I've lost"
   lineB$="my winnings AND had my reputation tainted to boot!"
  EndIf
  If negStage=0 And negTim>675 And negTim<975
   Speak(2,0,1)
   lineA$="You better cough up $"+GetFigure$(negPayOff)+" to make this pathetic"
   lineB$="team worth my while or I'm through with you!"
  EndIf
  If negStage=0 And negTim>975 Then camFoc=1
  If negStage=0 And negTim>1000 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,3)
   If charged=0 
    PlaySound sCash : charBank(gamChar)=charBank(gamChar)-negPayOff
    If charHappiness(negChar)<75 Then charHappiness(negChar)=75
    AdjustAttitude(gamChar,1) : charged=1
   EndIf
   lineA$="Thanks, this should make up for it! Sorry I had"
   lineB$="to get tough, but my career is important to me..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(2,0,1)
   If charged=0 
    date=NextDate()
    gamOpponent(date)=negChar : gamPromo(date)=55
    ChangeRelationship(gamChar,negChar,-1) 
    AdjustAttitude(gamChar,-1) : charged=1
   EndIf
   lineA$="You think I'm bluffing? I've been waiting for an"
   lineB$="excuse to get rid of you ever since we teamed up!"
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;73. DOCTOR OFFERS PAIN RELIEF
 If negTopic=73
  optionA$="Yes, use painkillers!" : optionB$="No thanks..."
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,3) 
   lineA$="Hello, "+charName$(gamChar)+", I invited you to my"
   lineB$="surgery today to talk about your health..."
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,2)
   lineA$="I treat a lot of wrestlers, and I can tell when"
   lineB$="they've been working too hard! You look exhausted..."
  EndIf
  If negStage=0 And negTim>675 And negTim<975
   Speak(2,0,3)
   lineA$="It doesn't have to be that way though. I can"
   lineB$="prescribe you some painkillers for $"+GetFigure$(negPayOff)+"?"
  EndIf
  If negStage=0 And negTim>975 Then camFoc=1
  If negStage=0 And negTim>1000 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,2)
   If charged=0
    PlaySound sCash : charBank(gamChar)=charBank(gamChar)-negPayOff
    charHealth(gamChar)=100 : gamAgreement(14)=2
    randy=Rnd(0,2)
    If randy=0 And fed=<6 And gamPromo(gamDate)=0 Then gamPromo(gamDate)=97 
    AdjustAttitude(gamChar,-1) : charged=1
   EndIf
   lineA$="A wise choice! Once these drugs get into your"
   lineB$="system you'll be back to feeling your best..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(2,0,1)
   If charged=0 Then AdjustAttitude(gamChar,1) : charged=1
   lineA$="Hey, it's your choice - but how do you intend to"
   lineB$="compete with wrestlers that ARE using this stuff?!"
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;74. DOCTOR OFFERS STEROIDS
 If negTopic=74
  optionA$="Yes, use steroids!" : optionB$="No thanks..."
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,3)
   lineA$="Hello, "+charName$(gamChar)+", I invited you to my"
   lineB$="surgery today to talk about your physique..."
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,3)
   lineA$="Wrestling is an extreme sport that requires extreme"
   lineB$="fitness! Have you ever considered taking steroids?"
  EndIf
  If negStage=0 And negTim>675 And negTim<975
   Speak(2,0,3)
   lineA$="I prescribe them to all the world's top athletes."
   lineB$="For just $"+GetFigure$(negPayOff)+", you could taste the benefits too?"
  EndIf
  If negStage=0 And negTim>975 Then camFoc=1
  If negStage=0 And negTim>1000 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,3)
   If charged=0
    PlaySound sCash : charBank(gamChar)=charBank(gamChar)-negPayOff
    charStrength(gamChar)=charStrength(gamChar)+1
    charSkill(gamChar)=charSkill(gamChar)+1
    charAgility(gamChar)=charAgility(gamChar)+1
    charStamina(gamChar)=charStamina(gamChar)+1
    charToughness(gamChar)=charToughness(gamChar)+1
    charWeightChange(gamChar)=charWeightChange(gamChar)+Rnd(1,5)
    gamAgreement(13)=2
    randy=Rnd(0,2)
    If randy=0 And fed=<6 And gamPromo(gamDate)=0 Then gamPromo(gamDate)=97 
    AdjustAttitude(gamChar,-1) : charged=1
   EndIf
   lineA$="Good for you! You should feel the results"
   lineB$="in no time... and so will your opponents!"
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(2,0,1)
   If charged=0 Then AdjustAttitude(gamChar,1) : charged=1
   lineA$="Hey, it's your choice - but how do you intend to"
   lineB$="compete with wrestlers that ARE using this stuff?!"
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;75. DOCTOR OFFERS SURGERY
 If negTopic=75
  optionA$="Yes, have surgery!" : optionB$="No thanks..."
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,3)
   lineA$="Hello, "+charName$(gamChar)+", I invited you to my"
   lineB$="surgery today to talk about your injuries..."
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,3)
   lineA$="Time isn't always the best healer. By undergoing"
   lineB$="surgery you could cut your prognosis in HALF!"
  EndIf
  If negStage=0 And negTim>675 And negTim<975
   Speak(2,0,3)
   lineA$="How would you like to go under the knife? For"
   lineB$="$"+GetFigure$(negPayOff)+", you could be back in the ring in no time!"
  EndIf
  If negStage=0 And negTim>975 Then camFoc=1
  If negStage=0 And negTim>1000 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,3)
   If charged=0
    PlaySound sCash : charBank(gamChar)=charBank(gamChar)-negPayOff
    For limb=0 To 5
     charInjured(gamChar,limb)=charInjured(gamChar,limb)/2
    Next
    AdjustAttitude(gamChar,-1) : charged=1
   EndIf
   lineA$="A wise decision! There's nothing valiant"
   lineB$="about spending your career on the bench..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(2,0,1)
   If charged=0 Then AdjustAttitude(gamChar,1) : charged=1
   lineA$="An athlete can't put a price on their health!"
   lineB$="You morons need all the help you can get..."
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;76. DOCTOR OFFERS CORRECTIVE SURGERY
 If negTopic=76
  optionA$="Yes, have surgery!" : optionB$="No thanks..."
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,3)
   lineA$="Hello, "+charName$(gamChar)+", I invited you to my"
   lineB$="surgery today to talk about your injuries..."
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,2)
   lineA$="I couldn't help but notice that you've lost one"
   lineB$="or two limbs during your battles in the ring?"
  EndIf
  If negStage=0 And negTim>675 And negTim<975
   Speak(2,0,3)
   lineA$="Well, they don't have to stay lost forever!"
   lineB$="For just $"+GetFigure$(negPayOff)+", I could repair the damage?"
  EndIf
  If negStage=0 And negTim>975 Then camFoc=1
  If negStage=0 And negTim>1000 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,3)
   If charged=0
    PlaySound sCash : charBank(gamChar)=charBank(gamChar)-negPayOff
    For limb=0 To 50
     charLimb(gamChar,limb)=1
    Next
    If InjuryStatus(gamChar)=0 Then charInjured(gamChar,0)=1 : charHealth(gamChar)=0
    AdjustAttitude(gamChar,-1) : charged=1
   EndIf
   lineA$="A wise decision! After a couple of weeks of"
   lineB$="healing, you can get back to being whole again..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(2,0,1)
   If charged=0 Then AdjustAttitude(gamChar,1) : charged=1
   lineA$="A disabled athlete can't put a price on their body!"
   lineB$="You freaks need all the treatment you can get..."
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;77. RACIST THREAT
 If negTopic=77
  optionA$="Yes, go home..." : optionB$="No, screw you!"
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,1)
   lineA$="Hey, "+charName$(gamChar)+", you're a long way from home!"
   lineB$="There's no place for "+textRace$(GetRace(gamChar))+" people on this show..."
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,1)
   lineA$="I don't like you and the fans don't like you either,"
   lineB$="so why don't you do us all a favour and go away?"
  EndIf
  If negStage=0 And negTim>650 Then camFoc=1
  If negStage=0 And negTim>675 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,2)
   If charged=0 
    gamSchedule(gamDate)=0
    ChangeRelationship(gamChar,negChar,-1)
    AdjustAttitude(gamChar,-2) : charged=1
   EndIf
   lineA$="Good, I'm glad you know your place! Now run along to"
   lineB$="your shack and leave the wrestling to us decent people..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(2,0,1)
   If charged=0 Then ChangeRelationship(gamChar,negChar,-1) : AdjustAttitude(gamChar,1) : charged=1
   lineA$="Why don't you animals ever know your place?!"
   lineB$="Now I'll have to beat you out like a cockroach..."
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;78. SABOTAGE OFFER
 If negTopic=78
  g=charGender(gamOpponent(gamDate))
  optionA$="Yes, sabotage opponent!" : optionB$="No thanks..."
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,2)
   lineA$="Hey, "+charName$(gamChar)+", I notice you're booked"
   lineB$="to face "+charName$(gamOpponent(gamDate))+" in a match tonight?"
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,3)
   lineA$="Well, how would you like me to make it easy for you?"
   lineB$="For just $"+GetFigure$(negPayOff)+", I'll make sure "+Lower$(He$(g))+" has an 'accident'!"
  EndIf
  If negStage=0 And negTim>650 Then camFoc=1
  If negStage=0 And negTim>675 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,3)
   If charged=0 
    PlaySound sCash : charBank(gamChar)=charBank(gamChar)-negPayOff
    charHealth(gamOpponent(gamDate))=0 : charInjured(gamOpponent(gamDate),4)=1
    ChangeRelationship(gamChar,gamOpponent(gamDate),-1)
    ChangeRelationship(negChar,gamOpponent(gamDate),-1)
    ChangeRelationship(gamChar,negChar,1)
    AdjustAttitude(gamChar,-1) : charged=1
   EndIf
   lineA$="Congratulations on your victory! Once I'm done with"
   lineB$=Him$(g)+", "+Lower$(He$(g))+" won't be able to walk - let alone wrestle..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(2,0,1)
   If charged=0
    ChangeRelationship(negChar,gamOpponent(gamDate),1) 
    ChangeRelationship(gamChar,negChar,-1)
    AdjustAttitude(gamChar,1) : charged=1
   EndIf
   lineA$="Fine, maybe "+charName$(gamOpponent(gamDate))+" will appreciate my offer!"
   lineB$="Once I'm done with you you'll be crawling to the ring..."
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;79. FRIEND ASKS FOR MONEY
 If negTopic=79
  optionA$="Yes, give $"+GetFigure$(negPayOff)+"!" : optionB$="No, sorry..."
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,3)
   lineA$="Hey, "+charName$(gamChar)+", we're good friends right?"
   lineB$="Then you've gotta help me out with something..."
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,2)
   lineA$="I'm hurting for money right now and I can't cope!"
   lineB$="Could you lend me $"+GetFigure$(negPayOff)+"? You know I'm good for it..."
  EndIf
  If negStage=0 And negTim>650 Then camFoc=1
  If negStage=0 And negTim>675 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,3)
   If charged=0 
    PlaySound sCash : charBank(gamChar)=charBank(gamChar)-negPayOff
    ChangeRelationship(gamChar,negChar,1)
    AdjustAttitude(gamChar,1) : charged=1
   EndIf
   lineA$="Thanks, you just saved my life! You're a great"
   lineB$="friend. It's good to know I can count on you..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(2,0,1)
   If charged=0 Then ChangeRelationship(gamChar,negChar,-1) : AdjustAttitude(gamChar,-1) : charged=1
   lineA$="After all I've done for you, how can you forsake me?!"
   lineB$="I guess you're not the friend I thought you were..."
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;80. WRESTLER OFFERS TO SELL MOVES
 If negTopic=80
  optionA$="Yes, buy moves!" : optionB$="No thanks..."
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,2)
   lineA$="Hey, "+charName$(gamChar)+", by now you must know that"
   lineB$="knowledge is power in a business like this?"
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,3)
   lineA$="So why don't you let me help you out by teaching"
   lineB$="you my moves? For $"+GetFigure$(negPayOff)+", everything I know is yours!"
  EndIf
  If negStage=0 And negTim>650 Then camFoc=1
  If negStage=0 And negTim>675 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,3)
   If charged=0 
    PlaySound sCash : charBank(gamChar)=charBank(gamChar)-negPayOff
    LearnMoves(negChar,1)
    ChangeRelationship(gamChar,negChar,1)
    AdjustAttitude(gamChar,2) : charged=1
   EndIf
   lineA$="I'm glad we could come to an arrangement! What"
   lineB$="good is learning anything if you can't pass it on?"
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(2,0,1)
   If charged=0 Then ChangeRelationship(gamChar,negChar,-1) : AdjustAttitude(gamChar,-2) : charged=1
   lineA$="You don't think my moves are worth buying? You"
   lineB$="won't be saying that when I beat you with them!"
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;81. WRESTLER OFFERS TO SELL COSTUMES
 If negTopic=81
  optionA$="Yes, buy clothes!" : optionB$="No thanks..."
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,2)
   lineA$="Hey, "+charName$(gamChar)+", don't you ever get jealous of"
   lineB$="all the fashions you see other wrestlers wearing?"
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,3)
   lineA$="Well, it can all be yours for a price! For just"
   lineB$="$"+GetFigure$(negPayOff)+" I'd be happy to sell you some of my outfits?"
  EndIf
  If negStage=0 And negTim>650 Then camFoc=1
  If negStage=0 And negTim>675 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,3)
   If charged=0 
    PlaySound sCash : charBank(gamChar)=charBank(gamChar)-negPayOff
    LearnCostumes(negChar,1)
    ChangeRelationship(gamChar,negChar,1)
    AdjustAttitude(gamChar,2) : charged=1
   EndIf
   lineA$="I'm glad we could come to an arrangement! What"
   lineB$="good is fashion sense if you can't help others?"
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(2,0,1)
   If charged=0 Then ChangeRelationship(gamChar,negChar,-1) : AdjustAttitude(gamChar,-2) : charged=1
   lineA$="You don't think my clothes are worth buying?"
   lineB$="Fine, carry on stinking up those rags of yours!"
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;82. BOOKER PROMISES CONTRACT OFFER
 If negTopic=82
  If gamOpponent(gamDate)=0 Then gamOpponent(gamDate)=AssignOpponent(gamChar,gamDate,0)
  optionA$="Yes, that would be great!" : optionB$="No, don't bother..."
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,3)
   lineA$="Hello, "+charName$(gamChar)+". We've been toying with the"
   lineB$="idea of bringing you to "+fedName$(charFed(negChar))+"!"
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,2)
   lineA$="We're not sold yet though. We'd like to see how you"
   lineB$="get on in your match against "+charName$(gamOpponent(gamDate))+" tonight..."
  EndIf
  If negStage=0 And negTim>675 And negTim<975
   Speak(2,0,3)
   lineA$="Chalk up an impressive victory and there'll be a"
   lineB$="lot to talk about! How does that sound to you?"
  EndIf
  If negStage=0 And negTim>975 Then camFoc=1
  If negStage=0 And negTim>1000 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,3)
   If charged=0 Then gamAgreement(21)=charFed(negChar) : AdjustAttitude(gamChar,2) : charged=1
   lineA$="Then it's a date! Good luck in your match tonight."
   lineB$="We'll call you back next week if we like what we see..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(2,0,1)
   If charged=0 Then ChangeRelationship(gamChar,negChar,-1) : AdjustAttitude(gamChar,-2) : charged=1
   lineA$="Do you realize how many wrestlers would kill for this"
   lineB$="opportunity?! Fine, you'll never hear from us again..."
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;83. PARTNER WANTS TO COPY YOUR COSTUME
 If negTopic=83
  optionA$="Yes, match costumes!" : optionB$="No, take that off..."
  If negStage=0 And negTim>25 And negTim<325
   Speak(2,0,2)
   lineA$="Hey, "+charName$(gamChar)+", we're supposed to be a"
   lineB$="team - but you wouldn't know it to look at us!"
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(2,0,3)
   lineA$="Why don't I change my costume to match yours?"
   lineB$="Perhaps then we'll look as good as we perform!"
  EndIf
  If negStage=0 And negTim>650 Then camFoc=1
  If negStage=0 And negTim>675 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(2,0,3)
   If charged=0 Then AdjustAttitude(gamChar,2) : gamAgreement(3)=4 : charged=1
   lineA$="Great, thanks for letting me copy your style!"
   lineB$="Now we'll really be presenting a united front..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(2,0,1)
   If charged=0 
    CopyChar(0,negChar)
    charAttitude(negChar)=charAttitude(negChar)-1
    charHappiness(negChar)=charHappiness(negChar)-1
    AdjustAttitude(gamChar,-2) : charged=1
   EndIf
   lineA$="Hey, I was just trying to make you feel better!"
   lineB$="Do you think I really want to dress like YOU?!"
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf

 ;-------------------------- MISSION ASSIGNMENTS ---------------------------------
 ;STANDARD INTRO
 If negTopic>100 And negTopic<120 
  If negTim>25 And negTim<325
   Speak(2,0,2)
   lineA$="Listen, "+charName$(gamChar)+", I've been keeping a close eye"
   lineB$="on your progress and I think changes need to be made..."
  EndIf
 EndIf
 ;1. IMPROVE STRENGTH
 If negTopic=101
  If negTim>350 And negTim<650 
   Speak(2,0,1)
   gamTarget=charStrength(gamChar)+5 : gamDeadline=gamDate+Rnd(4,6)
   lineA$="You're not strong enough to cut it as a wrestler!"
   lineB$="The fans pay to see somebody crush their opponents..."
  EndIf
  If negTim>675 And negTim<975
   Speak(2,0,1)
   lineA$="Raise your strength to "+gamTarget+"% by the "+DescribeDate$(gamDeadline,0)
   lineB$="or the only work for you will be behind a desk!"
  EndIf
  If negTim>1025 Then gamMission=1 : go=1
 EndIf
 ;2. IMPROVE SKILL
 If negTopic=102
  If negTim>350 And negTim<650 
   Speak(2,0,1)
   gamTarget=charSkill(gamChar)+5 : gamDeadline=gamDate+Rnd(4,6)
   lineA$="You're not talented enough to give the fans"
   lineB$="the technical wrestling that they pay to see!"
  EndIf
  If negTim>675 And negTim<975
   Speak(2,0,1)
   lineA$="Raise your skill to "+gamTarget+"% by the "+DescribeDate$(gamDeadline,0)
   lineB$="or you'll be applying new 'skills' to a new JOB!"
  EndIf
  If negTim>1025 Then gamMission=2 : go=1
 EndIf
 ;3. IMPROVE AGILITY
 If negTopic=103
  If negTim>350 And negTim<650 
   Speak(2,0,1)
   gamTarget=charAgility(gamChar)+5 : gamDeadline=gamDate+Rnd(4,6)
   lineA$="You don't move around the ring fast enough to give"
   lineB$="the fans a show! It's like watching paint dry..."
  EndIf
  If negTim>675 And negTim<975
   Speak(2,0,1)
   lineA$="Get your agility up to "+gamTarget+"% by the "+DescribeDate$(gamDeadline,0)
   lineB$="or you'll be jogging all the way to the job centre!"
  EndIf
  If negTim>1025 Then gamMission=3 : go=1
 EndIf
 ;4. IMPROVE STAMINA
 If negTopic=104
  If negTim>350 And negTim<650 
   Speak(2,0,1)
   gamTarget=charStamina(gamChar)+5 : gamDeadline=gamDate+Rnd(4,6)
   lineA$="You're not fit enough to give us a decent match!"
   lineB$="The fans pay to see somebody give it their all..."
  EndIf
  If negTim>675 And negTim<975
   Speak(2,0,1)
   lineA$="You've got until the "+DescribeDate$(gamDeadline,0)+" to increase"
   lineB$="your stamina to "+gamTarget+"% otherwise I'll throw in the towel!"
  EndIf
  If negTim>1025 Then gamMission=4 : go=1
 EndIf
 ;5. IMPROVE TOUGHNESS
 If negTopic=105
  If negTim>350 And negTim<650 
   Speak(2,0,1)
   gamTarget=charToughness(gamChar)+5 : gamDeadline=gamDate+Rnd(4,6)
   lineA$="You're not tough enough to cut it as a wrestler!"
   lineB$="The fans pay to see you take some punishment..."
  EndIf
  If negTim>675 And negTim<975
   Speak(2,0,1)
   lineA$="If you don't improve your toughness to "+gamTarget+"% by the"
   lineB$=DescribeDate$(gamDeadline,0)+", I'll show you how tough I can be!"
  EndIf
  If negTim>1025 Then gamMission=5 : go=1
 EndIf
 ;6. IMPROVE POPULARITY
 If negTopic=106
  If negTim>350 And negTim<650 
   Speak(2,0,1)
   gamTarget=charPopularity(gamChar)+5 : gamDeadline=gamDate+Rnd(4,6)
   If fed=<6 Then lineA$="You're not popular enough to represent this brand!"
   If fed=7 Then lineA$="You're not popular enough to cut it as a wrestler!"
   lineB$="The fans pay to see people that they care about..."
  EndIf
  If negTim>675 And negTim<975
   Speak(2,0,1)
   lineA$="Raise your popularity to "+gamTarget+"% by the "+DescribeDate$(gamDeadline,0)
   lineB$="or I'll send you back to the gutter you came from!"
  EndIf
  If negTim>1025 Then gamMission=6 : go=1
 EndIf 
 ;7. IMPROVE ATTITUDE
 If negTopic=107
  If negTim>350 And negTim<650 
   Speak(2,0,1)
   gamTarget=charAttitude(gamChar)+5 : gamDeadline=gamDate+Rnd(4,6)
   lineA$="You'll never make it in this business with that attitude"
   lineB$="of yours. You need to 'kiss' as much ass as you KICK!"
  EndIf
  If negTim>675 And negTim<975
   Speak(2,0,1)
   lineA$="Improve your reputation by the "+DescribeDate$(gamDeadline,0)
   lineB$="or I'll show you how bad MY attitude can be!"
  EndIf
  If negTim>1025 Then gamMission=7 : go=1
 EndIf  
 ;8. IMPROVE BANK BALANCE
 If negTopic=108
  If negTim>350 And negTim<650 
   Speak(2,0,1)
   addSum=charSalary(gamChar)*2
   If addSum<1000 Then addSum=1000
   gamTarget=charBank(gamChar)+addSum
   gamTarget=RoundOff(gamTarget,100)
   If gamTarget<1000 Then gamTarget=1000
   If gamTarget>100000 Then gamTarget=100000
   gamDeadline=gamDate+Rnd(4,6)
   lineA$="Your financial status brings shame on the company!"
   lineB$="Our wrestlers are supposed to be sporting superstars..."
  EndIf
  If negTim>675 And negTim<975
   Speak(2,0,1)
   lineA$="Amass a fortune of $"+GetFigure$(gamTarget)+" by the "+DescribeDate$(gamDeadline,0)
   lineB$="or I'll make sure you never earn another dollar here!"
  EndIf
  If negTim>1025 Then gamMission=8 : go=1
 EndIf 
 ;9. LOSE WEIGHT
 If negTopic=109
  If negTim>350 And negTim<650 
   Speak(2,0,1)
   gamTarget=TranslateWeight(gamChar)-10 : gamDeadline=gamDate+Rnd(4,6)
   lineA$="You're so fat I'm scared to point the camera at you!"
   If charGender(gamChar)=0 Then lineB$="Wrestlers are meant to be perfect physical specimens..."
   If charGender(gamChar)=0 Then lineB$="Women are supposed to keep themselves in good shape..."
  EndIf
  If negTim>675 And negTim<975
   Speak(2,0,1)
   lineA$="Get down to "+gamTarget+"lbs by the "+DescribeDate$(gamDeadline,0)
   lineB$="or you'll be waddling your way out of the door!"
  EndIf
  If negTim>1025 Then gamMission=9 : go=1
 EndIf 
 ;10. GAIN WEIGHT 
 If negTopic=110
  If negTim>350 And negTim<650 
   Speak(2,0,1)
   gamTarget=TranslateWeight(gamChar)+10 : gamDeadline=gamDate+Rnd(4,6)
   lineA$="You're so frail I'm scared to send you into the ring!"
   lineB$="Wrestlers are supposed to be formidable athletes..."
  EndIf
  If negTim>675 And negTim<975
   Speak(2,0,1)
   lineA$="Get your weight up to "+gamTarget+"lbs by the "+DescribeDate$(gamDeadline,0)
   lineB$="or the only job you'll have here is making the coffee!"
  EndIf
  If negTim>1025 Then gamMission=10 : go=1
 EndIf 
 ;11. WIN A TITLE
 If negTopic=111
  If negTim>350 And negTim<650 
   Speak(2,0,1)
   gamTarget=0 : gamDeadline=gamDate+Rnd(4,6)
   lineA$="I'm not convinced that you'll ever be a champion,"
   lineB$="which makes me wonder what you're doing here?!"
  EndIf
  If negTim>675 And negTim<975
   Speak(2,0,1)
   lineA$="Get your hands on a title by the "+DescribeDate$(gamDeadline,0)
   lineB$="or we'll have to assume that it's never gonna happen..."
  EndIf
  If negTim>1025 Then gamMission=11 : go=1
 EndIf 
 ;12. GET A DEAL
 If negTopic=112
  If negTim>350 And negTim<650 
   Speak(2,0,1)
   gamTarget=0 : gamDeadline=gamDate+Rnd(4,6)
   lineA$="You can't stay at wrestling school forever! I've"
   lineB$="got other prospects waiting for their opportunity..."
  EndIf
  If negTim>675 And negTim<975
   Speak(2,0,1)
   lineA$="Sign with a major promotion by the "+DescribeDate$(gamDeadline,0)
   lineB$="or we'll have to assume that it's never gonna happen..."
  EndIf
  If negTim>1025 Then gamMission=12 : go=1
 EndIf 
 ;13. GET OUT OF DEBT
 If negTopic=113
  If negTim>350 And negTim<650 And fed=<6
   Speak(2,0,1)
   gamTarget=0 : gamDeadline=gamDate+Rnd(4,6)
   lineA$="Your financial status brings shame on the company!"
   lineB$="Our wrestlers are supposed to be sporting superstars..."
  EndIf 
  If negTim>350 And negTim<650 And fed=7
   Speak(2,0,1)
   gamTarget=0 : gamDeadline=gamDate+Rnd(4,6)
   lineA$="Your last cheque bounced! You'll never make it in"
   lineB$="this business if you can't manage your finances..."
  EndIf
  If negTim>675 And negTim<975
   Speak(2,0,1)
   lineA$="You better get out of debt by the "+DescribeDate$(gamDeadline,0)
   lineB$="or I'll put you out on the street where you belong!"
  EndIf
  If negTim>1025 Then gamMission=13 : go=1
 EndIf
 ;14. IMPROVE WIN RATE
 If negTopic=114
  If negTim>350 And negTim<650 
   Speak(2,0,1)
   gamTarget=GetWinRate(gamChar,0)+5 : gamDeadline=gamDate+Rnd(4,6)
   lineA$="You're getting a reputation for being a loser!"
   lineB$="The fans only get behind wrestlers that can win..."
  EndIf
  If negTim>675 And negTim<975
   Speak(2,0,1)
   lineA$="Get your win rate up to "+gamTarget+"% by the "+DescribeDate$(gamDeadline,0)
   lineB$="or I'll give your spot to somebody that deserves it!"
  EndIf
  If negTim>1025 Then gamMission=14 : go=1
 EndIf 
   
 ;MISSION ACCOMPLISHED!
 If negTopic=120
  If negTim>25 And negTim<325
   Speak(2,0,3)
   lineA$="Congratulations, "+charName$(gamChar)+"! You followed my"
   lineB$="advice and made your prospects better than ever..."
  EndIf
  If negTim>350 And negTim<650 And fed=<6 
   Speak(2,0,3)
   If charged=0 
    PlaySound sCash : charged=1
    charBank(gamChar)=charBank(gamChar)+1000
   EndIf
   lineA$="As a reward for your struggle, here's a $1'000 bonus!"
   lineB$="I'll still be watching though, so don't get complacent..."
  EndIf
  If negTim>350 And negTim<650 And fed=7 
   Speak(2,0,3)
   lineA$="I wish I could offer you a reward, but at this"
   lineB$="level experience is the only reward there is!"
  EndIf
  If negTim>700 Then gamMission=0 : go=1
 EndIf
 ;FINANCIAL ACHIEVEMENT
 If negTopic=121
  If negTim>25 And negTim<325
   Speak(2,0,3)
   lineA$="Congratulations, "+charName$(gamChar)+"! With $"+GetFigure$(charBank(gamChar))
   lineB$="in the bank, you're an asset to this company..."
  EndIf
  If negTim>375 Then gamMission=0 : go=1
 EndIf
 ;MISSED FAILED (PROFESSIONAL)
 If negTopic=130
  If negTim>25 And negTim<325
   Speak(2,0,1)
   lineA$="Well, "+charName$(gamChar)+", your time is up and it"
   lineB$="appears that you've failed to follow my advice..."
  EndIf
  If negTim>350 And negTim<650 
   Speak(2,0,1) : ChannelPitch chTheme,PercentOf#(chThemePitch,90)
   lineA$="Slackers like you don't belong at THIS show!"
   lineB$="Clean out your locker and get out of my sight..."
  EndIf
  If negTim>675 Then camFoc=1 : pEyes(camFoc)=1
  If negTim>825 Then gamMission=0 : go=1
 EndIf
 ;MISSED FAILED (SCHOOL)
 If negTopic=131
  If negTim>25 And negTim<325
   Speak(2,0,1)
   lineA$="Well, "+charName$(gamChar)+", your time is up and it"
   lineB$="looks like you've failed to deliver the goods..."
  EndIf
  If negTim>350 And negTim<650 
   Speak(2,0,1) : ChannelPitch chTheme,PercentOf#(chThemePitch,90)
   lineA$="Unfortunately, this is the end of the road for you."
   lineB$="Maybe you'll put more effort into your next big idea..."
  EndIf
  If negTim>675 Then camFoc=1 : pEyes(camFoc)=1
  If negTim>825 Then gamMission=0 : go=1
 EndIf
 ;---------- DISPLAY SUBTITLES ----------
 DisplaySubtitles()
 ;---------- OPTION BOX ---------------
 If negStage=1 And negVerdict=0
  ;reflect mood
  mood=2
  If negTopic=55
   If foc=1 Then mood=1
   If foc=2 Then mood=3
  Else
   If foc=1 Then mood=3
   If foc=2 Then mood=1
  EndIf
  Speak(1,0,mood)
  ;display options
  DrawOption(1,rX#(400),rY#(520),optionA$,"")
  DrawOption(2,rX#(400),rY#(560),optionB$,"")
  ;stat reminder
  char=negChar
  If negChar=fedBooker(charFed(gamChar)) Or negChar=fedBooker(8) Or negChar=fedBooker(9) Then char=gamChar
  If negTopic=9 Or negTopic=55 Or negTopic=56 Or negTopic=60 Then char=negVariable
  If negTopic=15 Or negTopic=19 Or negTopic=37 Or negTopic=40 Or negTopic=41 Or negTopic=43 Or negTopic=79 Or negTopic=80 Or negTopic=81
   char=gamChar
  EndIf
  If negTopic=57 Then char=fedChampWorld(fed)
  If negTopic=58 Then char=fedChampInter(fed)
  If negTopic=59 Then char=fedChampTag(fed,1)
  If negTopic=78 Then char=gamOpponent(gamDate)
  If MouseDown(2)
   If char=gamChar Then char=negChar Else char=gamChar
  EndIf
  DrawProfile(char,-1,-1,0)
 EndIf
 ;return camera
 If negStage=2 And negTim>10 Then camFoc=2
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
;lose job
If negTopic=130
 MoveChar(gamChar,7)
 charHappiness(gamChar)=charHappiness(gamChar)+PursueValue(charHappiness(gamChar),30,0)
 If TournamentStatus(gamChar)>0 Then cupSize(cupSlot)=0
EndIf
;proceed
screen=20
If negTopic=0 Then cupSlot=3+slot : gamScroll=-((GetMonth(gamDate)-1)*125)
If (negTopic=1 And negVerdict=1) Or negTopic=131 Then screen=25
;detour to CPU match
If negTopic=16 Or negTopic=17 Or negTopic=18
 If negVerdict=1 Or (negTopic=>17 And negTopic=<18 And charClause(gamChar,1)=0) 
  ResetCharacters()
  GetMatchRules(2) : AddGimmick(0)
  If negTopic=17 
   no_refs=1 : matchRules=1
   randy=Rnd(0,2)
   If randy=0 Or promoLocked(28) Then matchPromo=28
  EndIf
  no_plays=no_wrestlers+no_refs
  If negTopic=16 Then pChar(1)=negChar : pChar(2)=AssignOpponent(negChar,gamDate,0) : pChar(3)=AssignReferee() : screenAgenda=14
  If negTopic=17 Then pChar(1)=negVariable : pChar(2)=AssignOpponent(negVariable,gamDate,0) : pChar(3)=gamChar : screenAgenda=13
  If negTopic=18 Then pChar(1)=gamChar : pChar(2)=AssignOpponent(gamChar,gamDate,0) : pChar(3)=AssignReferee() : screenAgenda=15
  screen=50
 EndIf
EndIf
;detour to 8-man inter-promotional war
If negTopic=25 And negVerdict=1 
 fed=0
 ResetCharacters()
 GetMatchRules(20) : AddGimmick(0)
 pChar(1)=gamChar
 pChar(5)=negChar 
 For cyc=1 To no_wrestlers
  If pChar(cyc)=0
   its=0
   Repeat
    satisfied=1 : its=its+1
    If cyc=<4 Then newbie=fedRoster(charFed(gamChar),Rnd(1,fedSize(charFed(gamChar))))
    If cyc=>5 Then newbie=fedRoster(charFed(negChar),Rnd(1,fedSize(charFed(negChar))))
    If its<100 And TitleHolder(newbie,0)=0 Then satisfied=0
    If FindCharacter(newbie)>0 Or InjuryStatus(newbie)>0 Then satisfied=0
   Until satisfied=1 Or its>1000
   pChar(cyc)=newbie
  EndIf
 Next
 If no_refs>0 Then pChar(no_wrestlers+1)=AssignReferee()
 screen=50 : screenAgenda=15
EndIf
;detour to brawl
If (negTopic=>19 And negTopic=<20 And negVerdict=-1) Or (negTopic=>22 And negTopic=<24 And negVerdict=1) Or negTopic=21 Or (negTopic=>77 And negTopic=<78 And negVerdict=-1)
 ResetCharacters()
 GetMatchRules(1) : AddGimmick(0)
 If negSetting=>10 And negSetting=<13 Then matchLocation=2 Else matchLocation=1
 If matchLocation=1 Then itemLayout=2 : weapLayout=2
 pChar(1)=gamChar
 pChar(2)=negChar 
 If negTopic=22 Then pChar(2)=negVariable
 If negTopic=23
  no_wrestlers=4 : no_plays=4 : matchTeams=1
  For count=3 To 4
   Repeat
    newbie=fedRoster(negVariable,Rnd(1,fedSize(negVariable)))
   Until FindCharacter(newbie)=0
   pChar(count)=newbie
  Next
  matchPromo=56
 EndIf
 If negTopic=24
  no_wrestlers=4 : no_plays=4
  For count=3 To 4
   Repeat
    newbie=AssignOpponent(gamChar,gamDate,1)
   Until FindCharacter(newbie)=0
   pChar(count)=newbie
  Next
 EndIf 
 screen=50 : screenAgenda=12
EndIf
End Function

;/////////////////////////////////////////////////////////////////
;---------------------- RELATED FUNCTIONS ------------------------
;/////////////////////////////////////////////////////////////////

;ADJUST ATTITUDE
Function AdjustAttitude(char,level)
 If level=2 Then charAttitude(char)=charAttitude(char)+1 : charHappiness(char)=charHappiness(char)+1 ;2. thoroughly positive
 If level=1 Then charAttitude(char)=charAttitude(char)+1 : charHappiness(char)=charHappiness(char)-1 ;1. unhappy consent
 If level=-1 Then charAttitude(char)=charAttitude(char)-1 : charHappiness(char)=charHappiness(char)+1 ;-1. happy rebellion
 If level=-2 Then charAttitude(char)=charAttitude(char)-1 : charHappiness(char)=charHappiness(char)-1 ;-2. thoroughly negative
 ;limits
 randy=Rnd(0,1)
 If randy=0 And charHappiness(char)>90 And charHappiness(char)>charOldHappiness(char) Then charHappiness(char)=charOldHappiness(char)
 If randy=1 And charAttitude(char)>90 And charAttitude(char)>charOldAttitude(char) Then charAttitude(char)=charOldAttitude(char)
 If charAttitude(char)>99 Then charAttitude(char)=99
 If charHappiness(char)>99 Then charHappiness(char)=99
End Function

;RANDOMIZE MEETING ANIMATIONS
Function RandomizeAnimation(cyc)
 randy=Rnd(0,5000)
 ;seated animations
 If randy=<1 And pAnim(cyc)=>1 And pAnim(cyc)=<9
  Repeat
   pAnim(cyc)=Rnd(1,9) : satisfied=1
   If pAnim(cyc)=<3 And negSetting=1 And cyc=1 Then satisfied=0
   If pAnim(cyc)=9 And negSetting=1 And cyc=2 Then satisfied=0
  Until satisfied=1
 EndIf
 ;standing animations 
 If pAnim(cyc)=>10 And pAnim(cyc)=<14
  If randy=<1 Then pAnim(cyc)=Rnd(10,14) 
  If randy=<10 And pSpeaking(cyc)>0 Then pAnim(cyc)=14
  If pAnim(cyc)=14 And pSpeaking(cyc)=0 Then pAnim(cyc)=Rnd(10,13) 
 EndIf
 ;activate
 If pAnim(cyc)<>pOldAnim(cyc)
  animSpeed#=Rnd#(0.1,0.3)
  Animate p(cyc),1,animSpeed#,pSeq(cyc,pAnim(cyc)),20
  pOldAnim(cyc)=pAnim(cyc)
 EndIf
End Function