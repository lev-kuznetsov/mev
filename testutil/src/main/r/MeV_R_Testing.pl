#! /user/bin/perl
use strict;
use File::Basename;
use Cwd 'abs_path';
use Cwd;

#$DATA_FILE='/Users/yaoyu/Documents/mev/data/Affymetrix/affy_U133plus2_dataset.txt';
#$CONTRAST_FILE='/Users/yaoyu/Documents/mev/data/Affymetrix/affy_U133plus2_dataset.annotate_mtx.txt';
#$LIMMA_FILE='/Users/yaoyu/Documents/mev/limma/domain/src/main/r/limma.R';

my $DATADIR='../../../../data/Affymetrix/';
my $OUTDIR=$DATADIR."Test/";
my $DATA_FILE='../../../../data/Affymetrix/affy_U133plus2_dataset.txt';
my $SAMPLE_FILE='../../../../data/Affymetrix/affy_U133plus2_dataset.annotate_mtx.txt';
my $HOME=getcwd();
print ("$HOME\n");
#run_limma($DATA_FILE, $SAMPLE_FILE, $OUTDIR, '../../../../limma/domain/src/main/r/limma.R');
#run_ttest($DATA_FILE, $SAMPLE_FILE, $OUTDIR, '../../../../t-test/domain/src/main/r/mev_t_test.R');
#run_wilcoxon($DATA_FILE, $SAMPLE_FILE, $OUTDIR, '../../../../stats/domain/src/main/r/wilcoxon.r');
run_pca($DATA_FILE, $OUTDIR, '../../../../stats/domain/src/main/r/pca.r');

sub run_limma(){
	my ($DATA_FILE, $CONTRAST_FILE, $OUTDIR, $R_FILE)=@_;
	my $LIMMA_FILE=$R_FILE;
	my $LIMMA_CODE="";
	my $DIRNAME=dirname($LIMMA_FILE);
	my $CURR_OUTDIR=$OUTDIR."limma/";
	my $RNK_OUT=$CURR_OUTDIR."rnk_out.txt";
	my $RESULT_OUT=$CURR_OUTDIR."result_out.txt";
	my $TOPGO_OUT=$CURR_OUTDIR."topgo_out.txt";
	
	open(INLIMMA, $LIMMA_FILE);
	while(my $inline=<INLIMMA>){$LIMMA_CODE.=$inline;}
	$LIMMA_CODE=~s/INFILE/\"$DATA_FILE\"/g;
	$LIMMA_CODE=~s/SAMPLE_FILE/\"$CONTRAST_FILE\"/g;
	$LIMMA_CODE=~s/RNK_OUT/\"$RNK_OUT\"/g;
	$LIMMA_CODE=~s/RESULT_OUT/\"$RESULT_OUT\"/g;
	$LIMMA_CODE=~s/TOPGO_OUT/\"$TOPGO_OUT\"/g;
	print "run_limma:\t$OUTDIR\n";
	if(-d $CURR_OUTDIR){print "$CURR_OUTDIR exists\n";}else{mkdir($CURR_OUTDIR);}
	chdir($CURR_OUTDIR) or die "$!";
	my $TESTCODE="limma_test.R";
	open(OUTCODE, ">$TESTCODE");
	print OUTCODE $LIMMA_CODE;
	close(OUTCODE);
	system("Rscript $TESTCODE");
}

sub run_ttest(){
	#Required Input Parameters
	# INFILE
	# SAMPLE_FILE
	# ONE_SAMPLE
	# TWO_SAMPLE
	# USER_MU
	# TEST_TYPE
	# EQUAL_VARIANCE
	# PAIRED
	# CORRECT_FOR_MULTIPLE_TESTING
	# OUTFILE


	my ($DATA_FILE, $SAMPLE_FILE, $OUTDIR, $R_FILE)=@_;
	my $DIRNAME=dirname($R_FILE);
	my $CURR_OUTDIR=$OUTDIR."ttest/";
	
	my $R_FILE=$R_FILE;
	my $R_CODE="";
	my $OUTFILE=$CURR_OUTDIR."result_out.txt";
	my $ONE_SAMPLE=1;
	my $TWO_SAMPLE=2;
	my $TEST_TYPE=2;
	my $EQUAL_VARIANCE=1;
	my $PAIRED=1;
	my $CORRECT_FOR_MULTIPLE_TESTING=1;
	

	open(INCODE, $R_FILE);
	while(my $inline=<INCODE>){$R_CODE.=$inline;}
	$R_CODE=~s/INFILE/\"$DATA_FILE\"/g;	
	$R_CODE=~s/SAMPLE_FILE/\"$SAMPLE_FILE\"/g;	
	$R_CODE=~s/OUTFILE/\"$OUTFILE\"/g;	
	$R_CODE=~s/ONE_SAMPLE/$ONE_SAMPLE/g;	
	$R_CODE=~s/TWO_SAMPLE/$TWO_SAMPLE/g;	
	$R_CODE=~s/TEST_TYPE/$TEST_TYPE/g;	
	$R_CODE=~s/EQUAL_VARIANCE/$EQUAL_VARIANCE/g;	
	$R_CODE=~s/PAIRED/$PAIRED/g;	
	$R_CODE=~s/CORRECT_FOR_MULTIPLE_TESTING/$CORRECT_FOR_MULTIPLE_TESTING/g;	

	print "run_ttest:\t$OUTDIR\n";
	if(-d $CURR_OUTDIR){print "$CURR_OUTDIR exists\n";}else{mkdir($CURR_OUTDIR);}
	chdir($CURR_OUTDIR) or die "$!";
	my $TESTCODE="ttest.R";
	open(OUTCODE, ">$TESTCODE");
	print OUTCODE $R_CODE;
	close(OUTCODE);
	system("Rscript $TESTCODE");

	my $status=(-e $OUTFILE)?"created":"not created";
	print "run_ttest: $OUTFILE   was $status\n";
}

