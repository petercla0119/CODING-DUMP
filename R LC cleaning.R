setwd("~/Downloads")

library(dplyr)
library(ggplot2)
library(tidyr)
library(readxl)
library(reshape2)

#import data
all.raw <- read_excel("Total LC Phase 1.xlsx")
cell.dat.raw <- read_excel("combined_measures_noedit.xlsx")


slideid_pattern <- "^[0-9]{2,4}(?# Autopsy Year matching 2-4 digits between 0-9)\\-([0-9]{1,3})(?# Autopsy case number matching 1-3 digits between 0-1)\\-([0-9]{1,2})([EF])"

names(all.raw)
names(cell.dat.raw)
df.wide = reshape(df2, idvar='autopsyID.full', timevar='Cohort', direction='wide') 


total.area <- all.raw[,c(2, 5:6, 10, 13, 14)] #selects following columns: image, parent, roi, postive pixel area, total roi area, positive % of total roi
colnames(total.area)[grep ("(.*?)of.total.ROI(.*)", names(total.area))] <- "total LC percent.AO" # Rename the %AO column to avoid special characters using grep()
total.area$slideID <- stringr::str_extract_all(as.matrix(total.area[,1]), slideid_pattern, simplify = TRUE)  #Create a new column in df to contain SlideID
total.area$autopsyID <- stringr::str_extract_all(as.matrix(total.area[,1]), "^([0-9]{2,4})\\-([0-9]{1,3})", simplify = TRUE)  #Create a new column in df to contain AutopsyID


cell.data <- cell.dat.raw[,c(2, 5:6, 10, 13, 14)]
colnames(cell.data)[grep ("(.*?)of.total.ROI(.*)", names(cell.data))] <- "individual cell percent.AO" # Rename the %AO column to avoid special characters using grep()
cell.data$slideID <- stringr::str_extract_all(as.matrix(cell.data[,1]), slideid_pattern, simplify = TRUE)  #Create a new column in df to contain SlideID
cell.data$autopsyID <- stringr::str_extract_all(as.matrix(cell.data[,1]), "^([0-9]{2,4})\\-([0-9]{1,3})", simplify = TRUE)  #Create a new column in df to contain AutopsyID

#### Convert columns to factors/numerics ####
cell.data$autopsyID <- as.factor(cell.data$autopsyID)
cell.data$`Positive pixel area µm^2` <- as.numeric(cell.data$`Positive pixel area µm^2`)
cell.data$`Total ROI area µm^2` <- as.numeric(cell.data$`Total ROI area µm^2`)
cell.data$`individual cell percent.AO` <- as.numeric(cell.data$`individual cell percent.AO`)
cell.data$Parent <- as.factor(cell.data$Parent)


cell.data.clean <- cell.data %>% 
  filter(!Parent=="Image") %>%
  select(autopsyID, `Positive pixel area µm^2`, `Total ROI area µm^2`, `individual cell percent.AO`) %>%
  group_by(autopsyID) %>%
  summarise(total.cell.count = n(),
            Number.NM.TH.Cells = sum(`Positive pixel area µm^2` > 0, na.rm = T),
            Sum.IC.NM.Area = sum(`Positive pixel area µm^2`, na.rm = TRUE),
            Sum.Total.Cell.Area = sum(`Total ROI area µm^2`, na.rm = TRUE))



# `Positive pixel area µm^2` = `Positive pixel area µm^2`, 
# `Total ROI area µm^2` = `Total ROI area µm^2`, 
# `individual cell percent.AO` = `individual cell percent.AO`


ic.nm <- cell.data %>%
  select(autopsyID, Parent, `Positive pixel area µm^2`, `Total ROI area µm^2`, `individual cell percent.AO`) %>%
  mutate(avg.IC.NM = (mean(cell.data$`Positive pixel area µm^2`, na.rm = TRUE)/cell.data$`Total ROI area µm^2`)*100) %>%
  filter(!Parent=="Image")

#write.csv(ic.nm, "IC.NM Data.csv", row.names = FALSE) #write NM cell data as csv

# #summarize cell data 
# cell.data %>% 
#   group_by(autopsyID) %>%
#   summarise(sum.positive.cell = )

# cell.count <- cell.data %>%
#   group_by(autopsyID) %>%
#   count()



# Total area
total.area$autopsyID <- as.factor(total.area$autopsyID)
total.area$`Positive pixel area µm^2` <- as.numeric(total.area$`Positive pixel area µm^2`)
total.area$`Total ROI area µm^2` <- as.numeric(total.area$`Total ROI area µm^2`)
# total.area <- total.area[c(1,17:56),]
total.area <- total.area[,c(4:5,8)]
colnames(total.area) <- c("Total.NM.Area.in.LC", "Total.LC.Area", "LC.Area.AutopsyID" )
#colnames(total.area) <- paste("total", colnames(total.area), sep = "_")
#match(total.area$autopsyID,df$autopsyID)

