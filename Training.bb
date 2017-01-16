;//////////////////////////////////////////////////////////////////////////////
;------------------------- WRESTLING MPIRE 2008: TRAINING ---------------------
;//////////////////////////////////////////////////////////////////////////////

;----------------------------------------------------------------------------
;//////////////////////// 54. TRAINING PROCESS //////////////////////////////
;----------------------------------------------------------------------------
Function Training()
;load setting
Loader("Please Wait","Preparing To Train")
;chThemeVol#=0.5
;ChannelVolume chTheme,chThemeVol#
ResetTextures()
world=LoadAnimMesh("World/Gym/Gym.3ds")
ExtractAnimSeq world,15,100,0 ;1
PrepareScenery()
For count=1 To 2
 EntityAlpha FindChild(world,"Net0"+count),0.5
Next
HideEntity FindChild(world,"Bag")
PositionEntity FindChild(world,"Bracket"),EntityX(FindChild(world,"Bracket"),1),44,EntityZ(FindChild(world,"Bracket"),1)
;camera
cam=CreateCamera()
CameraViewport cam,0,0,GraphicsWidth(),GraphicsHeight()
CameraZoom cam,1.5 
camX#=545 : camY#=50 : camZ#=50 
PositionEntity cam,camX#,camY#,camZ# 
camType=10 : camOption=1
dummy=CreatePivot()
camPivot=CreatePivot()
;atmosphere
AmbientLight 220,210,200
no_lights=1
light(1)=CreateLight(1) 
LightColor light(1),250,230,210
PositionEntity light(1),0,100,0
lightPivot=CreatePivot()
PositionEntity lightPivot,Rnd(-100,100),100,Rnd(-110,110) 
PointEntity light(1),lightPivot
;CHARACTERS
no_plays=1 : pChar(1)=gamChar
If charPartner(gamChar)>0 Then no_plays=2 : pChar(2)=charPartner(gamChar)
If charManager(gamChar)>0 Then no_plays=2 : pChar(2)=charManager(gamChar)
pX#(1)=505 : pY#(1)=6 : pZ#(1)=250 : pA#(1)=135 : pAnim(1)=0
pX#(2)=pX#(1)-40 : pY#(2)=6 : pZ#(2)=250 : pA#(2)=245 : pAnim(2)=0
For cyc=1 To no_plays
 ;load model
 pCostume(cyc)=2
 p(cyc)=LoadAnimMesh("Characters/Models/Model0"+GetModel(pChar(cyc))+".3ds")
 StripModel(cyc)
 ApplyCostume(cyc)
 If cyc=2 And TitleHolder(pChar(cyc),0)>0 Then WearBelt(cyc,TitleHolder(pChar(cyc),0)) 
 pEyes(cyc)=2 : pOldEyes(cyc)=-1
 ;load sequences
 pSeq(cyc,601)=LoadAnimSeq(p(cyc),"Characters/Sequences/Standard01.3ds")
 pSeq(cyc,604)=LoadAnimSeq(p(cyc),"Characters/Sequences/Standard04.3ds")
 pSeq(cyc,605)=LoadAnimSeq(p(cyc),"Characters/Sequences/Standard05.3ds")
 pSeq(cyc,606)=LoadAnimSeq(p(cyc),"Characters/Sequences/Standard06.3ds")
 If InjuryStatus(pChar(cyc))>0
  pSeq(cyc,1)=ExtractAnimSeq(p(cyc),70,110,pSeq(cyc,604)) ;injured stance
 Else
  pSeq(cyc,1)=ExtractAnimSeq(p(cyc),2020,2080,pSeq(cyc,604)) ;casual stance
 EndIf 
 pSeq(cyc,2)=ExtractAnimSeq(p(cyc),450,510,pSeq(cyc,605)) ;exhausted
 pSeq(cyc,3)=ExtractAnimSeq(p(cyc),1730,1760,pSeq(cyc,601)) ;prepare (early)
 pSeq(cyc,4)=ExtractAnimSeq(p(cyc),10,40,pSeq(cyc,601)) ;prepare (imminent)
 pSeq(cyc,5)=ExtractAnimSeq(p(cyc),660,700,pSeq(cyc,605)) ;prepare (weightlifting)
 pSeq(cyc,6)=ExtractAnimSeq(p(cyc),10,40,pSeq(cyc,601)) ;trainer encouragement
 pSeq(cyc,11)=ExtractAnimSeq(p(cyc),590,650,pSeq(cyc,605)) ;power exercise
 ;pSeq(cyc,12)=ExtractAnimSeq(p(cyc),2240,2385,pSeq(cyc,601)) ;reach exercise
 pSeq(cyc,12)=ExtractAnimSeq(p(cyc),520,579,pSeq(cyc,605)) ;skill exercise
 pSeq(cyc,13)=ExtractAnimSeq(p(cyc),2110,2230,pSeq(cyc,601)) ;agility exercise
 pSeq(cyc,14)=ExtractAnimSeq(p(cyc),725,785,pSeq(cyc,605)) ;stamina exercise
 pSeq(cyc,15)=ExtractAnimSeq(p(cyc),70,210,pSeq(cyc,606)) ;toughness exercise
 Animate p(cyc),1,Rnd#(0.25,0.5),pSeq(cyc,1),0
 If cyc=1 And charHealth(gamChar)<25 And InjuryStatus(gamChar)=0 Then Animate p(cyc),1,Rnd#(0.25,0.5),pSeq(cyc,2),0
 ;orientation
 PositionEntity p(cyc),pX#(cyc),pY#(cyc),pZ#(cyc)
 RotateEntity p(cyc),0,pA#(cyc),0  
 scaler#=0.055+(GetPercent#(charHeight(pChar(cyc)),24)/10000)
 ScaleEntity p(cyc),scaler#,scaler#,scaler# 
 ;shadows
 LoadShadows(cyc)
