# A R script for colocalization with Bio7 using the ROI Manager ImageJ to R transfer.
# Author: M. Austenfeld
library(RColorBrewer)

result_pearson <- NULL
result_mander <- NULL
allVars <- NULL

# Pearson’s Correlation coefficient - Rr
pearson_coeff <- function(channel1, channel2) {
    ch1 <- channel1 - mean(channel1)
    ch2 <- channel2 - mean(channel2)
    sum_ch <- sum(ch1 * ch2)
    sqrt_ch <- sqrt(sum(ch1^2) * sum(ch2^2))
    return(sum_ch/sqrt_ch)
}
#Manders Overlap Coefficient - R
manders_coeff <- function(channel1, channel2) {
    sum_ch <- sum(channel1 * channel2)
    sqrt_ch <- sqrt(sum(channel1^2) * sum(channel2^2))
    return(sum_ch/sqrt_ch)
}
# Overlap Coefficients for red and green – kred and kgreen
overlap_coeff_rg<-function(channel1, channel2){
	sum_ch <- sum(channel1 * channel2)
	kred<-sum_ch/channel1^2
	kgreen<-sum_ch/channel2^2
	result<-c(kred,kgreen)
	return(result)
}
# Colocalisation Coefficients – Mred, Mgreen
coloc_coeff_rg<-function(channel1, channel2){
	sum_ri<-sum(channel1[channel2>0])
	mred<-sum_ri/sum(channel1)
	sum_gi<-sum(channel2[channel1>0])
	mgreen<-sum_gi/sum(channel2)
	return(c(mred,mgreen))
}
# ICQ - Intensity Correlation Quotient
icq_Coeff<-function(channel1, channel2){
	ch1 <- channel1 - mean(channel1)
    ch2 <- channel2 - mean(channel2)
    pdm_ch <- ch1 * ch2
    nve<-length(pdm_ch[pdm_ch>0])
    ntot1<-length(channel1[channel1!=0])
    ntot2<-length(channel2[channel2!=0])
    ntotal<-ntot1+ntot2
    icq<-(nve/ntotal)-0.5
    return(icq)
}
# Tests all ROI's of all layers of a stack with R,G channels.
allColocStatsFromWsVars <- function() {
	result_pearson <<- NULL
	result_mander <<- NULL
    # Get all variables from the workspace as objects! Filter out all with
    # a certain prefix!
    allVars <<- mget(ls(envir = .GlobalEnv, pattern = "X0"), envir = .GlobalEnv)
	amount_layers <- dim(allVars[[1]])[2]
    for (x in 1:length(allVars)) {
    	# Variable to measure r,g layers in the ROI!
        mat <- allVars[[x]]
        # Iterate over amount of layers (by increment 2)! 
        for (y in seq(1, amount_layers-1, by=2)){        	     
            p_coff <- pearson_coeff(mat[, y], mat[, y + 1])
            m_coff <- manders_coeff(mat[, y], mat[, y + 1])
            result_pearson <<- c(result_pearson, p_coff)
            result_mander <<- c(result_mander, m_coff)
        }
    }
}

callScatterGraph <- function(x, y) {
    coldef = c("#313695", "#4575B4", "#74ADD1", "#ABD9E9", "#E0F3F8", "#FFFFBF", 
        "#FEE090", "#FDAE61", "#F46D43", "#D73027", "#A50026")
    col_pal = colorRampPalette(c(coldef))
    # par(bg = "#313695")
    # par(mar = c(0,0,0,0))
    smoothScatter(x, y, colramp = col_pal,main = "Plot Layers", xlab = "Layer 1", 
        ylab = "Layer 2")
    #Add a regression line!
    abline(v=min(x), col="purple",lty = 1, lwd = 3)
    abline(h=min(y), col="purple",lty = 1, lwd = 3)
    abline(lm(x ~ y), col = "red2", lty = 1, lwd = 3)
    plot(x,y,add=T)
}