#### Combine cell measures with total LC measures
combdf <- cbind(cell.data.clean,total.area)

combdf <- combdf %>% 
  mutate(Pct.NM.TH.Pos.Cells = (Number.NM.TH.Cells/total.cell.count)*100,
         EC.NM.Area = Total.NM.Area.in.LC - Sum.IC.NM.Area,
         Pct.EC.NM.Area = (EC.NM.Area/Total.NM.Area.in.LC)*100,
         difference = Total.NM.Area.in.LC-Sum.IC.NM.Area)

write.csv(combdf, "combined LC data_12_27_2019.csv", row.names = F)






### Dan's Weird Histograms
alldigital.cases<- read_excel("Copy of LC_Master_v12_.xlsx", sheet = "Sheet1")
diagnoses<- alldigital.cases %>%
  select(AutopsyID, Cohort, cohort_recoded, NPDx1, ClinicalDx1, Sex, Race, Ethnicity, Education, HighestOccupation,AgeatDeath,PMI,VentricularEnlargement,BrainWeight,Braak03,Braak06,ABeta,CERAD, FTLDTDP,ARTAG,Arteriolosclerosis,TDP43 )

ic.nm <- read.csv("combined LC data_12_27_2019.csv")

cases.to.select= unique(ic.nm$autopsyID)
diagnoses$AutopsyID<-as.factor(diagnoses$AutopsyID)
selected.cases <- diagnoses %>%
  filter(AutopsyID  %in% cases.to.select)


ic.nm <- ic.nm %>% 
  # add unique id for rows to be able to use spread
  group_by(autopsyID) %>%
  mutate(unique_id = row_number())
#ic.nm <- ic.nm[,-c(2)] #remove parent column
merged.df.wide <- merge (ic.nm, selected.cases, by.x="autopsyID", by.y="AutopsyID")
cell.dat.long <-melt(merged.df.wide, id.vars=c("autopsyID", "cohort_recoded"))



#########TABLE ONE PACKAGE########
library(tableone)
factorVars <- c("Cohort", "cohort_recoded", "NPDx1", "ClinicalDx1", "Sex", "Race", "Ethnicity", "Education", "HighestOccupation", "AgeatDeath", "PMI", "VentricularEnlargement", "Braak03", "Braak06", "ABeta", "CERAD", "FTLDTDP", "ARTAG", "Arteriolosclerosis")

vars <- c("Cohort", "cohort_recoded", "NPDx1", "ClinicalDx1", "Sex", "Race", "Ethnicity", "Education", "HighestOccupation", "AgeatDeath", "PMI", "VentricularEnlargement", "Braak03", "Braak06", "ABeta", "CERAD", "FTLDTDP", "ARTAG", "Arteriolosclerosis", "EC.NM.Area", "Pct.EC.NM.Area", "Pct.NM.TH.Pos.Cells", "total.cell.count","Number.NM.TH.Cells")
tableOne <- CreateTableOne(vars = vars, data = merged.df.wide, factorVars = factorVars)
tableOne



breaks <- c(0,10,20,30,40,50,60,70,80,90,100)
# specify interval/bin labels
tags <- c("10","20", "30","40","50","60","70","80","90", "100")
# bucketing values into bins
group_tags <- cut(merged.df.wide$individual.cell.percent.AO, 
                  breaks=breaks, 
                  include.lowest=TRUE, 
                  right=TRUE, 
                  labels=tags)
# inspect bins
summary(group_tags)

merged.df.wide<-merge(ic.nm, selected.cases, by.x="autopsyID", by.y="AutopsyID")


ggplot(merged.df.wide, mapping = aes(merged.df.wide$indivudal.cell.percent.AO)) + 
  geom_histogram(aes(merged.df.wide$indivudal.cell.percent.AO, binwidth=30))


merged.df.wide$cohort_recoded<- as.factor(merged.df.wide$cohort_recoded)
ggplot(data=merged.df.wide, aes(merged.df.wide$individual.cell.percent.AO)) + 
  geom_histogram(breaks=seq(0, 100, by = 10)) + 
  labs(title="Histogram for LC Cells") +
  labs(x="% IC NM", y="Count") + 
  xlim(c(0,100)) + 
  ylim(c(0,300)) +
  facet_wrap(~cohort_recoded)

selected.cases %>%
  group_by(NPDx1) %>%
  count()


