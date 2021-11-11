IF EXISTS(select * from INFORMATION_SCHEMA.ROUTINES where ROUTINE_NAME = 'spPrilogeClanovReportGetList')
BEGIN
	drop procedure dbo.spPrilogeClanovReportGetList
END
GO

--------------------------------------------------------------------------------------------
-- Get data at specific condition from dbo.PRILOGECLANOVREPORT
-- History:
-- 03.02.2019 Jure; created
--------------------------------------------------------------------------------------------
CREATE PROCEDURE [dbo].[spPrilogeClanovReportGetList]
	@search_query varchar(max),
	@page_number int = 1,
	@rows_per_page int = 30
AS
BEGIN

	-- EDM Data contract
	if (1=2) begin
		select 
			PRILOGECLANOVREPORT.id as id,
			PRILOGECLANOVREPORT.kmgmid_imetnika as kmgmid_imetnika,
			PRILOGECLANOVREPORT.naziv_imetnika as naziv_imetnika,
			PRILOGECLANOVREPORT.stevilka_certifikata as stevilka_certifikata,
			PRILOGECLANOVREPORT.shema as shema,
			PRILOGECLANOVREPORT.naziv_zascita as naziv_zascita,
			PRILOGECLANOVREPORT.naziv_proizvod as naziv_proizvod,
			PRILOGECLANOVREPORT.kmgmid_clana as kmgmid_clana,
			PRILOGECLANOVREPORT.naziv_clana as naziv_clana,
			PRILOGECLANOVREPORT.davcna_clana as davcna_clana,
			PRILOGECLANOVREPORT.naslov_clana as naslov_clana,
			PRILOGECLANOVREPORT.stevilka as stevilka,
			PRILOGECLANOVREPORT.dat_izdaje as dat_izdaje,
			PRILOGECLANOVREPORT.dat_velj as dat_velj,
			PRILOGECLANOVREPORT.status as status
		from 
			dbo.PRILOGECLANOVREPORT
			
		WHERE 
			1=2 
	end

	declare @cmd varchar(8000) = ' 
		select 
			PRILOGECLANOVREPORT.id as id,
			PRILOGECLANOVREPORT.kmgmid_imetnika as kmgmid_imetnika,
			PRILOGECLANOVREPORT.naziv_imetnika as naziv_imetnika,
			PRILOGECLANOVREPORT.stevilka_certifikata as stevilka_certifikata,
			PRILOGECLANOVREPORT.shema as shema,
			PRILOGECLANOVREPORT.naziv_zascita as naziv_zascita,
			PRILOGECLANOVREPORT.naziv_proizvod as naziv_proizvod,
			PRILOGECLANOVREPORT.kmgmid_clana as kmgmid_clana,
			PRILOGECLANOVREPORT.naziv_clana as naziv_clana,
			PRILOGECLANOVREPORT.davcna_clana as davcna_clana,
			PRILOGECLANOVREPORT.naslov_clana as naslov_clana,
			PRILOGECLANOVREPORT.stevilka as stevilka,
			PRILOGECLANOVREPORT.dat_izdaje as dat_izdaje,
			PRILOGECLANOVREPORT.dat_velj as dat_velj,
			PRILOGECLANOVREPORT.status as status
		from 
			dbo.PRILOGECLANOVREPORT
			
	'

	if isnull(@search_query, '') != '' begin
		set @cmd = @cmd + 'where '
			set @cmd = @cmd + replace('PRILOGECLANOVREPORT.kmgmid_imetnika like ''%[0]%'' OR ', '[0]', @search_query)
			set @cmd = @cmd + replace('PRILOGECLANOVREPORT.naziv_imetnika like ''%[0]%'' OR ', '[0]', @search_query)
			set @cmd = @cmd + replace('PRILOGECLANOVREPORT.stevilka_certifikata like ''%[0]%'' OR ', '[0]', @search_query)
			set @cmd = @cmd + replace('PRILOGECLANOVREPORT.shema like ''%[0]%'' OR ', '[0]', @search_query)
			set @cmd = @cmd + replace('PRILOGECLANOVREPORT.naziv_zascita like ''%[0]%'' OR ', '[0]', @search_query)
			set @cmd = @cmd + replace('PRILOGECLANOVREPORT.naziv_proizvod like ''%[0]%'' OR ', '[0]', @search_query)
			set @cmd = @cmd + replace('PRILOGECLANOVREPORT.kmgmid_clana like ''%[0]%'' OR ', '[0]', @search_query)
			set @cmd = @cmd + replace('PRILOGECLANOVREPORT.naziv_clana like ''%[0]%'' OR ', '[0]', @search_query)
			set @cmd = @cmd + replace('PRILOGECLANOVREPORT.davcna_clana like ''%[0]%'' OR ', '[0]', @search_query)
			set @cmd = @cmd + replace('PRILOGECLANOVREPORT.naslov_clana like ''%[0]%'' OR ', '[0]', @search_query)
			set @cmd = @cmd + replace('PRILOGECLANOVREPORT.stevilka like ''%[0]%'' OR ', '[0]', @search_query)
			set @cmd = @cmd + replace('PRILOGECLANOVREPORT.status like ''%[0]%''', '[0]', @search_query)
	end

	set @cmd = @cmd + '
	ORDER BY 
			PRILOGECLANOVREPORT.id
	OFFSET ((@page_number - 1) * @rows_per_page) ROWS
	FETCH NEXT @rows_per_page ROWS ONLY;'
	
	set @cmd = REPLACE(@cmd, '@page_number', @page_number)
	set @cmd = REPLACE(@cmd, '@rows_per_page', @rows_per_page)
	
	print(@cmd)
	exec(@cmd)
END

