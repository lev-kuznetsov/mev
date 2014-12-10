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
my $CONTRAST_FILE='../../../../data/Affymetrix/affy_U133plus2_dataset.annotate_mtx.txt';
my $HOME=getcwd();
print ("$HOME\n");
run_limma($DATA_FILE, $CONTRAST_FILE, $OUTDIR);


sub run_limma(){
	my ($DATA_FILE, $CONTRAST_FILE, $OUTDIR)=@_;
	my $LIMMA_FILE='../../../../limma/domain/src/main/r/limma.R';
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