Next
;restore textures
RestoreTextures()
;point camera at camfoc
camType=10 : camFoc=no_plays
camPivX#=pX#(camFoc) : camPivY#=pY#(camFoc)+30 : camPivZ#=pZ#(camFoc)
PositionEntity camPivot,camPivX#,camPivY#,camPivZ#
;reset status
trainCourse=0 : trainAnim#=0
trainInput=0 : trainCPU=2 : trainScore=0
matchClock=0 : matchSecs=30
trainStage=1 : trainTim=0 : negTim=0
If no_plays=2 Or charHealth(gamChar)<25 Or InjuryStatus(gamChar)>0 Then trainStage=0
;frame rating
timer=CreateTimer(30)
;MAIN LOOP
foc=7 : oldfoc=foc : charged=0
go=0 : gotim=-20 : keytim=20
While go=0

 Cls
 frames=WaitTimer(timer)
 For framer=1 To frames
	
	;timers
	keytim=keytim-1
	If keytim<1 Then keytim=0 
	flashTim=flashTim+1
	If flashTim>15 Then flashTim=0   
	trainSoundTim=trainSoundTim-1 
	If trainSoundTim<0 Then trainSoundTim=0 
	
    ;MENU
    cyc=1
    gotim=gotim+1
    If gotim>50 Then trainTim=trainTim+1
    If trainStage=1 Then camFoc=0
	If trainStage=1 And trainTim>25 And keytim=0
	 ;highlight option
	 If KeyDown(200) Or JoyYDir()=-1 Then foc=foc-1 : PlaySound sMenuSelect : keytim=5
	 If KeyDown(208) Or JoyYDir()=1 Then foc=foc+1 : PlaySound sMenuSelect : keytim=5  
	 If foc<1 Then foc=7
	 If foc>7 Then foc=1
	 ;activate session
	 If KeyDown(28) Or ButtonPressed() Or MouseDown(1)
	  If foc=>1 And foc=<5
	   PlaySound sMenuGo : keytim=10
	   If optTrainLevel=0 Then matchSecs=10 Else matchSecs=20
	   trainCourse=foc : trainAnim#=0
	   trainStage=2 : trainTim=0 : trainScore=0
	   trainCPU=Rnd(2,5) : trainChanger=Rnd(2,5) : trainDrag=Rnd(0,5)
	   PositionEntity p(cyc),pX#(cyc),pY#(cyc),pZ#(cyc)
	   RotateEntity p(cyc),0,pA#(cyc),0 
	   If trainCourse=1
	    ShowEntity FindChild(p(cyc),"Barbell")
	   Else
	    HideEntity FindChild(p(cyc),"Barbell")
	   EndIf
	   ;If trainCourse=2 
	    ;ShowEntity FindChild(world,"Bag") : HideEntity FindChild(world,"Ball")
	    ;PositionEntity FindChild(world,"Bracket"),EntityX(FindChild(world,"Bracket"),1),56,EntityZ(FindChild(world,"Bracket"),1)
	    ;RotateEntity p(cyc),0,270,0 
	   ;EndIf
	   If trainCourse=2
	    HideEntity FindChild(world,"Bag") : ShowEntity FindChild(world,"Ball")
	    PositionEntity FindChild(world,"Bracket"),EntityX(FindChild(world,"Bracket"),1),44,EntityZ(FindChild(world,"Bracket"),1)
	    PositionEntity p(cyc),pX#(cyc)+12,pY#(cyc),pZ#(cyc)
	    RotateEntity p(cyc),0,270,0 
	   EndIf
	   If trainCourse=1 Then Animate p(cyc),1,0.5,pSeq(cyc,5),15
	   If trainCourse<>1 Then Animate p(cyc),1,0.25,pSeq(cyc,3),25
	   ResetOldValues(gamChar)
	  EndIf
	  If foc=6 Then PlaySound sMenuGo : keytim=10 : go=2
	  If foc=7 Then PlaySound sMenuBack : keytim=10 : go=-1 
	 EndIf
	 ;leave
	 If KeyDown(1) Then PlaySound sMenuBack : keytim=10 : go=-1
	EndIf

	;TRAINING PROCESS
	If trainStage=2
	 camFoc=1
	 ;preparatory animation 
	 If trainTim=75 And trainCourse<>1 Then Animate p(cyc),1,0.25,pSeq(cyc,4),25
	 If trainTim=115 Then Animate p(cyc),0,1.0,pSeq(cyc,10+trainCourse),10 
	 If trainTim=125
	  PlaySound sWhistle
	  If no_plays=2 Then Animate p(2),1,0.5,pSeq(2,6),10
	 EndIf
	 If trainTim>125
	  ;get input
	  trainOldInput=trainInput
	  If CommandsPressed()=<1
	   If KeyDown(keyAttack) Or JoyDown(buttAttack) Then trainInput=1
       If KeyDown(keyGrapple) Or JoyDown(buttGrapple) Then trainInput=2
       If KeyDown(keyRun) Or JoyDown(buttRun) Then trainInput=3
       If KeyDown(keyPickUp) Or JoyDown(buttPickUp) Then trainInput=4
       If KeyDown(keySwitch) Or JoyDown(buttSwitch) Then trainInput=5
       If KeyDown(keyTaunt) Or JoyDown(buttTaunt) Then trainInput=6
      EndIf
      ;translate input
      If optTrainLevel=0 Then trainScore=trainScore-15
      If optTrainLevel=1 Then trainScore=trainScore-(13+trainDrag)
      If optTrainLevel=2 Then trainScore=trainScore-(18+trainDrag)
      If optTrainLevel=3 Then trainScore=trainScore-(23+trainDrag)
      If trainScore>5000 Then trainScore=trainScore-1
	  If trainScore>10000 Then trainScore=trainScore-1
	  If trainScore>15000 Then trainScore=trainScore-1 
	  If charHealth(gamChar)=<75 Then trainScore=trainScore-1
	  If charHealth(gamChar)=<50 Then trainScore=trainScore-1
	  If charHealth(gamChar)=<25 Then trainScore=trainScore-1 
	  randy=Rnd(0,50)
	  If randy=0 Then trainChanger=Rnd(2,5)   
	  randy=Rnd(0,5*trainChanger)
	  If randy=0 Then trainCPU=Rnd(2,5)
	  randy=Rnd(-1,trainCPU)
      If (optTrainLevel>0 And trainInput<>trainOldInput) Or (randy=<0 And optTrainLevel=0)
       trainScore=trainScore+100
       If no_plays=2 Then trainScore=trainScore+10
       If gamAgreement(13)>0 Then trainScore=trainScore+20
      EndIf
      If trainScore<0 Then trainScore=0 
      If trainScore>20000 Then trainScore=20000
	  ;update animation 
	  progress#=GetPercent#(trainScore,trainTim*10)
	  If optTrainLevel=0 Then progress#=GetPercent#(trainScore/2,trainTim*5)
	  speeder#=1.0+PercentOf#(3.0,progress#) 
	  If speeder#>6.0 Then speeder#=6.0
	  trainGruntTim#=trainGruntTim#+speeder#
	  If trainCourse=1 Then speeder#=speeder#/4
	  If trainCourse=2 Then speeder#=speeder#-(speeder#/4)
	  If trainCourse=4 Then speeder#=speeder#/2
	  If trainCourse=5 Then speeder#=speeder#-(speeder#/4)
	  trainAnim#=trainAnim#+speeder#
	  SetAnimTime p(cyc),trainAnim#,pSeq(cyc,10+trainCourse)
	  ;sound effects
	  If trainSoundTim=0
	   If trainCourse=2
	    If (Int(AnimTime(p(cyc)))=>17 And Int(AnimTime(p(cyc)))=<19) Or (Int(AnimTime(p(cyc)))=>43 And Int(AnimTime(p(cyc)))=<45)
	     ProduceSound(0,sBlock(Rnd(1,4)),22050,0.3)
	     Animate world,3,2.0,1,3
	     trainSoundTim=5
	    EndIf
	   EndIf
	   If trainCourse=3
	    If (Int(AnimTime(p(cyc)))=>29 And Int(AnimTime(p(cyc)))=<31) Or (Int(AnimTime(p(cyc)))=>89 And Int(AnimTime(p(cyc)))=<91)
	     ProduceSound(0,sStep(Rnd(3,4)),22050,0.5)
	     trainSoundTim=5
	    EndIf
	   EndIf
	   If trainCourse=4
	    If (Int(AnimTime(p(cyc)))=>14 And Int(AnimTime(p(cyc)))=<16) Or (Int(AnimTime(p(cyc)))=>44 And Int(AnimTime(p(cyc)))=<46)
	     ProduceSound(0,sStep(Rnd(3,4)),22050,Rnd(0.25,0.75))
	     trainSoundTim=5
	    EndIf
	   EndIf
	   If trainCourse=5
	    If (Int(AnimTime(p(cyc)))=>19 And Int(AnimTime(p(cyc)))=<21) Or (Int(AnimTime(p(cyc)))=>89 And Int(AnimTime(p(cyc)))=<91)
	     ProduceSound(0,sImpact(Rnd(1,4)),22050,0.3)
	     trainSoundTim=5 : trainGruntTim#=999
	    EndIf
	   EndIf
	  EndIf
	  If trainGruntTim#>150 Then ProduceSound(0,sPain(Rnd(1,8)),GetVoice(cyc),Rnd(0.0,0.5)) : trainGruntTim#=0
	  ;clock
	  matchClock=matchClock-1
      If matchClock<0 Then matchSecs=matchSecs-1 : matchClock=59
      ;end session
      If matchSecs=<0 Or KeyDown(1)
       PlaySound sWhistle 
       matchSecs=0 : matchClock=0
       trainStage=3 : trainTim=0
       foc=1 : keytim=20 : gotim=0
       If KeyDown(1) Then trainScore=0
       If trainCourse=1 Then Animate p(cyc),1,0.5,pSeq(cyc,5),30
       If trainCourse<>1 Then Animate p(cyc),1,Rnd#(0.1,0.4),pSeq(cyc,2),30
       If trainCourse=2 Then RotateEntity p(cyc),0,240,0
       If no_plays=2 Then Animate p(2),1,Rnd#(0.1,0.4),pSeq(2,1),30
       TrainingEffects(gamChar)
       ;If (trainResult>1 And charManager(gamChar)=0) Or trainResult>2
        ;If optTrainLevel<2 Then optTrainLevel=optTrainLevel+1  
       ;EndIf 
      EndIf 
     EndIf
    EndIf

    ;AFTERMATH
    If trainStage=3
     camFoc=1
     ;alter stats
     AlterStats(gamChar)
     ;speed up
	 If gotim>25 And keytim>2 And foc=<9
	  If KeyDown(1) Or KeyDown(28) Or ButtonPressed() Or MouseDown(1) Then keytim=2
	 EndIf
	 ;proceed
	 If foc=10
	  FindWeightChanges(gamChar)
	  trainStage=1 : trainTim=15 : foc=7 : keytim=35 : gotim=0
	  If no_plays=2 Or charHealth(gamChar)<25 Then trainStage=4 : negTim=0
	 EndIf
    EndIf

    ;PROMOS
	If trainStage=0 Or trainStage=4
	 If gotim>25 Or trainStage>0 Then negTim=negTim+1
	 ;speed-ups
     If gotim>25 And keytim=0
	  If KeyDown(1) Or KeyDown(28) Or ButtonPressed() Or MouseDown(1) Then negTim=negTim+50 : keytim=5 ;: PlaySound sMenuBrowse
	 EndIf
	EndIf
 	
	;CHARACTER MANAGEMENT
	For cyc=1 To no_plays
	 ;facial expressions
	 If trainStage=2 And trainTim>125
	  pEyes(cyc)=1
	  If trainScore=>5000 Then pEyes(cyc)=2
	  If trainScore=>10000 Then pEyes(cyc)=3
	  pSpeaking(cyc)=1 
	 EndIf
	 FacialExpressions(cyc)
	 ManageEyes(cyc)
	 ;shadows
	 For limb=1 To 50
      If pShadow(cyc,limb)>0
       RotateEntity pShadow(cyc,limb),90,EntityYaw(pLimb(cyc,limb),1),0
       PositionEntity pShadow(cyc,limb),EntityX(pLimb(cyc,limb),1),pY#(cyc)+0.4,EntityZ(pLimb(cyc,limb),1)
      EndIf
     Next
	Next
	
	;CAMERA
	Camera()
	;music
	ManageMusic(-1)
	
 UpdateWorld
 Next 
 ;look at trainee
 If trainStage=2 And trainCourse=2 And trainTim<125 Then PointHead(1,FindChild(world,"Ball")) : LookAtObject(1,FindChild(world,"Ball")) 
 If no_plays=2
  If (trainStage=<1 And trainCourse=0) Then PointHead(1,FindChild(p(2),"Head")) : LookAtPerson(1,2)
  PointHead(2,FindChild(p(1),"Head")) : LookAtPerson(2,1) 
  If charEyeShape(pChar(2))=112 Then LookAtPerson(2,2)
 EndIf
 If charEyeShape(pChar(1))=112 Then LookAtPerson(1,1) 

 RenderWorld 1

 ;DISPLAY
 DrawImage gLogo(2),rX#(400),rY#(65)
 If trainStage=3 Then HighlightStats(gamChar)
 DrawProfile(gamChar,-1,-1,0)
 ;reset expressions
 For cyc=1 To no_plays
  pSpeaking(cyc)=0
 Next
 ;reset subtitles
 lineA$="" : lineB$=""
 redLineA$="" : redLineB$=""
 greenLineA$="" : greenLineB$=""
 ;PRE-EXERCISE PROMOS
 If trainStage=0 And no_plays=2
  camFoc=2 : camType=10
  ;too old to train
  If charPeaked(gamChar)>0
   If negTim>25 And negTim<325
    Speak(2,0,3)
    lineA$="Sorry, "+charName$(gamChar)+", but you're too old to train!"
    lineB$="I suspect you'd be wasting your energy for nothing..."
   EndIf
   If negTim>350 Then trainStage=1 : trainTim=0 : negTim=0 : keytim=15
  EndIf
  ;enough for 3 sessions
  If charHealth(gamChar)>75 And charPeaked(gamChar)=0
   If negTim>25 And negTim<325
    Speak(2,0,3)
    lineA$="You're in such good shape we can go nuts today,"
    lineB$=charName$(gamChar)+"! What would you like to work on?"
   EndIf
   If negTim>350 Then trainStage=1 : trainTim=0 : negTim=0 : keytim=15
  EndIf
  ;enough for 2 sessions
  If charHealth(gamChar)=>50 And charHealth(gamChar)=<75 And charPeaked(gamChar)=0
   If negTim>25 And negTim<325
    Speak(2,0,3)
    lineA$="You're good for a couple of training sessions,"
    lineB$=charName$(gamChar)+". What would you like to work on?"
   EndIf
   If negTim>350 Then trainStage=1 : trainTim=0 : negTim=0 : keytim=15
  EndIf
  ;enough for 1 session
  If charHealth(gamChar)=>25 And charHealth(gamChar)<50 And charPeaked(gamChar)=0 
   If negTim>25 And negTim<325
    Speak(2,0,2)
    lineA$="You can just about manage ONE training session,"
    lineB$=charName$(gamChar)+"! What would you like to work on?"
   EndIf
   If negTim>350 Then trainStage=1 : trainTim=0 : negTim=0 : keytim=15
  EndIf
  ;too tired to train
  If charHealth(gamChar)<25 And InjuryStatus(gamChar)=0 And charPeaked(gamChar)=0
   If negTim>25 And negTim<325
    Speak(2,0,3)
    lineA$="Sorry, "+charName$(gamChar)+", but you're too tired"
    lineB$="to train! Just take it easy for a while..."
   EndIf
   If negTim>350 Then go=1
  EndIf
  ;can't train when injured
  If InjuryStatus(gamChar)>0 And charPeaked(gamChar)=0
   If negTim>25 And negTim<325
    Speak(2,0,3)
    lineA$="Sorry, "+charName$(gamChar)+", but you can't train"
    lineB$="while injured! Give yourself a chance to heal..."
   EndIf
   If negTim>375 Then go=1
  EndIf
 EndIf
 ;solo player stops self
 If trainStage=0 And no_plays=1
  camFoc=1 : camType=10
  If negTim>25 And negTim<325 And charHealth(gamChar)<25 And InjuryStatus(gamChar)=0
   Speak(1,0,3)
   lineA$="Damn, I'm too tired to do anything today!"
   lineB$="I better take it easy until next week..."
  EndIf
  If negTim>25 And negTim<325 And InjuryStatus(gamChar)>0
   Speak(1,0,3)
   lineA$="Damn, I can't do anything while I'm injured!"
   lineB$="I'll have to take it easy until I've recovered..."
  EndIf
  If negTim>375 Then go=1
 EndIf
 ;OPTIONS
 If trainStage=1 And trainTim>25
  x=400 : y=175
  DrawOption(1,rX#(x),rY#(y),"STRENGTH","")
  DrawOption(2,rX#(x),rY#(y+40),"SKILL","")
  DrawOption(3,rX#(x),rY#(y+80),"AGILITY","")
  DrawOption(4,rX#(x),rY#(y+120),"STAMINA","") 
  DrawOption(5,rX#(x),rY#(y+160),"TOUGHNESS","")
  DrawOption(6,rX#(x),rY#(y+220),"SPARRING","")
  DrawOption(7,rX#(x),rY#(560),"<<< EXIT <<<","")  
 EndIf
 ;TRAINING DISPLAY
 If (trainStage=2 And trainTim>10) Or (trainStage=3 And foc=<9)
  ;clock
  DrawImage gClock,rX#(70),rY#(565)
  message=0
  If trainStage=2 And trainTim<125 Then message=1
  If trainStage=2 And trainTim=>125 And trainTim=<160 Then message=2
  If trainStage=3 And gotim=>0 And gotim=<50 Then message=3
  If message=0
   SetFont fontNews(8)
   Outline(Dig$(matchSecs,10),rX#(70)-11,rY#(565)-4,0,0,0,255,255,255)
   SetFont fontNews(2)
   Outline(":"+Dig$(matchClock,10),rX#(70)+18,rY#(565),0,0,0,255,255,255)
  EndIf
  If message=1
   SetFont font(5)
   Outline("GET",rX#(70),rY#(565)-15,0,0,0,255,Rnd(175,250),130)
   Outline("READY...",rX#(70),rY#(565)+5,0,0,0,255,Rnd(175,250),130)
  EndIf
  If message=2
   SetFont font(10)
   Outline("GO!",rX#(70),rY#(565)-5,0,0,0,50,Rnd(175,250),0)
  EndIf
  If message=3
   SetFont font(10)
   Outline("STOP!",rX#(70),rY#(565)-5,0,0,0,Rnd(175,250),0,0)
  EndIf
  ;meter
  x=140 : y=570
  Color 225,0,0 : Rect rX#(x),rY#(y)-15,rX#(600),30,1
  Color 125,0,0 : Rect rX#(x)+1,rY#(y)-14,rX#(600)-2,28,0 
  Color 50,120,250 : Rect rX#(x),rY#(y)-15,rX#(Float#(trainScore)/33.34),30,1
  Color 5,70,190 : Rect rX#(x)+1,rY#(y)-14,rX#(Float#(trainScore)/33.34)-1,28,0  
  Color 0,0,0 : Rect rX#(x),rY#(y)-15,rX#(600),30,0
  ;indicator lines
  SetFont fontNews(2)
  If trainScore=>5000 Then Color 255,250,100 Else Color 200,100,100
  Outline("LEVEL 1",rX#(x+150),rY#(y)-30,0,0,0,ColorRed(),ColorGreen(),ColorBlue())
  Line rX#(x+150),rY#(y)-18,rX#(x+150),rY#(y)+18
  Color 0,0,0 : Rect rX#(x+150)-1,rY#(y)-19,3,38,0
  If trainScore=>10000 Then Color 255,250,100 Else Color 200,100,100 
  Outline("LEVEL 2",rX#(x+300),rY#(y)-30,0,0,0,ColorRed(),ColorGreen(),ColorBlue()) 
  Line rX#(x+300),rY#(y)-18,rX#(x+300),rY#(y)+18 
  Color 0,0,0 : Rect rX#(x+300)-1,rY#(y)-19,3,38,0
  If trainScore=>15000 Then Color 255,250,100 Else Color 200,100,100
  Outline("LEVEL 3",rX#(x+450),rY#(y)-30,0,0,0,ColorRed(),ColorGreen(),ColorBlue())
  Line rX#(x+450),rY#(y)-18,rX#(x+450),rY#(y)+18
  Color 0,0,0 : Rect rX#(x+450)-1,rY#(y)-19,3,38,0
  ;advice
  If message=1 And flashTim=<10
   If optTrainLevel=0 Then namer$="AUTOMATIC TRAINING MODE! YOUR INPUT WILL NOT COUNT..."
   If optTrainLevel>0 Then namer$="PRESS ANY TWO COMMANDS ALTERNATELY TO FILL THE METER!"
   SqueezeFont(namer$,595,25)
   Outline(namer$,rX#(x+300),rY#(y),0,0,50,200,200,255)
  EndIf
 EndIf
 ;POST-EXERCISE PROMOS
 If trainStage=4 And no_plays=2
  camFoc=2 : camType=10
  ;failed
  If trainResult=0
   If negTim>25 And negTim<325 
    Speak(2,0,1)
    lineA$="What the hell was that, "+charName$(gamChar)+"?!"
    lineB$="We haven't made any progress here at all..."
   EndIf
   If negTim>350 Then trainStage=1 : trainTim=0 : negTim=0 : keytim=15
  EndIf 
  ;progress
  If trainResult>0
   If negTim>25 And negTim<325 
    If trainResult=1 Then intro$="Good job, "+charName$(gamChar)+"." : mood=2
    If trainResult=2 Then intro$="Nice work, "+charName$(gamChar)+"!" : mood=3
    If trainResult=3 Then intro$="Amazing stuff, "+charName$(gamChar)+"!" : mood=3
    Speak(2,0,mood)
    If trainProgress=0
     lineA$=intro$+" Unfortunately, it doesn't"
     If trainCourse=1 Then lineB$="seem to have had any effect on your strength..."
     If trainCourse=2 Then lineB$="seem to have had any effect on your skill..."
     If trainCourse=3 Then lineB$="seem to have had any effect on your agility..."
     If trainCourse=4 Then lineB$="seem to have had any effect on your stamina..."
     If trainCourse=5 Then lineB$="seem to have had any effect on your toughness..."
    EndIf
    If trainProgress>0
     lineA$=intro$+" We've managed"
     If trainCourse=1 Then lineB$="to increase your strength to "+charStrength(gamChar)+"%..."
     If trainCourse=2 Then lineB$="to increase your skill to "+charSkill(gamChar)+"%..."
     If trainCourse=3 Then lineB$="to increase your agility to "+charAgility(gamChar)+"%..."
     If trainCourse=4 Then lineB$="to increase your stamina to "+charStamina(gamChar)+"%..."
     If trainCourse=5 Then lineB$="to increase your toughness to "+charToughness(gamChar)+"%..."
    EndIf
   EndIf
   If negTim>350 Then trainStage=1 : trainTim=0 : negTim=0 : keytim=15
  EndIf
  ;find advice
  If trainStage=<1
   If gamSchedule(gamDate+1)>0 And charHealth(gamChar)=<50 Then trainStage=4 : negTim=0 : trainResult=-3
   If gamSchedule(gamDate)>0 And charHealth(gamChar)=<75 Then trainStage=4 : negTim=0 : trainResult=-2
   If charHealth(gamChar)<25 Then trainStage=4 : negTim=0 : trainResult=-1
  EndIf
  ;warn about forthcoming match
  If trainResult=-3
   If negTim>25 And negTim<325 
    Speak(2,0,3)
    lineA$="Remember you've got a match next week! We should"
    lineB$="probably wrap this up and preserve your energy..."
   EndIf
   If negTim>350 Then trainStage=1 : trainTim=0 : negTim=0 : keytim=15
  EndIf
  ;warn about match today!
  If trainResult=-2
   If negTim>25 And negTim<325 
    Speak(2,0,3)
    lineA$="Remember you've got a match today! We should"
    lineB$="probably wrap this up and preserve your health..."
   EndIf
   If negTim>350 Then trainStage=1 : trainTim=0 : negTim=0 : keytim=15
  EndIf
  ;advise stopping
  If trainResult=-1
   If negTim>25 And negTim<325 
    Speak(2,0,3)
    lineA$="Alright, you're looking pretty tired"
    lineB$="so I think that's enough for one day..."
   EndIf
   If negTim>375 Then go=1
  EndIf
 EndIf
 ;solo player stops self
 If trainStage=4 And no_plays=1
  camFoc=1 : camType=10
  If negTim>25 And negTim<325 
   Speak(1,0,3)
   lineA$="Damn, I'm exhausted after that!"
   lineB$="I think it's time I called it a day..."
  EndIf
  If negTim>375 Then go=1
 EndIf
 ;---------- DISPLAY SUBTITLES ----------
 DisplaySubtitles()
 ;cursor
 If foc<>oldfoc And trainStage<>3 And trainTim<>0 Then PlaySound sMenuSelect 
 oldfoc=foc 
 DrawImage gCursor,MouseX(),MouseY()
 ;mask shaky start
 If gotim<0 Then Loader("Please Wait","Preparing To Train")

 Flip
 ;screenshot (F12)
 If KeyHit(88) Then Screenshot()

Wend
;restore sound 
;chThemeVol#=PercentOf#(1.0,optMusicVolume)
;ChannelVolume chTheme,chThemeVol#	
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
 FreeEntity p(cyc)
 For limb=1 To 50
  If pShadow(cyc,limb)>0
   FreeEntity pShadow(cyc,limb)
  EndIf
 Next
Next
;proceed
If go=<1 Then screen=20
;sparring session
If go=2
 ResetCharacters()
 GetMatchRules(2) : AddGimmick(0)
 matchTimeLim=optLength
 pChar(1)=gamChar : pControl(1)=3
 pChar(2)=charPartner(gamChar)
 If pChar(2)=0 Then pChar(2)=charManager(gamChar)
 If pChar(2)=0 Then pChar(2)=AssignOpponent(gamChar,0,0)
 If no_refs>0 Then pChar(3)=AssignReferee()
 arenaPreset=1 : arenaAttendance=0
 If optFog>0 Then arenaAtmos=3 Else arenaAtmos=0
 arenaRopes=9 : arenaPads=Rnd(1,3)
 arenaApron=arenaPads : arenaCanvas=Rnd(1,4)
 arenaMatting=Rnd(0,3)
 screen=50 : screenAgenda=10
EndIf
End Function

;--------------------------------------------------------------
;////////////////// RELATED FUNCTIONS /////////////////////////
;--------------------------------------------------------------

;TRAINING EFFECTS
Function TrainingEffects(char)
 ;reset new values
 ResetNewValues(char)
 ;exhaust health
 charNewHealth(char)=charNewHealth(char)-25
 ;translate score
 trainResult=0
 If trainScore=>5000 Then trainResult=1
 If trainScore=>10000 Then trainResult=2
 If trainScore=>15000 Then trainResult=3
 ;negative effect
 If trainResult=0
  If trainCourse=1 Then charNewStrength(char)=charNewStrength(char)-Rnd(0,1)
  If trainCourse=2 Then charNewSkill(char)=charNewSkill(char)-Rnd(0,1)
  If trainCourse=3 Then charNewAgility(char)=charNewAgility(char)-Rnd(0,1)
  If trainCourse=4 Then charNewStamina(char)=charNewStamina(char)-Rnd(0,1)
  If trainCourse=5 Then charNewToughness(char)=charNewToughness(char)-Rnd(0,1)
  If trainScore=0 Then charNewAttitude(char)=charNewAttitude(char)-1
  If trainScore>0 Then charNewAttitude(char)=charNewAttitude(char)-Rnd(0,1)
  charNewHappiness(char)=charNewHappiness(char)-1
 EndIf
 ;positive effect
 If trainResult>0
  trainProgress=Rnd(0,trainResult+1)
  If trainProgress>trainResult Then trainProgress=trainResult
  If AverageStats(gamChar)=<60 Then trainProgress=trainResult
  If trainProgress>0
   If trainCourse=1 ;strength
    charNewStrength(char)=charNewStrength(char)+PursueValue(charStrength(char),100,trainProgress)
    randy=Rnd(1,3)
    If randy=1 Then charNewSkill(char)=charNewSkill(char)-Rnd(0,1)
    If randy=2 Then charNewAgility(char)=charNewAgility(char)-Rnd(0,1)
    If randy=3 Then charNewStamina(char)=charNewStamina(char)-Rnd(0,1)
    If charStrength(char)=>99 Then trainProgress=0
   EndIf
   If trainCourse=2 ;skill
    charNewSkill(char)=charNewSkill(char)+PursueValue(charSkill(char),100,trainProgress)
    randy=Rnd(1,2)
    If randy=1 Then charNewStrength(char)=charNewStrength(char)-Rnd(0,1)
    If randy=2 Then charNewToughness(char)=charNewToughness(char)-Rnd(0,1)
    If charSkill(char)=>99 Then trainProgress=0
   EndIf
   If trainCourse=3 ;agility
    charNewAgility(char)=charNewAgility(char)+PursueValue(charAgility(char),100,trainProgress)
    randy=Rnd(1,2)
    If randy=1 Then charNewStrength(char)=charNewStrength(char)-Rnd(0,1)
    If randy=2 Then charNewToughness(char)=charNewToughness(char)-Rnd(0,1)
    If charAgility(char)=>99 Then trainProgress=0
   EndIf
   If trainCourse=4 ;stamina
    charNewStamina(char)=charNewStamina(char)+PursueValue(charStamina(char),100,trainProgress)
    randy=Rnd(1,2)
    If randy=1 Then charNewStrength(char)=charNewStrength(char)-Rnd(0,1)
    If randy=2 Then charNewToughness(char)=charNewToughness(char)-Rnd(0,1)
    If charStamina(char)=>99 Then trainProgress=0
   EndIf
   If trainCourse=5 ;toughness
    charNewToughness(char)=charNewToughness(char)+PursueValue(charToughness(char),100,trainProgress)
    randy=Rnd(1,3)
    If randy=1 Then charNewSkill(char)=charNewSkill(char)-Rnd(0,1)
    If randy=2 Then charNewAgility(char)=charNewAgility(char)-Rnd(0,1)
    If randy=3 Then charNewStamina(char)=charNewStamina(char)-Rnd(0,1)
    If charToughness(char)=>99 Then trainProgress=0
   EndIf
  EndIf
  If trainProgress>0
   randy=Rnd(0,4)
   If randy=<1 Then charNewHappiness(char)=charNewHappiness(char)+Rnd(0,1)
   If randy=2 Then charNewAttitude(char)=charNewAttitude(char)+Rnd(0,1)
  Else
   charNewAttitude(char)=charNewAttitude(char)-Rnd(0,1)
   charNewHappiness(char)=charNewHappiness(char)-1
  EndIf
 EndIf
 ;check changes
 CheckNewValues(char)
End Function

;COUNT COMMANDS PRESSED
Function CommandsPressed()
 value=0
 If KeyDown(keyAttack) Or JoyDown(buttAttack) Then value=value+1
 If KeyDown(keyGrapple) Or JoyDown(buttGrapple) Then value=value+1
 If KeyDown(keyRun) Or JoyDown(buttRun) Then value=value+1
 If KeyDown(keyPickUp) Or JoyDown(buttPickUp) Then value=value+1
 If KeyDown(keySwitch) Or JoyDown(buttSwitch) Then value=value+1
 If KeyDown(keyTaunt) Or JoyDown(buttTaunt) Then value=value+1
 Return value
End Function

;ADJUST WEIGHT ACCORDING TO STAT CHANGES
Function FindWeightChanges(char)
 If charStrength(char)>charOldStrength(char) Then charWeightChange(char)=charWeightChange(char)+Rnd(1,3)
 If charSkill(char)>charOldSkill(char) Then charWeightChange(char)=charWeightChange(char)-1
 If charAgility(char)>charOldAgility(char) Then charWeightChange(char)=charWeightChange(char)-Rnd(1,3)
 If charStamina(char)>charOldStamina(char) Then charWeightChange(char)=charWeightChange(char)-1
 If charToughness(char)>charOldToughness(char) Then charWeightChange(char)=charWeightChange(char)+1
End Function