sub run_wilcoxon(){
	#Required Input Parameters
	# INFILE: DATA FILE matrix
	# SAMPLE_FILE: File store contrast
	# OUTFILE
	# ALT_HYPOTHESIS: 'two.sided'/'greater'/'less'. Default='two.sided'
	# IS_PAIRED: boolean. Default=FALSE
	# IS_CONF_INT: boolean. Default=FALSE
	# CORRECT_FOR_MULTIPLE_TESTING

	my ($DATA_FILE, $SAMPLE_FILE, $OUTDIR, $R_FILE)=@_;
	my $DIRNAME=dirname($R_FILE);
	my $CURR_OUTDIR=$OUTDIR."wilcoxon/";
	
	my $R_FILE=$R_FILE;
	my $R_CODE="";
	my $OUTFILE=$CURR_OUTDIR."result_out.txt";
	my $ALT_HYPOTHESIS='two.sided'; # 'two.sided'/'greater'/'less'. Default='two.sided'
	my $IS_PAIRED=0; 	#boolean. Default=FALSE
	my $IS_CONF_INT=1;	#boolean. Default=FALSE
	# CORRECT_FOR_MULTIPLE_TESTING

	open(INCODE, $R_FILE);
	while(my $inline=<INCODE>){$R_CODE.=$inline;}
	$R_CODE=~s/INFILE/\"$DATA_FILE\"/g;	
	$R_CODE=~s/SAMPLE_FILE/\"$SAMPLE_FILE\"/g;	
	$R_CODE=~s/OUTFILE/\"$OUTFILE\"/g;	
	$R_CODE=~s/ALT_HYPOTHESIS/\"$ALT_HYPOTHESIS\"/g;	
	#$R_CODE=~s/IS_PAIRED/$IS_PAIRED/g;	
	$R_CODE=~s/IS_CONF_INT/$IS_CONF_INT/g;	
	
	print "run_wilcoxon:\t$OUTDIR\n";
	if(-d $CURR_OUTDIR){print "$CURR_OUTDIR exists\n";}else{mkdir($CURR_OUTDIR);}
	chdir($CURR_OUTDIR) or die "$!";
	my $TESTCODE="wilcoxon.R";
	open(OUTCODE, ">$TESTCODE");
	print OUTCODE $R_CODE;
	close(OUTCODE);
	system("Rscript $TESTCODE");

	my $status=(-e $OUTFILE)?"created":"not created";
	print "run_wilcoxon: $OUTFILE   was $status\n";
}

sub run_pca(){
	#Required Input Parameters
	# INFILE: DATA FILE matrix
	# SAMPLE_FILE: File store contrast
	# OUTFILE
	# ALT_HYPOTHESIS: 'two.sided'/'greater'/'less'. Default='two.sided'
	# IS_PAIRED: boolean. Default=FALSE
	# IS_CONF_INT: boolean. Default=FALSE
	# CORRECT_FOR_MULTIPLE_TESTING

	my ($DATA_FILE, $OUTDIR, $R_FILE)=@_;
	my $DIRNAME=dirname($R_FILE);
	my $CURR_OUTDIR=$OUTDIR."pca/";
	
	my $R_FILE=$R_FILE;
	my $R_CODE="";
	my $OUTDIR=$CURR_OUTDIR;
	# CORRECT_FOR_MULTIPLE_TESTING

	open(INCODE, $R_FILE);
	while(my $inline=<INCODE>){$R_CODE.=$inline;}
	$R_CODE=~s/INFILE/\"$DATA_FILE\"/g;	
	$R_CODE=~s/OUTDIR/\"$OUTDIR\"/g;	
	
	print "run_pca:\t$OUTDIR\n";
	if(-d $CURR_OUTDIR){print "$CURR_OUTDIR exists\n";}else{mkdir($CURR_OUTDIR);}
	chdir($CURR_OUTDIR) or die "$!";
	my $TESTCODE="pca.R";
	open(OUTCODE, ">$TESTCODE");
	print OUTCODE $R_CODE;
	close(OUTCODE);
	system("Rscript $TESTCODE");

	my $status=(-e $OUTDIR)?"created":"not created";
	print "run_pca: $OUTDIR   was $status\n";
	#system("rm $TESTCODE");
	#print "run_pca: $TESTCORDE was removed\n";
	
}


